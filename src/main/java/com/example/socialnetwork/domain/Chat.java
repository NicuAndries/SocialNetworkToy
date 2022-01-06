package com.example.socialnetwork.domain;

import java.util.List;

public class Chat extends Entity<Long>{
    private String name;
    private List<Long> chatMembers;

    public Chat(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getMembers() {
        return chatMembers;
    }

    public void setMembers(List<Long> members) {
        this.chatMembers = members;
    }

    public void addMember(Long user){
        chatMembers.add(user);
    }
}
