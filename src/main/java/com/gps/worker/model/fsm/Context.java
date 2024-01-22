package com.gps.worker.model.fsm;

import com.gps.server.Register;
import com.gps.shared_resources.Request;
import com.gps.shared_resources.Service;
import com.gps.shared_resources.User;
import com.gps.worker.model.data.Worker;
import javafx.util.Pair;

import java.beans.PropertyChangeSupport;
import java.util.Date;

public class Context {
    Worker data;
    IState state;

    public Context(PropertyChangeSupport pcs) {
        data = new Worker(pcs);
        state = State.LOGIN.createState(this,data);
    }

    public void changeState(IState state) {
        this.state = state;
    }

    public State getState() {
        return state.getState();
    }

    public void rLogin(Pair<String, String> info) {
        state.rLogin(info);
    }

    public void registerLoginTransition() {
        state.registerLoginTransition();
    }

    public void disconnect() {
        state.disconnect();
    }

    public void rRegister(Register register) {
        state.rRegister(register);
    }

    public void rAddService(Service service){
        state.rAddService(service);
    }

    public void rEditService(Service service, int serviceId){
        state.rEditService(service, serviceId);
    }

    public void rRemoveService(int serviceId){
        state.rRemoveService(serviceId);
    }

    public void homepageTransition() {
        state.homepageTransition();
    }

    public void weekTransition(Date date) {
        state.weekTransition(date);
    }

    public boolean rUpdateServices() { return state.rUpdateServices();}

    public Object getResponse() {
        return state.getResponse();
    }

    public Request getRequest() {
        return state.getRequest();
    }

    public void rUpdateCalendar(Date date) {
        state.rUpdateCalender(date);
    }

    public Date getFirstDayOfWeek() {
        return state.getFirstDayOfWeek();
    }

    public void addServiceTransition() {
        state.addServiceTransition();
    }

    public void weekChildrenTransition() {
        state.weekChildrenTransition();
    }

    public void editServiceTransition() {
        state.editServiceTransition();
    }

    public void removeServiceTransition() {
        state.removeServiceTransition();
    }

    public void concludeServiceTransition() {
        state.concludeServiceTransition();
    }

    public void rUpdateWeeklyCalender(User worker, Date date) {
        state.rUpdateWeeklyCalender(worker,date);
    }

    public void getWorkers() {
        state.getWorkers();
    }

    public void getTypeOfServices() {
        state.getTypeOfServices();
    }


    public void rUpdateUserWeeklyCalendar(User user, Date date) {
        state.rUpdateUserWeeklyCalendar(user,date);
    }

    public User getUser() {
        return new User(data.getRequest().getId(), null);
    }


    public void rConcludeService(int serviceId) {
        state.rConcludeService(serviceId);
    }
}
