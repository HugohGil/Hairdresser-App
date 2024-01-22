package com.gps.shared_resources.responses;

import com.gps.shared_resources.User;

import java.io.Serializable;
import java.util.List;

public class WorkersResponse implements Serializable {
    List<User> users;

    public WorkersResponse(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }
}
