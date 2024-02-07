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
@RequestMapping("api/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody User user){
        userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(userService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id){
        userService.delete(id);
        return ResponseEntity.ok("Suppression de l'utilisateur.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody User user,@PathVariable("id") Integer id){
        if(!id.equals(user.getId()))
            return ResponseEntity.badRequest().build();
        userService.update(user);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/disable/{id}")
    public ResponseEntity<?> disableUser(@PathVariable("id") Integer id){
        if(userService.findById(id).isEmpty())
            return ResponseEntity.badRequest().build();
        userService.toggleIsActive(id);
        return ResponseEntity.ok().build();
    }

}

