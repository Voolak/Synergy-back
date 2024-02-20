package com.slack.synergy.model.dao;

import com.slack.synergy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsernameAndEmail(String username, String email);
    Optional<User> findByEmail(String email);
}
