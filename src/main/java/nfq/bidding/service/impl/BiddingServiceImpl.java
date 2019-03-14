package nfq.bidding.service.impl;

import nfq.bidding.constant.JobStatus;
import nfq.bidding.entity.Bidding;
import nfq.bidding.entity.Job;
import nfq.bidding.entity.User;
import nfq.bidding.exception.CredentialException;
import nfq.bidding.exception.DataException;
import nfq.bidding.exception.ExecutionException;
import nfq.bidding.repository.BiddingRepository;
import nfq.bidding.repository.JobRepository;
import nfq.bidding.service.BiddingService;
import nfq.bidding.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;

import static nfq.bidding.constant.JobStatus.IN_BIDDING;
import static nfq.bidding.constant.JobStatus.IN_PROGRESS;


@Service
public class BiddingServiceImpl implements BiddingService {

    private static Logger logger = LoggerFactory.getLogger(BiddingServiceImpl.class);

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    BiddingRepository biddingRepository;

    @Autowired
    UserService userService;

    @Override
    @Transactional
    public Bidding bidOneJob(long jobId, Bidding bidding) throws ExecutionException, CredentialException, DataException {
        User user = userService.getCurrentUser();
        bidding.setUser(user);

        Job job = jobRepository.findById(jobId).get();
        checkForBiddableJob(job);
        bidding.setBiddingJob(job);

        checkForBiddingPrice(bidding, job.getPrice());
        bidding = biddingRepository.save(bidding);

        //If it is the fist time job is bid, just change status to IN_BIDDING
        if (!job.getStatus().equals(JobStatus.IN_BIDDING)) {
            job.setStatus(JobStatus.IN_BIDDING);
            jobRepository.save(job);
        }

        return bidding;
    }

    private void checkForBiddingPrice(Bidding bidding, BigDecimal price) throws DataException {
        String errorMessage;

        if (ObjectUtils.isEmpty(bidding)) {
            errorMessage = "Bidding information cannot be null or empty";
            throw new DataException(errorMessage);
        } else if (bidding.getBidPrice().compareTo(price) < 0) {
            errorMessage = "Bidding price cannot be less than job price";
            throw new DataException(errorMessage);
        }
    }

    private void checkForBiddableJob(Job job) throws ExecutionException {
        if (job == null
                || (!job.getStatus().equals(IN_PROGRESS) && !job.getStatus().equals(IN_BIDDING))) {

            String jobId = job != null ? Long.toString(job.getId()) : "";
            throw new ExecutionException("Job " + jobId + " is not available or ready for bidding");
        }
    }
}
