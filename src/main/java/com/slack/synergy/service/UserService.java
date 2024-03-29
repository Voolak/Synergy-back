package com.slack.synergy.service;

import com.slack.synergy.model.PrivateMessage;
import com.slack.synergy.model.User;
import com.slack.synergy.model.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public boolean save(User user){
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());

        if(optionalUser.isEmpty()) {
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public Optional<User> findById(Integer id){
        return userRepository.findById(id);
    }

    public void delete(Integer id){
        userRepository.deleteById(id);
    }

    public void update(User user){
        userRepository.save(user);
    }

    public void toggleIsActive(Integer id){
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()){
            user.get().setActive(!user.get().isActive());
            userRepository.save(user.get());
        }
    }

    public Optional<User> findByUsernameAndEmail(String username, String email){
        return userRepository.findByUsernameAndEmail(username, email);
    }
}
