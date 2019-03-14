package nfq.bidding.service.impl;

import nfq.bidding.constant.JobStatus;
import nfq.bidding.entity.Job;
import nfq.bidding.entity.User;
import nfq.bidding.exception.ExecutionException;
import nfq.bidding.exception.CredentialException;
import nfq.bidding.exception.DataException;
import nfq.bidding.repository.JobRepository;
import nfq.bidding.repository.UserRepository;
import nfq.bidding.service.JobService;
import nfq.bidding.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class JobServiceImpl implements JobService {

    private static Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);
    @Resource
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Override
    public List<Job> getJobsByUserId(long userId) throws Exception {
        logger.info("JobService find all job for userId: " + userId);

        if (!userRepository.existsById(userId)) {
            throw new DataException("User not found userID: " + userId);
        }

        List<Job> result = null;
        try {
            result = jobRepository.findAllByUserId(userId);
        } catch (Exception e) {
            throw new Exception("JobService find all jobs for user error:", e);
        }

        return result;
    }

    @Override
    public Job createNewJob(Job job) throws ExecutionException, CredentialException {
        User creator = userService.getCurrentUser();

        if (job.getId() != null && jobRepository.existsById(job.getId())) {
            throw new ExecutionException("Job already existed with id " + job.getId());
        }

        job.setUser(creator);
        return jobRepository.save(job);
    }

    @Override
    public Job updateJob(Job job) throws ExecutionException, CredentialException {
        //For checking current user, if not throw exception
        userService.getCurrentUser();

        if (jobRepository.existsById(job.getId())) {
            checkUpdatePrice(job);
        }

        return jobRepository.save(job);
    }

    private void checkUpdatePrice(Job job) throws ExecutionException {
        Job oldJob = jobRepository.findById(job.getId()).get();

        if (oldJob.getStatus().equals(JobStatus.IN_BIDDING)
                && oldJob.getPrice().compareTo(job.getPrice()) != 0) {
            throw new ExecutionException("The job " + job.getId() + " has been in bidding by candidates, price is not allowed to modify");
        }
    }
}
