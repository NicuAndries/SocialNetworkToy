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

    @Override
    public String toString() {
        return "" + id +" | firstName: " + firstName +
                " | lastName: " + lastName +
                " | friends since: " + date;
    }
}
