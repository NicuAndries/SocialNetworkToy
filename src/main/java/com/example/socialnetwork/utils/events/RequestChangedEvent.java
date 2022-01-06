package com.example.socialnetwork.utils.events;

import com.example.socialnetwork.domain.FriendRequest;

public class RequestChangedEvent implements Event{
    private ChangeEventType type;
    private FriendRequest data, oldData;

    public RequestChangedEvent(ChangeEventType type, FriendRequest data) {
        this.type = type;
        this.data = data;
    }

    public RequestChangedEvent(ChangeEventType type, FriendRequest data, FriendRequest oldData) {
        this.type = type;
        this.data = data;
        this.oldData=oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public FriendRequest getData() {
        return data;
    }

    public FriendRequest getOldData() {
        return oldData;
    }
}
