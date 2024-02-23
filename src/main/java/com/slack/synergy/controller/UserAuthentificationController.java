package com.slack.synergy.controller;

import com.slack.synergy.model.User;
import com.slack.synergy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/login")
public class UserAuthentificationController {
    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<?> authentificateUser(@RequestBody User user){
        Optional<User> optionalUser = userService.findByUsernameAndEmail(user.getUsername(), user.getEmail());
        if(optionalUser.isEmpty())
            return ResponseEntity.badRequest().body(Map.of("message", "Utilisateur inexistant!"));
        return ResponseEntity.ok(optionalUser.get());
    }
}
