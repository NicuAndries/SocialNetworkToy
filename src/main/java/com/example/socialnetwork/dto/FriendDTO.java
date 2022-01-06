package com.example.socialnetwork.dto;

import java.time.LocalDate;

public class FriendDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate date;

    public FriendDTO(Long id, String firstName, String lastName, LocalDate date) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "" + id +" | firstName: " + firstName +
                " | lastName: " + lastName +
                " | friends since: " + date;
    }
}
