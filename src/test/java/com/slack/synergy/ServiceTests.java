package com.slack.synergy;

import com.slack.synergy.model.Channel;
import com.slack.synergy.model.PrivateMessage;
import com.slack.synergy.model.User;
import com.slack.synergy.service.ChannelService;
import com.slack.synergy.service.PrivateMessageService;
import com.slack.synergy.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ServiceTests {
    @Autowired
    private PrivateMessageService privateMessageService;
    @Autowired
    private UserService userService;
    @Autowired
    private ChannelService channelService;

    @Test
    void testCreateUsers() {
        User u1 = new User("john_doe", "John", "Doe", "jd@exemple.com");
        User u2 = new User("jane_smith", "Jane", "Smith", "js@exemple.com");
        User u3 = new User("tom_jones", "Tom", "Jones", "tj@exemple.com");

        userService.save(u1);
        userService.save(u2);
        userService.save(u3);
    }

    @Test
    void testCreateChannels() {
        Channel c1 = new Channel("General");
        channelService.save(c1);
    }

    @Test
    void testCreatePrivateMessages() {
        User u1 = new User("marysmith", "Mary", "Smith", "ms@exemple.com");
        User u2 = new User("Anne", "Anne", "Steel", "as@exemple.com");
        User u3 = new User("Jerry", "Jerry", "Mayer", "jm@exemple.com");

        userService.save(u1);
        userService.save(u2);
        userService.save(u3);

        PrivateMessage pm12 = new PrivateMessage("Hi !", u1, u2);
        PrivateMessage pm21 = new PrivateMessage("Hello, how are you ?", u2, u1);

        privateMessageService.save(pm12);
        privateMessageService.save(pm21);

        PrivateMessage pm13 = new PrivateMessage("Hello there !", u1, u3);
        PrivateMessage pm31 = new PrivateMessage("Hello, hello !", u3, u1);
        privateMessageService.save(pm13);
        privateMessageService.save(pm31);
    }

    @Test
    void testGetAllPrivateMessagesOfUser() {
        Optional<User> optional = userService.findById(2);
        if(optional.isPresent()) {
            User u1 = optional.get();

            List<PrivateMessage> privateMessagesOfUser =
                    privateMessageService.findAllPrivateMessageFromUserId(u1.getId());

            privateMessagesOfUser.forEach(System.out::println);

        }
    }

    @Test
    @Transactional
    void testFindAllUniqueUsersFromPrivateMessageList() {
        Optional<User> optional = userService.findById(2);
        if(optional.isPresent()) {
            User u1 = optional.get();

            List<User> usersContacted =
                    privateMessageService.findAllUniqueUsersFromPrivateMessageList(u1.getId());

            usersContacted.forEach(System.out::println);
        }
    }

    @Test
    @Transactional
    void testFindAllPrivateMessageFromTwoUserId() {
        Optional<User> optional1 = userService.findById(2);
        Optional<User> optional2 = userService.findById(3);

        if(optional1.isPresent() && optional2.isPresent()) {
            User u1 = optional1.get();
            User u2 = optional2.get();
            List<PrivateMessage> list =
                    privateMessageService.findAllPrivateMessageFromTwoUserId(u1.getId(), u2.getId());

            list.forEach(System.out::println);
        }
    }

    @Test
    @Transactional
    void testOrderOfPrivateMessages() {
        Optional<User> optional1 = userService.findById(1);
        Optional<User> optional2 = userService.findById(4);

        if(optional1.isPresent() && optional2.isPresent()) {
            User u2 = optional1.get();
            User u3 = optional2.get();

//            PrivateMessage pm12 = new PrivateMessage("Test 1 !", u2, u3);
//            privateMessageService.save(pm12);
//            PrivateMessage pm21 = new PrivateMessage("Test 1 response", u3, u2);
//            privateMessageService.save(pm21);

//            PrivateMessage pm13 = new PrivateMessage("Test 2", u2, u3);
//            privateMessageService.save(pm13);

//            PrivateMessage pm31 = new PrivateMessage("Test 2 response", u3, u2);
//            privateMessageService.save(pm31);

            List<PrivateMessage> list =
                    privateMessageService.findAllPrivateMessageFromTwoUserId(u2.getId(), u3.getId());

            list.forEach(System.out::println);
        }
    }

}
