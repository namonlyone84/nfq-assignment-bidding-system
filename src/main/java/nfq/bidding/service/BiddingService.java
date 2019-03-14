package nfq.bidding.service;

import nfq.bidding.entity.Bidding;
import nfq.bidding.exception.DataException;
import nfq.bidding.exception.ExecutionException;
import nfq.bidding.exception.CredentialException;

public interface BiddingService {
    Bidding bidOneJob(long jobId, Bidding bidding) throws ExecutionException, CredentialException, DataException;
}
