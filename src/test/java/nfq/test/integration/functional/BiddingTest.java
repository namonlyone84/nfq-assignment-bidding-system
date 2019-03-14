package nfq.test.integration.functional;

import nfq.test.BaseIntegrationTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import javax.annotation.Resource;
import java.math.BigDecimal;

public class BiddingTest extends BaseIntegrationTest {
    @Resource
    private WebTestClient client;

    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void testBiddingPriceCannotLowerThanJobPrice() {
        String responseBody = client.post().uri("/api/jobs/" + JOB_ID + "/bidding?userId=" + BIDDER_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(createNewBid(BigDecimal.valueOf(49)))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .returnResult().getResponseBody();
        Assert.assertEquals("Bidding price cannot be less than job price", responseBody);
    }
}
