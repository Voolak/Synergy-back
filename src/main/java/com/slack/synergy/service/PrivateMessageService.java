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

    public PrivateMessageStatus save(PrivateMessage privateMessage){
        if(!privateMessage.getSender().isActive())
            return PrivateMessageStatus.ERROR_SENDER_INACTIVE;
        if(!privateMessage.getRecipient().isActive())
            return PrivateMessageStatus.ERROR_RECIPIENT_INACTIVE;
        privateMessageRepository.save(privateMessage);
        return PrivateMessageStatus.OK;

    }

    public Optional<PrivateMessage> findById(Integer id){
        return privateMessageRepository.findById(id);
    }


    public boolean delete(PrivateMessage privateMessage){
        if(privateMessage.getSender().isActive()) {
            privateMessageRepository.delete(privateMessage);
            return true;
        }
        return false;
    }

    public boolean updateContent(String content, PrivateMessage fromBdd){
        if(fromBdd.getSender().isActive()){
            fromBdd.setUpdateDate(LocalDateTime.now());
            fromBdd.setContent(content);
            privateMessageRepository.save(fromBdd);
            return true;
        }
        return false;
    }

    public boolean toggleUpvote(User user,PrivateMessage privateMessage){
        if(user.isActive()){
            if(privateMessage.getUpvoters().contains(user)){
                privateMessage.getUpvoters().remove(user);
            }else{
                privateMessage.addUpvoter(user);
            }
            return true;
        }
        return false;
    }

    public boolean toggleDownvote(User user, PrivateMessage privateMessage){
        if(user.isActive()){
            if(privateMessage.getDownvoters().contains(user)){
                privateMessage.getDownvoters().remove(user);
            }else{
                privateMessage.addDownvoter(user);
            }
            return true;
        }
        return false;
    }


    public List<PrivateMessage> findAllPrivateMessageFromUserId(Integer idUser){
        return privateMessageRepository.findBySender_IdOrRecipient_Id(idUser,idUser);
    }

    public List<User> findAllUniqueUsersFromPrivateMessageList(Integer idUser){
        List<PrivateMessage> list=findAllPrivateMessageFromUserId(idUser);
        Set<User> userSet= new HashSet<>();
        for (PrivateMessage p: list){
            userSet.add(p.getRecipient());
            userSet.add(p.getSender());
        }
        return userSet.stream().toList();
    }

    public List<PrivateMessage> findAllPrivateMessageFromTwoUserId(Integer id1,Integer id2){
        List<PrivateMessage> listMessages = privateMessageRepository.findBySender_IdAndRecipient_Id(id1,id2);
        listMessages.addAll(privateMessageRepository.findByRecipient_IdAndSender_Id(id1,id2));
        return listMessages.stream().sorted(Comparator.comparing(PrivateMessage::getCreationDate)).toList();
    }

}
