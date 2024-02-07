package com.slack.synergy.service;

import com.slack.synergy.model.PrivateMessage;
import com.slack.synergy.model.User;
import com.slack.synergy.model.dao.PrivateMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PrivateMessageService {

    @Autowired
    PrivateMessageRepository privateMessageRepository;

    public void save(PrivateMessage privateMessage){
        privateMessageRepository.save(privateMessage);
    }

    public List<PrivateMessage> findAll(){
        return privateMessageRepository.findAll();
    }

    public Optional<PrivateMessage> findById(Integer id){
        return privateMessageRepository.findById(id);
    }

    public void delete(Integer id){
        privateMessageRepository.deleteById(id);
    }

    public void updateContent(String content, PrivateMessage fromBdd){
        fromBdd.setUpdateDate(LocalDateTime.now());
        fromBdd.setContent(content);
        privateMessageRepository.save(fromBdd);
    }

    public void  toggleUpvote(User user,PrivateMessage privateMessage){
        if(privateMessage.getUpvoters().contains(user)){
            privateMessage.getUpvoters().remove(user);
        }else{
            privateMessage.addUpvoter(user);
        }
    }

    public void toggleDownvote(User user, PrivateMessage privateMessage){
        if(privateMessage.getDownvoters().contains(user)){
            privateMessage.getDownvoters().remove(user);
        }else{
            privateMessage.addDownvoter(user);
        }
    }


    public List<PrivateMessage> findAllPrivateMessageFromUserId(Integer idUser){
        return privateMessageRepository.findBySender_IdOrRecipient_Id(idUser);
    }

    public Set<User> findAllUniqueUsersFromPrivateMessageList(Integer idUser){
        List<PrivateMessage> list=findAllPrivateMessageFromUserId(idUser);
        Set<User> userSet= new HashSet<>();
        for (PrivateMessage p: list){
            userSet.add(p.getRecipient());
            userSet.add(p.getSender());
        }
        return userSet;
    }

    public List<PrivateMessage> findAllPrivateMessageFromTwoUserId(Integer id1,Integer id2){
        List<PrivateMessage> listMessages = privateMessageRepository.findBySender_IdAndRecipient_Id(id1,id2);
        listMessages.addAll(privateMessageRepository.findByRecipient_IdAndSender_Id(id1,id2));
        return listMessages.stream().sorted(Comparator.comparing(PrivateMessage::getCreationDate).reversed()).toList();
    }

}
