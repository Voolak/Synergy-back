package com.slack.synergy.controller;

import com.slack.synergy.model.Channel;
import com.slack.synergy.model.PrivateMessage;
import com.slack.synergy.model.PublicMessage;
import com.slack.synergy.model.User;
import com.slack.synergy.service.ChannelService;
import com.slack.synergy.service.PublicMessageService;
import com.slack.synergy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController("api/publicmessages")


public class PublicMessageController {
    @Autowired
    private PublicMessageService publicMessageService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> savePublicMessage(@RequestBody PublicMessage message) {
        if(message.getContent() == null || message.getContent().isBlank())
            return ResponseEntity.badRequest().body("Le contenu du message ne peut pas être vide");
        Optional<Channel> channelOptional = channelService.findById(message.getChannel().getId());
        if(message.getChannel() == null || channelOptional.isEmpty())
            return ResponseEntity.badRequest().body("Le channel associé n'est pas valide");
        Optional<User> userOptional = userService.findById(message.getSender().getId());
        if(message.getSender() == null || userOptional.isEmpty())
            return ResponseEntity.badRequest().body("L'utilisateur associé n'est pas valide");
        if(!publicMessageService.save(message))
            return ResponseEntity.badRequest().body("Action impossible. Le compte est désactivé.");
        return ResponseEntity.status(HttpStatus.CREATED).body("Creation du message.");
    }


    @DeleteMapping("/{idMessage}/{idUser}")
    public ResponseEntity<?> deletePublicMessageById(@PathVariable("idMessage") Integer idMessage,@PathVariable("idUser") Integer idUser){
        if(userService.findById(idUser).isEmpty())
            return ResponseEntity.badRequest().body("L'id de l'utilisateur n'est pas reconnu.");
        if(publicMessageService.findById(idMessage).isEmpty())
            return ResponseEntity.badRequest().body("L'id du message n'est pas reconnu.");
        PublicMessage publicMessage = publicMessageService.findById(idMessage).get();
        User user = userService.findById(idUser).get();
        if(!publicMessage.getSender().getId().equals(user.getId()))
            return ResponseEntity.badRequest().body("Vous n'avez pas la permission de supprimer ce message.");
        if(!publicMessageService.delete(publicMessage))
            return ResponseEntity.badRequest().body("Action impossible. Le compte est désactivé.");
        return ResponseEntity.ok("Message supprimé.");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateContentOfMessage(@RequestBody PublicMessage messageFromBody, @PathVariable("id") Integer id) {
        if (!id.equals(messageFromBody.getId()))
            return ResponseEntity.badRequest().body("Les ids de la requête et celui passé en paramètre ne sont pas identiques");
        Optional<PublicMessage> optional = publicMessageService.findById(id);
        if (optional.isEmpty())
            return ResponseEntity.badRequest().body("L'id du message n'est pas reconnu");
        if (messageFromBody.getContent().isBlank())
            return ResponseEntity.badRequest().body("Le contenu du message ne peut pas être vide");
        if(!publicMessageService.updateContent(messageFromBody.getContent(), optional.get()))
            return ResponseEntity.badRequest().body("Action impossible. Le compte est désactivé.");
        return ResponseEntity.ok("Contenu modifié");
    }

    @PatchMapping("/upvote/{idMessage}/{idUser}")
    public ResponseEntity<?> upvoteMessage(@PathVariable("idMessage") Integer idMessage,@PathVariable("idUser") Integer idUser){
        if(userService.findById(idUser).isEmpty())
            return ResponseEntity.badRequest().body("L'id de l'utilisateur n'est pas reconnu.");
        if(publicMessageService.findById(idMessage).isEmpty())
            return ResponseEntity.badRequest().body("L'id du message n'est pas reconnu.");
        PublicMessage publicMessage = publicMessageService.findById(idMessage).get();
        User user = userService.findById(idUser).get();
        if(!publicMessageService.toggleUpvote(user,publicMessage))
            return ResponseEntity.badRequest().body("Action impossible. Le compte est désactivé.");
        return ResponseEntity.ok("Action effectué.");
    }

    @PatchMapping("/downvote/{idMessage}/{idUser}")
    public ResponseEntity<?> downvoteMessage(@PathVariable("idMessage") Integer idMessage,@PathVariable("idUser") Integer idUser){
        if(userService.findById(idUser).isEmpty())
            return ResponseEntity.badRequest().body("L'id de l'utilisateur n'est pas reconnu.");
        if(publicMessageService.findById(idMessage).isEmpty())
            return ResponseEntity.badRequest().body("L'id du message n'est pas reconnu.");
        PublicMessage publicMessage = publicMessageService.findById(idMessage).get();
        User user = userService.findById(idUser).get();
        if(!publicMessageService.toggleDownvote(user,publicMessage))
            return ResponseEntity.badRequest().body("Action impossible. Le compte est désactivé.");
        return ResponseEntity.ok("Action effectué.");
    }

}
