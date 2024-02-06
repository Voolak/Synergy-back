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

    public void update(PrivateMessage privateMessage){
        Optional<PrivateMessage> bddPrivateMessage = privateMessageRepository.findById(privateMessage.getId());
        if (bddPrivateMessage.isPresent()){
            if (!privateMessage.getContent().isBlank()){
                bddPrivateMessage.get().setUpdateDate(LocalDateTime.now());
                bddPrivateMessage.get().setContent(privateMessage.getContent());

                privateMessageRepository.save(bddPrivateMessage.get());
            }
        }
    }
}
