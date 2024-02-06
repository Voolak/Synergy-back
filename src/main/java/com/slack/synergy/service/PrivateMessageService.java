package com.slack.synergy.service;

import com.slack.synergy.model.PrivateMessage;
import com.slack.synergy.model.User;
import com.slack.synergy.model.dao.PrivateMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

}
