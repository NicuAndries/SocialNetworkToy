package com.example.socialnetwork.domain;

import javafx.scene.image.ImageView;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class Eveniment extends Entity<Long>{
    String name;
    LocalDate date;
    String time;
    String image;
    Map<Long, String> eventParticipants;

    public Eveniment(String name, LocalDate date,  String time) {
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Map<Long, String> getEventParticipants() {
        return eventParticipants;
    }

    public void setEventParticipants(Map<Long, String> eventParticipants) {
        this.eventParticipants = eventParticipants;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
