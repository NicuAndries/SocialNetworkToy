package com.example.socialnetwork.utils.events;

import com.example.socialnetwork.domain.Eveniment;
import com.example.socialnetwork.domain.EvenimentNotification;
import com.example.socialnetwork.domain.Message;

public class EvenimentChangedEvent implements Event {
    private ChangeEventType type;
    private EvenimentNotification data, oldData;

    public EvenimentChangedEvent(ChangeEventType type, EvenimentNotification data) {
        this.type = type;
        this.data = data;
    }

    public EvenimentChangedEvent(ChangeEventType type, EvenimentNotification data, EvenimentNotification oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public EvenimentNotification getData() {
        return data;
    }

    public EvenimentNotification getOldData() {
        return oldData;
    }
}
