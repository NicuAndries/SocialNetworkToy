package com.example.socialnetwork.utils.events;


import com.example.socialnetwork.domain.Friendship;

public class FriendshipChangedEvent implements Event{
    private ChangeEventType type;
    private Friendship data, oldData;

    public FriendshipChangedEvent(ChangeEventType type, Friendship data) {
        this.type = type;
        this.data = data;
    }

    public FriendshipChangedEvent(ChangeEventType type, Friendship data, Friendship oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Friendship getData() {
        return data;
    }

    public Friendship getOldData() {
        return oldData;
    }
}
