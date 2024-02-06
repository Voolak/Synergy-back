package com.slack.synergy.controller;

import com.slack.synergy.model.PrivateMessage;
import com.slack.synergy.model.dao.PrivateMessageRepository;
import com.slack.synergy.service.PrivateMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class PrivateMessageController {

    @Autowired
    PrivateMessageService privateMessageService;


    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody PrivateMessage fromBody, @PathVariable("id") Integer id) {
        if (id.equals(fromBody.getId())) {
            Optional<PrivateMessage> optional = privateMessageService.findById(id);
            if (optional.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (fromBody.getContent().isBlank()) {
                return ResponseEntity.badRequest().build();
            }
            privateMessageService.updateContent(fromBody.getContent(), optional.get());
            return ResponseEntity.ok("Contenu modifié");
        }
        return ResponseEntity.badRequest().build();
    }
}

