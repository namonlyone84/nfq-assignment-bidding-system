package nfq.bidding.service.impl;

import nfq.bidding.entity.Job;
import nfq.bidding.entity.User;
import nfq.bidding.exception.CredentialException;
import nfq.bidding.repository.JobRepository;
import nfq.bidding.repository.UserRepository;
import nfq.bidding.service.JobService;
import nfq.bidding.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Resource
    private UserRepository userRepository;

    @Override
    public User getUser(long userId) {
        User result = null;
        try {
            result = userRepository.findById(userId).get();
        } catch (Exception e) {
            logger.error("UserService find userId: " + userId + " failed", e);
        }

        return result;
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User addNewUser(User user) {
        return userRepository.save(user);
    }

    /*
        Fake method to get current user. Since this backend doesn't mind about authentication and authorization.
        This method is to get userID from request parameter.
     */
    @Override
    public User getCurrentUser() throws CredentialException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        String userId = request.getParameter("userId");

        if (userId == null || !userRepository.existsById(Long.valueOf(userId))) {
            throw new CredentialException("There is no user info in the current request");
        }

        return userRepository.findById(Long.valueOf(userId)).get();
    }
}
