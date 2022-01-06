package com.example.socialnetwork.utils.events;

import com.example.socialnetwork.domain.Chat;
import com.example.socialnetwork.domain.Message;

public class MessageChangedEvent implements Event{
    private ChangeEventType type;
    private Message data, oldData;

    public MessageChangedEvent(ChangeEventType type, Message data) {
        this.type = type;
        this.data = data;
    }

    public MessageChangedEvent(ChangeEventType type, Message data, Message oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Message getData() {
        return data;
    }

    public Message getOldData() {
        return oldData;
    }
}
