package com.slack.synergy.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "messages")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="message_type",
        discriminatorType = DiscriminatorType.STRING)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String content;
    @ManyToMany //(fetch = FetchType.EAGER)
    @JoinTable(
            name = "upvoters_messages",
            joinColumns = @JoinColumn(name = "message_id"),
            inverseJoinColumns = @JoinColumn(name = "upvoter_id"))
    private List<User> upvoters = new ArrayList<>();
    @ManyToMany  //(fetch = FetchType.EAGER)
    @JoinTable(
            name = "downvoters_messages",
            joinColumns = @JoinColumn(name = "message_id"),
            inverseJoinColumns = @JoinColumn(name = "downvoter_id"))
    private List<User> downvoters = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;
    @Column(name = "creation_date")
    private LocalDateTime creationDate = LocalDateTime.now();
    @Column(name = "update_date")
    private LocalDateTime updateDate;

    public Message() {
    }

    protected Message(String content, User sender) {
        this.content = content;
        this.sender = sender;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<User> getUpvoters() {
        return upvoters;
    }

    public List<User> getDownvoters() {
        return downvoters;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public void addUpvoter(User user){
        upvoters.add(user);
    }
    public void addDownvoter(User user){
        downvoters.add(user);
    }
    ///add remove method


    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", upvoters=" + upvoters.size() +
                ", downvoters=" + downvoters.size() +
                ", sender=" + sender +
                ", creationDate=" + creationDate +
                ", updateDate=" + updateDate +
                '}';
    }
}