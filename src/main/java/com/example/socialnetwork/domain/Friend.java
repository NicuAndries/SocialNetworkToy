package com.example.socialnetwork.domain;

import java.time.LocalDate;

public class Friend extends User {
    private User user;
    private LocalDate date;

    public Friend(User user, LocalDate date){
        this.user = user;
        this.date = date;
    }

    @Override
    public Long getId() {
        return user.getId();
    }

    @Override
    public String getFirstName() {
        return user.getFirstName();
    }

    @Override
    public String getLastName() {
        return user.getLastName();
    }

    @Override
    public String getGender() {
        return user.getGender();
    }

    @Override
    public LocalDate getBirthdate() {
        return user.getBirthdate();
    }

    @Override
    public String getProfilePicture() {
        return user.getProfilePicture();
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return user + " friends since " + date;
    }
}
