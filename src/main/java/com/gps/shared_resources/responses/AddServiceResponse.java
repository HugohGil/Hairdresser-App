package com.gps.shared_resources.responses;

import java.io.Serializable;

public class AddServiceResponse extends Response {
    public AddServiceResponse(boolean success, String msg) {
        super(success, msg);
    }
}
