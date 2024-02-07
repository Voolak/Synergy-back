package com.slack.synergy.service;

import com.slack.synergy.model.Channel;
import com.slack.synergy.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InitService {
    @Autowired
    private ChannelService channelService;
    @Autowired
    private UserService userService;

    public void createDefaultChannelIfNotExists() {
        if(channelService.findAll().isEmpty()) {
            Channel defaultChannel = new Channel("Général");
            defaultChannel.setDefault(true);
            channelService.save(defaultChannel);
        }
    }

    public void createInitialUserIfNotExists() {
        if(userService.findAll().isEmpty()) {
            User user = new User("johndoe","John", "Doe", "john@doe.fr");
            userService.save(user);
        }
    }
}
