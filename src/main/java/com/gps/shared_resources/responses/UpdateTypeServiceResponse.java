package com.gps.shared_resources.responses;

import com.gps.shared_resources.TypeService;

import java.io.Serializable;
import java.util.List;

public class UpdateTypeServiceResponse implements Serializable {
    List<TypeService> typeServices;

    public UpdateTypeServiceResponse(List<TypeService> typeServices) {
        this.typeServices = typeServices;
    }

    public List<TypeService> getTypeServices() {
        return typeServices;
    }
}
