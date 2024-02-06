package com.slack.synergy.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("public")
public class PublicMessage extends Message {
    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    public PublicMessage() {
    }

    public PublicMessage(String content, User sender, Channel channel) {
        super(content, sender);
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}