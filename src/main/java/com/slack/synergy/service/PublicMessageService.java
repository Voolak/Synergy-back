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

    public Optional<PublicMessage> save(PublicMessage publicMessage){
        if(publicMessage.getSender().isActive()){
            return Optional.of(publicMessageRepository.save(publicMessage));

        }
        return Optional.empty();
    }

    public Optional<PublicMessage> findById(Integer id){
        return publicMessageRepository.findById(id);
    }

    public boolean delete(PublicMessage publicMessage){
        if(publicMessage.getSender().isActive()){
            publicMessageRepository.delete(publicMessage);
            return true;
        }
        return false;
    }

    public boolean updateContent(String content, PublicMessage fromBdd){
        if(fromBdd.getSender().isActive()){
            fromBdd.setUpdateDate(LocalDateTime.now());
            fromBdd.setContent(content);
            publicMessageRepository.save(fromBdd);
            return true;
        }
        return false;
    }

    public boolean toggleUpvote(User user,PublicMessage publicMessage){
        if(user.isActive()){
            if(publicMessage.getUpvoters().contains(user)){
                publicMessage.getUpvoters().remove(user);
            }else{
                publicMessage.addUpvoter(user);
            }
            return true;
        }
        return false;
    }

    public boolean toggleDownvote(User user, PublicMessage publicMessage){
        if(user.isActive()){
            if(publicMessage.getDownvoters().contains(user)){
                publicMessage.getDownvoters().remove(user);
            }else{
                publicMessage.addDownvoter(user);
            }
            return true;
        }
        return false;
    }

    public List<PublicMessage> findAllPublicMessageFromChannelId(Integer idChannel){
        return publicMessageRepository.findAllByChannel_id(idChannel);
    }


}
