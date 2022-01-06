package com.example.socialnetwork.utils.events;

import com.example.socialnetwork.domain.Chat;

public class ChatChangedEvent implements Event{
    private ChangeEventType type;
    private Chat data, oldData;

    public ChatChangedEvent(ChangeEventType type, Chat data) {
        this.type = type;
        this.data = data;
    }

    public ChatChangedEvent(ChangeEventType type, Chat data, Chat oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Chat getData() {
        return data;
    }

    public Chat getOldData() {
        return oldData;
    }
}
