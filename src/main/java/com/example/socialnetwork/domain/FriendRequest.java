package com.example.socialnetwork.domain;

import com.example.socialnetwork.utils.Pair;

import java.time.LocalDate;

public class FriendRequest extends Entity<Pair<Long, Long>> {
    Long userSenderId;
    Long userReceiverId;
    String status;
    LocalDate date;

    public FriendRequest(Long userSenderId, Long userReceiverId) {
        this.userSenderId = userSenderId;
        this.userReceiverId = userReceiverId;
        this.status = "pending";
        this.date = LocalDate.now();
    }

    public FriendRequest(Long userSenderId, Long userReceiverId, String status, LocalDate date) {
        this.userSenderId = userSenderId;
        this.userReceiverId = userReceiverId;
        this.status = status;
        this.date = date;
    }

    public FriendRequest(Long userSenderId, Long userReceiverId, String status) {
        this.userSenderId = userSenderId;
        this.userReceiverId = userReceiverId;
        this.status = status;
    }

    public Long getIdSendingUser() {
        return userSenderId;
    }

    public Long getIdReceivingUser() {
        return userReceiverId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Id sender: " + userSenderId +
                " Id receiver: " + userReceiverId +
                " status=" + status;
    }
}
