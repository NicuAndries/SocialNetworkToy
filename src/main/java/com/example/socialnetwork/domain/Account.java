package com.example.socialnetwork.domain;

public class Account extends Entity<String> {
    String username;
    String password;
    Long userId;

    public Account(String username, String password, Long userId) {
        this.username = username;
        this.password = password;
        this.userId = userId;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Long getUserId() {
        return userId;
    }
}
