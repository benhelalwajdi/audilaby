package com.audiolaby.otto.events;

public class OfflineModeEvent {
    private boolean offline;

    public OfflineModeEvent(boolean offline) {
        this.offline = offline;
    }

    public boolean isOffline() {
        return this.offline;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
    }
}
