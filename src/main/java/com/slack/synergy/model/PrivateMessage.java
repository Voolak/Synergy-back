package com.slack.synergy.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("private")
public class PrivateMessage extends Message {
    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;

    public PrivateMessage() {
    }

    public PrivateMessage(String content, User sender, User recipient) {
        super(content, sender);
        this.recipient = recipient;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }
}