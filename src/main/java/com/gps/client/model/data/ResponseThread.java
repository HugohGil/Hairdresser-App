package com.gps.client.model.data;

import com.gps.shared_resources.RequestsType;
import com.gps.shared_resources.responses.*;
import javafx.application.Platform;

import java.beans.PropertyChangeSupport;

public class ResponseThread extends Thread {
    private Client client;
    private Object response;
    private PropertyChangeSupport pcs;

    public ResponseThread(Client client, PropertyChangeSupport pcs) {
        this.client = client;
        this.pcs = pcs;
    }

    @Override
    public void run() {
        while (true) {
            response = client.readFromSocket();
            response();
        }
    }

    private synchronized void response() {
        if (response instanceof LoginResponse login) {
            Platform.runLater(() -> {
                pcs.firePropertyChange(RequestsType.LOGIN.toString(),null,null);
                //Sets the id of the client
                if (login.isSuccess()) {
                    client.getRequest().setId(login.getId());
                }
            });
        } else if (response instanceof RegisterResponse) {
            fire(RequestsType.REGISTER.toString());
        }
        else if(response instanceof UpdateWeeklyCalenderResponse) {
            fire(RequestsType.UPDATE_WEEK.toString());
        } else if (response instanceof  UpdateCalenderResponse) {
            fire(RequestsType.UPDATE_CALENDAR.toString());
        } else if (response instanceof WorkersResponse) {
            fire(RequestsType.GET_WORKERS.toString());
        } else if (response instanceof UpdateTypeServiceResponse) {
            fire(RequestsType.GET_TYPE_OF_SERVICE.toString());
        } else if (response instanceof AddServiceResponse) {
            fire(RequestsType.ADD_SERVICE.toString());
        }else if (response instanceof UpdateUserWeeklyCalendarResponse) {
            fire(RequestsType.UPDATE_USER_WEEK.toString());
        }
        else if(response instanceof RemoveServiceResponse){
            fire(RequestsType.REMOVE_SERVICE.toString());
        }
        else if(response instanceof EditServiceResponse){
            fire(RequestsType.EDIT_SERVICE.toString());
        } else if (response instanceof UpdateCalendarAssinc) {
            fire(RequestsType.UPDATE_CALENDAR_ASSINC.toString());
        }
    }

    private void fire(String response) {
        Platform.runLater(() -> pcs.firePropertyChange(response,null,null));
    }

    public synchronized Object getResponse() {
        return response;
    }
}
