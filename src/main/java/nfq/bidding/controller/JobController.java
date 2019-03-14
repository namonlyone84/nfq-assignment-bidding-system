package nfq.bidding.controller;

import nfq.bidding.entity.Job;
import nfq.bidding.exception.CredentialException;
import nfq.bidding.exception.ExecutionException;
import nfq.bidding.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("api")
public class JobController {
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    JobService jobService;

    /**
     * Get a list of jobs from a user
     *
     * @return
     */
    @RequestMapping(value = "/users/{userId}/jobs", method = GET)
    public List<Job> getAllJobs(@PathVariable long userId) throws Exception {
        return jobService.getJobsByUserId(userId);
    }

    @RequestMapping(value = "/jobs", method = POST, params = {"userId"})
    public long createNewJob(@RequestBody Job newJob) throws ExecutionException, CredentialException {
        Job savedJob = jobService.createNewJob(newJob);
        return savedJob.getId();
    }

    @RequestMapping(value = "/jobs/{jobId}", method = POST, params = {"userId"})
    public long updateJob(@PathVariable Long jobId, @RequestBody Job job) throws ExecutionException, CredentialException {
        job.setId(jobId);
        Job savedJob = jobService.updateJob(job);
        return savedJob.getId();
    }

}
