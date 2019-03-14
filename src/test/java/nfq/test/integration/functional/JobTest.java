package nfq.test.integration.functional;

import nfq.bidding.entity.Job;
import nfq.bidding.repository.BiddingRepository;
import nfq.test.BaseIntegrationTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

public class JobTest extends BaseIntegrationTest {
    @Resource
    private WebTestClient client;

    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void testCreateNewJobSuccessful() {
        Job newJob = createNewJob(null);
        Long newJobId = client.post().uri("/api/jobs/?userId=" + AUCTIONEER_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(newJob)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Long.class)
                .returnResult().getResponseBody();
        Assert.assertTrue(newJobId > JOB_ID);
    }

    @Test
    public void testGetAllJobOfUser() {
        Job newJob = createNewJob(null);
        List<Job> results = client.get().uri("/api/users/"+ AUCTIONEER_ID +"/jobs")
                .exchange()
                .expectStatus().isOk()
                .expectBody(List.class)
                .returnResult().getResponseBody();

        Assert.assertTrue(results.size() == 1);
    }

    @Test
    public void testAuctioneerCannotChangePriceWhenJobInBidding() {
        // There are someone just bid this job
        client.post().uri("/api/jobs/" + JOB_ID + "/bidding?userId=" + BIDDER_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(createNewBid(BigDecimal.valueOf(51)))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Long.class);

        // Change the job price and check
        Job job = jobRepository.findById(JOB_ID).get();
        job.setPrice(BigDecimal.valueOf(45));
        String responseBody = client.post().uri("/api/jobs/" + JOB_ID + "?userId=" + AUCTIONEER_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(job)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .returnResult().getResponseBody();

        Assert.assertTrue(responseBody.contains("has been in bidding by candidates, price is not allowed to modify"));
    }

    @After
    public void tearDown() {
        super.tearDown();
    }
}
