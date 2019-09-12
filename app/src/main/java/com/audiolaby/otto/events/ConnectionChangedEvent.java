package com.audiolaby.otto.events;

public class ConnectionChangedEvent {
    private Status status;

    public enum Status {
        CONNECTED,
        DISCONNECTED
    }

    public ConnectionChangedEvent(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
