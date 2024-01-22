package com.gps.shared_resources.responses;

import java.io.Serializable;

public class Response implements Serializable {
    private boolean success;
    private String msg;

    public Response(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public Response(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }
}
