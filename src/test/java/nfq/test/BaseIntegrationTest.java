package nfq.test;

import nfq.bidding.BiddingSystemApplication;
import nfq.bidding.constant.JobStatus;
import nfq.bidding.entity.Bidding;
import nfq.bidding.entity.Job;
import nfq.bidding.entity.User;
import nfq.bidding.repository.BiddingRepository;
import nfq.bidding.repository.JobRepository;
import nfq.bidding.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@ContextConfiguration(classes = {BiddingSystemApplication.class})
public class BaseIntegrationTest {
    protected static final Logger logger = LoggerFactory.getLogger(BaseIntegrationTest.class);

    protected static long JOB_ID;
    protected static long BIDDER_ID;
    protected static long AUCTIONEER_ID;

    @LocalServerPort
    protected int port;

    @Resource
    protected UserRepository userRepository;

    @Resource
    protected JobRepository jobRepository;

    @Resource
    private BiddingRepository biddingRepository;

    @Before
    public void setup() {
        User auctioneer = new User("auc1@mail.com");
        auctioneer = userRepository.save(auctioneer);
        AUCTIONEER_ID = auctioneer.getId();

        //Create a new bidder
        User bidder = new User("bidder1@mail.com");
        BIDDER_ID = userRepository.save(bidder).getId();

        JOB_ID = jobRepository.save(createNewJob(auctioneer)).getId();
    }

    protected Bidding createNewBid(BigDecimal price) {
        Bidding bidding = new Bidding();
        bidding.setBidPrice(price);
        bidding.setComment("Testing bidding");
        return bidding;
    }

    protected Job createNewJob(User user) {
        Job job = new Job();
        job.setUser(user);
        job.setStatus(JobStatus.IN_PROGRESS);
        job.setPrice(BigDecimal.valueOf(50));
        return job;
    }

    @After
    public void tearDown() {
        biddingRepository.deleteAll();
        jobRepository.deleteAll();
        userRepository.deleteAll();
    }
}
