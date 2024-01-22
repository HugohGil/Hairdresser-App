package com.gps.client.model;

import com.gps.client.model.fsm.Context;
import com.gps.client.model.fsm.State;
import com.gps.server.Register;
import com.gps.shared_resources.Request;
import com.gps.shared_resources.Service;
import com.gps.shared_resources.User;
import javafx.util.Pair;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;

public class ModelManager {
    public static final String PROP_STATE = "state";
    public static final String PROP_DATA  = "data";

    Context context;
    PropertyChangeSupport pcs;

    public ModelManager() {
        pcs = new PropertyChangeSupport(this);
        this.context = new Context(pcs);
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property, listener);
    }

    public void rLogin(Pair<String, String> info) {
        context.rLogin(info);
    }

    public void disconnect() {
        context.disconnect();
    }

    public void rRegister(Register register) {
        context.rRegister(register);
    }

    public Object getResponse() {
        return context.getResponse();
    }

    public Request getRequest() {
        return context.getRequest();
    }

    public void rAddService(Service service) {
        context.rAddService(service);
    }
    public void rEditService(Service service, int serviceId) {
        context.rEditService(service, serviceId);
        pcs.firePropertyChange(PROP_DATA,null,context.rUpdateServices());
    }
    public void rRemoveService(int serviceId) {
        context.rRemoveService(serviceId);
        pcs.firePropertyChange(PROP_DATA,null,context.rUpdateServices());
    }


    //Transitions
    public void addServiceTransition() {
        context.addServiceTransition();
        pcs.firePropertyChange(PROP_STATE,null,context.getState());
    }
    public void registerLoginTransition() {
        context.registerLoginTransition();
        pcs.firePropertyChange(PROP_STATE,null,context.getState());
    }
    public void homepageTransition(){
        context.homepageTransition();
        pcs.firePropertyChange(PROP_STATE, null, context.getState());
    }
    public void weekTransition(Date date){
        context.weekTransition(date);
        pcs.firePropertyChange(PROP_STATE, null, context.getState());
    }


    public State getState() {
        return context.getState();
    }

    public void rUpdateCalendar(Date date) {
        context.rUpdateCalendar(date);
    }
    public void rUpdateServices() {
        context.rUpdateServices();
    }

    public Date getFirstDayOfWeek() {
        return context.getFirstDayOfWeek();
    }

    public void weekChildrenTransition() {
        context.weekChildrenTransition();
        pcs.firePropertyChange(PROP_STATE,null,context.getState());
    }

    public void editServiceTransition() {
        context.editServiceTransition();
        pcs.firePropertyChange(PROP_STATE,null,context.getState());
    }

    public void removeServiceTransition() {
        context.removeServiceTransition();
        pcs.firePropertyChange(PROP_STATE,null,context.getState());
    }

    public void rUpdateWeeklyCalender(User worker, Date date) {
        context.rUpdateWeeklyCalender(worker,date);
    }

    public void getWorkers() {
        context.getWorkers();
    }

    public void getTypeOfServices() {
        context.getTypeOfServices();
    }

    public void rUpdateUserWeeklyCalendar(User user, Date date) {
        context.rUpdateUserWeeklyCalendar(user, date);
    }

    public User getUser() {
        return context.getUser();
    }
}
