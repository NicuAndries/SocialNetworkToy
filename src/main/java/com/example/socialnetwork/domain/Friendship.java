package com.example.socialnetwork.domain;

import com.example.socialnetwork.utils.Pair;

import java.time.LocalDate;

public class Friendship extends Entity<Pair<Long, Long>>{
    User firstUser;
    User secondUser;
    private LocalDate date;

    public Friendship(User firstUser, User secondUser, LocalDate date) {
        this.firstUser = firstUser;
        this.secondUser = secondUser;
        this.date = date;
    }

    public Friendship(User firstUser, User secondUser) {
        this.firstUser = firstUser;
        this.secondUser = secondUser;
        this.date = LocalDate.now();
    }

    public LocalDate getDate() {
        return date;
    }

    public User getFirstUser() {
        return firstUser;
    }

    public User getSecondUser() {
        return secondUser;
    }
}
