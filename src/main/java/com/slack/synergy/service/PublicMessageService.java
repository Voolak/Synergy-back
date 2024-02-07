package com.slack.synergy.service;

import com.slack.synergy.model.PrivateMessage;
import com.slack.synergy.model.PublicMessage;
import com.slack.synergy.model.User;
import com.slack.synergy.model.dao.PublicMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PublicMessageService {

    @Autowired
    PublicMessageRepository publicMessageRepository;

    public void save(PublicMessage publicMessage){
        publicMessageRepository.save(publicMessage);
    }

    public List<PublicMessage> findAll(){
        return publicMessageRepository.findAll();
    }

    public Optional<PublicMessage> findById(Integer id){
        return publicMessageRepository.findById(id);
    }

    public void delete(Integer id){
        publicMessageRepository.deleteById(id);
    }

    public void updateContent(String content, PublicMessage fromBdd){
        fromBdd.setUpdateDate(LocalDateTime.now());
        fromBdd.setContent(content);
        publicMessageRepository.save(fromBdd);
    }

    public void toggleUpvote(User user,PublicMessage publicMessage){
        if(publicMessage.getUpvoters().contains(user)){
            publicMessage.getUpvoters().remove(user);
        }else{
            publicMessage.addUpvoter(user);
        }
    }

    public void toggleDownvote(User user, PublicMessage publicMessage){
        if(publicMessage.getDownvoters().contains(user)){
            publicMessage.getDownvoters().remove(user);
        }else{
            publicMessage.addDownvoter(user);
        }
    }

    public List<PublicMessage> findAllPublicMessageFromChannelId(Integer idChannel){
        return publicMessageRepository.findAllByChannel_id(idChannel);
    }


}
