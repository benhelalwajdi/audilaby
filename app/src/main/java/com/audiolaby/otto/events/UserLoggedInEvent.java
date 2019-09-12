package com.audiolaby.otto.events;


import com.audiolaby.persistence.model.User;

public class UserLoggedInEvent {
    private User user;

    public UserLoggedInEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
