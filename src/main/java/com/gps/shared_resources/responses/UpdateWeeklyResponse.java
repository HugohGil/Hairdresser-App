package com.gps.shared_resources.responses;

import com.gps.shared_resources.Service;

import java.io.Serializable;
import java.util.List;

public class UpdateWeeklyResponse implements Serializable {
    List<Service> serviceList;
    public UpdateWeeklyResponse(List<Service> serviceList) {
        this.serviceList = serviceList;
    }

    public List<Service> getServiceList() {
        return serviceList;
    }
}
