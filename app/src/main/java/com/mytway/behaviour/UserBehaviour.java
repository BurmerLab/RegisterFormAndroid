package com.mytway.behaviour;

import com.mytway.pojo.User;

public class UserBehaviour {

    private User user;
    private String message;

    public UserBehaviour() {
    }

    public UserBehaviour(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
