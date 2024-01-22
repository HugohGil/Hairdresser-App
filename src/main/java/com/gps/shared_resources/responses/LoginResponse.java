package com.gps.shared_resources.responses;

public class LoginResponse extends Response {
    private Integer id;
    public LoginResponse(Integer id,boolean success, String msg) {
        super(success, msg);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
