package com.slack.synergy.controller;

import com.slack.synergy.model.PrivateMessage;
import com.slack.synergy.model.User;
import com.slack.synergy.service.PrivateMessageService;
import com.slack.synergy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/privatemessages")
public class PrivateMessageController {

    @Autowired
    PrivateMessageService privateMessageService;
    @Autowired
    UserService userService;

    @GetMapping("/{idUser}")
    public ResponseEntity<?> getAllConversation(@PathVariable("idUser") Integer idUser){
        if(userService.findById(idUser).isEmpty())
            return ResponseEntity.badRequest().body("L'id de l'utilisateur n'est pas reconnu.");
        return ResponseEntity.ok(privateMessageService.findAllUniqueUsersFromPrivateMessageList(idUser));
    }

    @GetMapping("/{idUser}/{idSecondUser}")
    public ResponseEntity<?> getAllMessageFromConversation(@PathVariable("idUser") Integer idUser,@PathVariable("idSecondUser") Integer idSecondUser){
        if(userService.findById(idUser).isEmpty())
            return ResponseEntity.badRequest().body("L'id de l'utilisateur n'est pas reconnu.");
        if(userService.findById(idSecondUser).isEmpty())
            return ResponseEntity.badRequest().body("L'id de l'utilisateur second n'est pas reconnu.");
        return ResponseEntity.ok(privateMessageService.findAllPrivateMessageFromTwoUserId(idUser,idSecondUser));
    }

    @PostMapping
    public ResponseEntity<?> savePrivateMessage(@RequestBody PrivateMessage message){
        privateMessageService.save(message);
        return ResponseEntity.status(HttpStatus.CREATED).body("Creation du message.");
    }

    @PostMapping("/upvote/{idMessage}/{idUser}")
    public ResponseEntity<?> upvoteMessage(@PathVariable("idMessage") Integer idMessage,@PathVariable("idUser") Integer idUser){
        if(userService.findById(idUser).isEmpty())
            return ResponseEntity.badRequest().body("L'id de l'utilisateur n'est pas reconnu.");
        if(privateMessageService.findById(idMessage).isEmpty())
            return ResponseEntity.badRequest().body("L'id du message n'est pas reconnu.");
        PrivateMessage privateMessage = privateMessageService.findById(idMessage).get();
        User user = userService.findById(idUser).get();
        if(!privateMessage.getSender().getId().equals(user.getId()) && !privateMessage.getRecipient().getId().equals(user.getId()))
            return ResponseEntity.badRequest().body("Vous n'avez pas la permission de réagir à ce message.");
        privateMessageService.toggleUpvote(user,privateMessage);
        return ResponseEntity.ok("Action effectué.");
    }


    @PostMapping("/downvote/{idMessage}/{idUser}")
    public ResponseEntity<?> downvoteMessage(@PathVariable("idMessage") Integer idMessage,@PathVariable("idUser") Integer idUser){
        if(userService.findById(idUser).isEmpty())
            return ResponseEntity.badRequest().body("L'id de l'utilisateur n'est pas reconnu.");
        if(privateMessageService.findById(idMessage).isEmpty())
            return ResponseEntity.badRequest().body("L'id du message n'est pas reconnu.");
        PrivateMessage privateMessage = privateMessageService.findById(idMessage).get();
        User user = userService.findById(idUser).get();
        if(!privateMessage.getSender().getId().equals(user.getId()) && !privateMessage.getRecipient().getId().equals(user.getId()))
            return ResponseEntity.badRequest().body("Vous n'avez pas la permission de réagir à ce message.");
        privateMessageService.toggleDownvote(user,privateMessage);
        return ResponseEntity.ok("Action effectuée.");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateContentOfMessage(@RequestBody PrivateMessage fromBody, @PathVariable("id") Integer id) {
        if (!id.equals(fromBody.getId())) {
            return ResponseEntity.badRequest().build();
        }
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

    @DeleteMapping("/{idMessage}/{idUser}")
    public ResponseEntity<?> deleteMessage(@PathVariable("idMessage") Integer idMessage,@PathVariable("idUser") Integer idUser){
        if(userService.findById(idUser).isEmpty())
            return ResponseEntity.badRequest().body("L'id de l'utilisateur n'est pas reconnu.");
        if(privateMessageService.findById(idMessage).isEmpty())
            return ResponseEntity.badRequest().body("L'id du message n'est pas reconnu.");
        PrivateMessage privateMessage = privateMessageService.findById(idMessage).get();
        User user = userService.findById(idUser).get();
        if(!privateMessage.getSender().getId().equals(user.getId()) && !privateMessage.getRecipient().getId().equals(user.getId()))
            return ResponseEntity.badRequest().body("Vous n'avez pas la permission de supprimer ce message.");
        privateMessageService.delete(idMessage);
        return ResponseEntity.ok("Message supprimé.");
    }


}

