package com.slack.synergy.model.dao;

import com.slack.synergy.model.PublicMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicMessageRepository extends JpaRepository<PublicMessage, Integer> {
}
