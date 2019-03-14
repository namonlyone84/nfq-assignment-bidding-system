package nfq.bidding.controller;

import nfq.bidding.entity.Bidding;
import nfq.bidding.exception.DataException;
import nfq.bidding.exception.ExecutionException;
import nfq.bidding.exception.CredentialException;
import nfq.bidding.service.BiddingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("api")
public class BiddingController {
    @Autowired
    BiddingService biddingService;

    /**
     * Bid for a job
     *
     * @return
     */
    @RequestMapping(value = "/jobs/{jobId}/bidding", params = {"userId"}, method = POST)
    public long bidOneJob(@PathVariable long jobId, @RequestBody Bidding bidding)
            throws ExecutionException, CredentialException, DataException {

        Bidding savedBidding = biddingService.bidOneJob(jobId, bidding);
        return savedBidding.getId();
    }
}
