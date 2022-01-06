package com.example.socialnetwork.domain;

public class EvenimentNotification {
    Long eveniment_id;
    Long user_id;
    String notification;
    String status;

    public EvenimentNotification(Long eveniment_id, Long user_id, String notification, String status) {
        this.eveniment_id = eveniment_id;
        this.user_id = user_id;
        this.notification = notification;
        this.status = status;
    }

    public Long getEveniment_id() {
        return eveniment_id;
    }

    public void setEveniment_id(Long eveniment_id) {
        this.eveniment_id = eveniment_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
