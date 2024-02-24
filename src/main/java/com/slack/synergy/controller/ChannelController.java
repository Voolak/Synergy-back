package com.slack.synergy.controller;

import com.slack.synergy.model.Channel;
import com.slack.synergy.model.PublicMessage;
import com.slack.synergy.service.ChannelService;
import com.slack.synergy.service.PublicMessageService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/channels")
public class ChannelController {

    @Autowired
    private ChannelService channelService;
    @Autowired
    private PublicMessageService publicMessageService;

    @PostMapping
    @Operation(summary = "Ajoute un Canal")
    public ResponseEntity<?> addChannel(@RequestBody Channel channel){
        if (channel.getName()== null ||channel.getName().isBlank())
            return ResponseEntity.badRequest().body(Map.of("message", "Le nom du canal est obligatoire"));
        channelService.save(channel);
        return ResponseEntity.status(HttpStatus.CREATED).body((Map.of("message","Canal crée avec succés")));
    }
    @GetMapping
    @Operation(summary = "Renvoie la liste des canaux")
    public ResponseEntity<List<Channel>> getAllChannels(){
        List<Channel> channels = channelService.findAll();
        return ResponseEntity.ok(channels);
    }

    @GetMapping("/{idChannel}")
    @Operation(summary = "Renvoie un Canal")
    public ResponseEntity<?> getChannelMessagesById(@PathVariable("idChannel") Integer idChannel) {
        Optional<Channel> channelOptional = channelService.findById(idChannel);
        if(channelOptional.isEmpty())
            return ResponseEntity.badRequest().body(Map.of("message", "Ce canal n'existe pas"));
        List<PublicMessage> channelMessages = publicMessageService.findAllPublicMessageFromChannelId(idChannel);
        return ResponseEntity.ok(channelMessages);
    }

    @DeleteMapping("/{idChannel}")
    @Operation(summary = "Supprime un Canal")
    public ResponseEntity<?> deleteChannel(@PathVariable("idChannel") Integer idChannel){
        Optional<Channel> channelOptional = channelService.findById(idChannel);
        if(channelOptional.isEmpty())
            return ResponseEntity.badRequest().body(Map.of("message", "Ce canal n'existe pas"));
        if(!channelService.delete(channelOptional.get()))
            return ResponseEntity.badRequest().body(Map.of("message", "Le canal par défaut ne pas être supprimé"));
        return ResponseEntity.ok(Map.of("message", "Le canal a été supprimé"));
    }

    @PatchMapping("/{idChannel}")
    @Operation(summary = "Met à jour (Patch) un Canal")
    public ResponseEntity<?> updateNameChannel(@PathVariable("idChannel") Integer idChannel,@RequestBody Channel channel){
        Optional<Channel> channelOptional = channelService.findById(idChannel);
        if(channelOptional.isEmpty())
            return ResponseEntity.badRequest().body(Map.of("message", "Ce canal n'existe pas"));
        if (channel.getName()== null ||channel.getName().isBlank())
            return ResponseEntity.badRequest().body(Map.of("message", "Le nom du canal est obligatoire"));
        if(!channelService.updateName( channel.getName(),channelOptional.get()))
            return ResponseEntity.badRequest().body(Map.of("message", "Le canal par défaut ne pas être mis à jour"));
        return ResponseEntity.ok(Map.of("message", "Le canal a été mis à jour"));
    }

}
