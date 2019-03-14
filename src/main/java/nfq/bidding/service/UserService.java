package nfq.bidding.service;

import nfq.bidding.entity.Job;
import nfq.bidding.entity.User;
import nfq.bidding.exception.CredentialException;

import java.util.List;

public interface UserService {
    User getUser(long userId);
    User getCurrentUser() throws CredentialException;
    List<User> getAllUser();
    User addNewUser(User user);
}
