package com.gps.shared_resources.responses;

public class ConcludeServiceResponse extends Response{
    public ConcludeServiceResponse(boolean success, String msg) {
        super(success, msg);
    }

    public ConcludeServiceResponse(boolean success) {
        super(success);
    }
}
