package com.slack.synergy.service;

import com.slack.synergy.model.Channel;
import com.slack.synergy.model.User;
import com.slack.synergy.model.dao.ChannelRepository;
import com.slack.synergy.model.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChannelService {

    @Autowired
    ChannelRepository channelRepository;

    public void save(Channel channel){
        List<Channel> channels = channelRepository.findAll();
        if(!channels.isEmpty())
            channel.setDefault(false);
        channelRepository.save(channel);
    }

    public List<Channel> findAll(){
        return channelRepository.findAll();
    }

    //a verifier
    public Optional<Channel> findById(Integer id){
        return channelRepository.findById(id);
    }

    public boolean delete(Channel channel){
        if(channel.isDefault())
            return false;
        channelRepository.deleteById(channel.getId());
        return true;
    }

    public boolean updateName(String name,Channel channel){
        if(channel.isDefault())
            return false;
        channel.setName(name);
        channelRepository.save(channel);
        return true;
    }
}
