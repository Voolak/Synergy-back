package com.slack.synergy.model.dao;

import com.slack.synergy.model.PrivateMessage;
import com.slack.synergy.model.PublicMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicMessageRepository extends JpaRepository<PublicMessage, Integer> {
    List<PublicMessage> findAllByChannel_id(Integer id);
}
