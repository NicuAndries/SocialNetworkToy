package com.example.socialnetwork.dto;


import com.example.socialnetwork.domain.User;

import java.time.LocalDate;

public class FriendshipDTO {
    User user1, user2;
    LocalDate date;

    public FriendshipDTO(User user1, User user2, LocalDate date) {
        this.user1 = user1;
        this.user2 = user2;
        this.date = date;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return
                "First user: " + user1 +
                ", Second user: " + user2 +
                ", date: " + date;
    }
}
