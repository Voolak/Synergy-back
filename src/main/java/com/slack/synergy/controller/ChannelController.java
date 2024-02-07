package com.slack.synergy.controller;

import com.slack.synergy.model.Channel;
import com.slack.synergy.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/channels")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @PostMapping
    public ResponseEntity<?> addChannel(@RequestBody Channel channel){
        if (channel.getName()== null ||channel.getName().isBlank())
            return ResponseEntity.badRequest().body("Le nom du canal est obligatoire");
        channelService.save(channel);
        return ResponseEntity.status(HttpStatus.CREATED).body("Canal crée avec succés");
    }
    @GetMapping
    public ResponseEntity<List<Channel>> getAllChannels(){
        List<Channel> channels = channelService.findAll();
        return ResponseEntity.ok(channels);
    }

    @DeleteMapping("/{idChannel}")
    public ResponseEntity<?> deleteChannel(@PathVariable("idChannel") Integer idChannel){
        Optional<Channel> channelOptional = channelService.findById(idChannel);
        if(channelOptional.isEmpty())
            return ResponseEntity.badRequest().body("Ce canal n'existe pas");
        if(!channelService.delete(channelOptional.get()))
            return ResponseEntity.badRequest().body("Le canal par défaut ne pas être supprimé");
        return ResponseEntity.ok("Le canal a été supprimé");
    }

    @PatchMapping("/{idChannel}")
    public ResponseEntity<?> updateNameChannel(@PathVariable("idChannel") Integer idChannel,@RequestBody Channel channel){
        Optional<Channel> channelOptional = channelService.findById(idChannel);
        if(channelOptional.isEmpty())
            return ResponseEntity.badRequest().body("Ce canal n'existe pas");
        if (channel.getName()== null ||channel.getName().isBlank())
            return ResponseEntity.badRequest().body("Le nom du canal est obligatoire");
        if(!channelService.updateName( channel.getName(),channelOptional.get()))
            return ResponseEntity.badRequest().body("Le canal par défaut ne pas être mis à jour");
        return ResponseEntity.ok("Le canal a été mis à jour");
    }


}
