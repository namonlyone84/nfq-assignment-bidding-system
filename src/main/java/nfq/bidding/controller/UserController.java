package nfq.bidding.controller;

import nfq.bidding.entity.User;
import nfq.bidding.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping("api")
public class UserController {
    @Autowired
    UserService userService;

    /**
     * Get a list of all users
     */
    @RequestMapping(value = "/users", method = GET)
    public List<User> getAllUser(@PathVariable long userId) {
        return userService.getAllUser();
    }

    /**
     * Add new user
     * @param user
     * @return
     */
    @RequestMapping(value = "/users", method = PUT)
    public long addUser(@RequestBody User user) {
        User savedUser = userService.addNewUser(user);
        return savedUser.getId();
    }

    /**
     * Get user by UserID
     * @param userId
     * @return
     */
    @RequestMapping(value = "/users/{userId}", method = GET)
    public User getUserByUserId(@RequestBody long userId) {
        User user = userService.getUser(userId);
        return user;
    }
}
