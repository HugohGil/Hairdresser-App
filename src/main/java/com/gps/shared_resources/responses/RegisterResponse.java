package com.gps.shared_resources.responses;

public class RegisterResponse extends Response {
    private String fieldError;
    public RegisterResponse(boolean success, String msg,String fieldError) {
        super(success, msg);
        this.fieldError = fieldError;
    }

    public String getFieldError() {
        return fieldError;
    }
}
