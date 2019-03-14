package nfq.test.load;

import nfq.bidding.entity.Bidding;
import nfq.bidding.entity.User;
import nfq.bidding.repository.BiddingRepository;
import nfq.bidding.repository.JobRepository;
import nfq.bidding.repository.UserRepository;
import nfq.test.BaseIntegrationTest;
import org.junit.*;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LoadTest extends BaseIntegrationTest {
    private static final int NUMBER_USERS = 1000;
    private static long JOB_ID;

    private static long START_TIME;
    private static long LOAD_TEST_TIME = 120; //Total load test running time in second
    private static final int BIDDER_PERIOD = 25; //Each bidder bid the job for period second

    private ScheduledExecutorService scheduler;

    private Map<Long, User> bidderById = new HashMap<>();

    @Resource
    private UserRepository userRepository;

    @Resource
    private JobRepository jobRepository;

    @Resource
    private BiddingRepository biddingRepository;

    @Resource
    private TestRestTemplate restTemplate;

    @Before
    @Override
    public void setup() {
        //Create auctioneer user
        User auctioneer = userRepository.save(new User("auc1@gmail.com"));
        //Create job
        JOB_ID = jobRepository.save(createNewJob(auctioneer)).getId();

        for (int i = 1; i <= NUMBER_USERS; i++) {
            User bidder = new User("bidder" + i + "@mail.com");
            bidder = userRepository.save(bidder);
            bidderById.put(bidder.getId(), bidder);
        }
    }

    @Test
    public void loadTest() throws InterruptedException {
        START_TIME = System.currentTimeMillis();

        scheduler = Executors.newScheduledThreadPool(NUMBER_USERS);
        List<CompletableFuture> futures = new ArrayList<>();

        for (long bidderId : bidderById.keySet()) {
            futures.add(scheduleBidder(bidderId));
        }

        this.await(LOAD_TEST_TIME);

        CompletableFuture finalFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[]{}));
        assertException(finalFuture);
        assertResult(futures);
    }

    private void assertResult(List<CompletableFuture> futures) {
        CompletableFuture[] futuresArray = futures.toArray(new CompletableFuture[]{});

        // Combine all results
        Set<Long> savedBiddingIds = Stream.of(futuresArray)
                .map(future -> (List<Long>) future.join())
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        long numberInDatabase = biddingRepository.count();
        Assert.assertEquals(numberInDatabase, savedBiddingIds.size());
    }

    private void assertException(CompletableFuture future) {
        try {
            future.get();
        } catch (Exception e) {
            Assert.fail("Test failed due to" + e.getMessage());
        }
    }

    private void await(long duration) throws InterruptedException {
        boolean outOfTime = false;
        while (!outOfTime && !scheduler.isTerminated()) {
            long timeTillNow = (System.currentTimeMillis() - START_TIME) / 1000;

            if (timeTillNow >= duration) {
                outOfTime = true;
                scheduler.shutdown();
            } else {
                Thread.sleep(1000);
            }
        }
    }

    private CompletableFuture<List<Long>> scheduleBidder(long bidderId) {
        Random random = new Random();
        CompletableFuture<List<Long>> currentFuture = CompletableFuture.completedFuture(new ArrayList<>());

        //Create thread with CompletableFuture
        Runnable thread = createThread(bidderId, currentFuture);
        scheduler.scheduleWithFixedDelay(thread,
                random.nextInt(BIDDER_PERIOD), BIDDER_PERIOD, TimeUnit.SECONDS);

        return currentFuture;
    }

    private Runnable createThread(long bidderId, CompletableFuture<List<Long>> future) {
        WebTestClient client = WebTestClient.bindToServer().baseUrl("http://localhost:" + this.port)
                .responseTimeout(Duration.ofMillis(35000)).build();

        Bidding bidding = createNewBid(BigDecimal.valueOf(51));

        return new Runnable() {
            private int numberRequest = 0;
            private List<Long> biddingIds = new ArrayList<>();

            @Override
            public void run() {
                long tickTime = (System.currentTimeMillis() - START_TIME) / 1000;
                logger.info("Bidder " + bidderId + " making a bid to job " + JOB_ID + " at second: " + tickTime);

                try {
                    //Make a bidding request
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
                    HttpEntity<Bidding> httpEntity = new HttpEntity<>(bidding, httpHeaders);
                    ResponseEntity<Long> response = restTemplate.postForEntity("/api/jobs/" + JOB_ID + "/bidding?userId=" + bidderId,
                            httpEntity, Long.class);

                    //save results into property
                    biddingIds.add((Long) response.getBody());

                    //Add results into future
                    logger.info("Bidder" + bidderId + " number request: " + numberRequest + " bidding:" + biddingIds.toString());
                    future.obtrudeValue(biddingIds);

                } catch (Exception e) {
                    future.obtrudeException(e);
                    scheduler.shutdown();
                }
            }
        };
    }

    @After
    @Override
    public void tearDown() {
        //Do nothing since this is load test
    }
}
