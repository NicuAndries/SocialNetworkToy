package com.example.socialnetwork.domain;

import java.time.LocalDateTime;

public class Message extends Entity<Long>{
    Long userSenderId;
    Long chatId;
    String text;
    LocalDateTime time;
    Long reply;

    public Message(Long userSenderId, Long chatId, String text, LocalDateTime time) {
        this.userSenderId = userSenderId;
        this.chatId = chatId;
        this.text = text;
        this.time = time;
        this.reply = null;
    }

    public Message(Long userSenderId, Long chatId, String text) {
        this.userSenderId = userSenderId;
        this.chatId = chatId;
        this.text = text;
        this.time = LocalDateTime.now();
        this.reply = null;
    }

    public Long getUserSenderId() {
        return userSenderId;
    }

    public void setUserSenderId(Long userSenderId) {
        this.userSenderId = userSenderId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Long getReply() {
        return reply;
    }

    public void setReply(Long reply) {
        this.reply = reply;
    }

    @Override
    public String toString() {
        return "Message{" +
                "userSenderId=" + userSenderId +
                ", chatId=" + chatId +
                ", text='" + text + '\'' +
                ", time=" + time +
                ", reply=" + reply +
                '}';
    }
}