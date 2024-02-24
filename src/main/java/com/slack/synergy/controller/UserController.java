package com.slack.synergy.controller;

import com.slack.synergy.model.PrivateMessage;
import com.slack.synergy.model.User;
import com.slack.synergy.service.PrivateMessageService;
import com.slack.synergy.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    @Operation(summary = "Ajoute un utilisateur")
    public ResponseEntity<?> addUser(@RequestBody User user){
        if(!userService.save(user))
            return ResponseEntity.badRequest().body(Map.of("message", "Un utilisateur avec cette addresse mail existe déjà !"));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    @Operation(summary = "Récupère tous les utilisateurs")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(userService.findAll());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprime un utilisateur")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id){
        userService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Suppression de l'utilisateur."));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifie un utilisateur")
    public ResponseEntity<?> updateUser(@RequestBody User user,@PathVariable("id") Integer id){
        if(!id.equals(user.getId()))
            return ResponseEntity.badRequest().body(Map.of("message", "Id non identique."));
        userService.update(user);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/disable/{id}")
    @Operation(summary = "Désactive un utilisateur")
    public ResponseEntity<?> disableUser(@PathVariable("id") Integer id){
        if(userService.findById(id).isEmpty())
            return ResponseEntity.badRequest().body(Map.of("message", "Utilisateur non existant."));
        userService.toggleIsActive(id);
        return ResponseEntity.ok().build();
    }

}

