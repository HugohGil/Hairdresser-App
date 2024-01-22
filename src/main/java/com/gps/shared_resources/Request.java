package com.gps.shared_resources;

import java.io.Serializable;

public class Request implements Serializable {
    public static final String DATEFORMAT = "yyyy MM d hh:mm";
    private int id;
    private RequestsType type;
    private Object request;

    public int getId() {
        return id;
    }

    public RequestsType getType() {
        return type;
    }

    public Object getRequest() {
        return request;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(RequestsType type) {
        this.type = type;
    }

    public void setRequest(Object request) {
        this.request = request;
    }
}
