package nfq.bidding.service;

import nfq.bidding.entity.Job;
import nfq.bidding.exception.ExecutionException;
import nfq.bidding.exception.CredentialException;

import java.util.List;

public interface JobService {
    List<Job> getJobsByUserId(long userId) throws Exception;
    Job createNewJob(Job newJob) throws ExecutionException, CredentialException;
    Job updateJob(Job job) throws ExecutionException, CredentialException;
}
