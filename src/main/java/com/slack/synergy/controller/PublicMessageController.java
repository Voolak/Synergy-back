package com.slack.synergy.controller;

import com.slack.synergy.model.Channel;
import com.slack.synergy.model.PrivateMessage;
import com.slack.synergy.model.PublicMessage;
import com.slack.synergy.model.User;
import com.slack.synergy.service.ChannelService;
import com.slack.synergy.service.PublicMessageService;
import com.slack.synergy.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RequestMapping(("api/publicmessages"))
@RestController
public class PublicMessageController {
    @Autowired
    private PublicMessageService publicMessageService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private UserService userService;

    @PostMapping
    @Operation(summary = "Enregistre un message public")
    public ResponseEntity<?> savePublicMessage(@RequestBody PublicMessage message) {
        if(message.getContent() == null || message.getContent().isBlank())
            return ResponseEntity.badRequest().body(Map.of("message", "Le contenu du message ne peut pas être vide"));
        Optional<Channel> channelOptional = channelService.findById(message.getChannel().getId());
        if(message.getChannel() == null || channelOptional.isEmpty())
            return ResponseEntity.badRequest().body(Map.of("message", "Le channel associé n'est pas valide"));
        Optional<User> userOptional = userService.findById(message.getSender().getId());
        if(message.getSender() == null || userOptional.isEmpty())
            return ResponseEntity.badRequest().body(Map.of("message", "L'utilisateur associé n'est pas valide"));
        if(!publicMessageService.save(message))
            return ResponseEntity.badRequest().body(Map.of("message", "Action impossible. Le compte est désactivé."));
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Creation du message."));
    }


    @DeleteMapping("/{idMessage}/{idUser}")
    @Operation(summary = "Supprime un message public d'un utilisateur")
    public ResponseEntity<?> deletePublicMessageById(@PathVariable("idMessage") Integer idMessage,@PathVariable("idUser") Integer idUser){
        if(userService.findById(idUser).isEmpty())
            return ResponseEntity.badRequest().body(Map.of("message", "L'id de l'utilisateur n'est pas reconnu."));
        if(publicMessageService.findById(idMessage).isEmpty())
            return ResponseEntity.badRequest().body(Map.of("message", "L'id du message n'est pas reconnu."));
        PublicMessage publicMessage = publicMessageService.findById(idMessage).get();
        User user = userService.findById(idUser).get();
        if(!publicMessage.getSender().getId().equals(user.getId()))
            return ResponseEntity.badRequest().body(Map.of("message", "Vous n'avez pas la permission de supprimer ce message."));
        if(!publicMessageService.delete(publicMessage))
            return ResponseEntity.badRequest().body(Map.of("message", "Action impossible. Le compte est désactivé."));
        return ResponseEntity.ok(Map.of("message", "Message supprimé."));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Modifie le contenu d'un message")
    public ResponseEntity<?> updateContentOfMessage(@RequestBody PublicMessage messageFromBody, @PathVariable("id") Integer id) {
        if (!id.equals(messageFromBody.getId()))
            return ResponseEntity.badRequest().body(Map.of("message", "Les ids de la requête et celui passé en paramètre ne sont pas identiques"));
        Optional<PublicMessage> optional = publicMessageService.findById(id);
        if (optional.isEmpty())
            return ResponseEntity.badRequest().body(Map.of("message", "L'id du message n'est pas reconnu"));
        if (messageFromBody.getContent().isBlank())
            return ResponseEntity.badRequest().body(Map.of("message", "Le contenu du message ne peut pas être vide"));
        if(!publicMessageService.updateContent(messageFromBody.getContent(), optional.get()))
            return ResponseEntity.badRequest().body(Map.of("message", "Action impossible. Le compte est désactivé."));
        return ResponseEntity.ok(Map.of("message", "Contenu modifié"));
    }

    @PatchMapping("/upvote/{idMessage}/{idUser}")
    @Operation(summary = "Upvote un message public d'un utilisateur")
    public ResponseEntity<?> upvoteMessage(@PathVariable("idMessage") Integer idMessage,@PathVariable("idUser") Integer idUser){
        if(userService.findById(idUser).isEmpty())
            return ResponseEntity.badRequest().body(Map.of("message", "L'id de l'utilisateur n'est pas reconnu."));
        if(publicMessageService.findById(idMessage).isEmpty())
            return ResponseEntity.badRequest().body(Map.of("message", "L'id du message n'est pas reconnu."));
        PublicMessage publicMessage = publicMessageService.findById(idMessage).get();
        User user = userService.findById(idUser).get();
        if(!publicMessageService.toggleUpvote(user,publicMessage))
            return ResponseEntity.badRequest().body(Map.of("message", "Action impossible. Le compte est désactivé."));
        return ResponseEntity.ok(Map.of("message", "Action effectué."));
    }

    @PatchMapping("/downvote/{idMessage}/{idUser}")
    @Operation(summary = "Downvote un message public d'un utilisateur")
    public ResponseEntity<?> downvoteMessage(@PathVariable("idMessage") Integer idMessage,@PathVariable("idUser") Integer idUser){
        if(userService.findById(idUser).isEmpty())
            return ResponseEntity.badRequest().body(Map.of("message", "L'id de l'utilisateur n'est pas reconnu."));
        if(publicMessageService.findById(idMessage).isEmpty())
            return ResponseEntity.badRequest().body(Map.of("message", "L'id du message n'est pas reconnu."));
        PublicMessage publicMessage = publicMessageService.findById(idMessage).get();
        User user = userService.findById(idUser).get();
        if(!publicMessageService.toggleDownvote(user,publicMessage))
            return ResponseEntity.badRequest().body(Map.of("message", "Action impossible. Le compte est désactivé."));
        return ResponseEntity.ok(Map.of("message", "Action effectué."));
    }

}
