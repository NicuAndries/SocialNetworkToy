package com.example.socialnetwork.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RequestDTO {
    Long idUser;
    private String firstName, lastName;
    private String status;
    private String image;
    private LocalDate date;

    public RequestDTO(String firstName, String lastName, String status, LocalDate date, String image) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.date = date;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
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

    public String getStatusString(){
        if(status.equals("approved"))
            return "accepted";
        if(status.equals("rejected"))
            return "declined";
        else
            return "pending";
    }

    @Override
    public String toString() {
        return firstName + ' ' +lastName + ' ' +
                "sent you a friend request on " + date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + " you " + getStatusString() + " it";
    }
}
