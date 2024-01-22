package com.gps.worker.model.fsm;

import com.gps.server.Register;
import com.gps.shared_resources.Request;
import com.gps.shared_resources.Service;
import com.gps.shared_resources.User;
import javafx.util.Pair;

import java.util.Date;

public interface IState {
    //Transitions
    void registerLoginTransition();
    void weekTransition(Date date);
    
    void rLogin(Pair<String,String> info);
    void rAddService(Service service);

    void rUpdateCalender(Date date);

    void rUpdateWeeklyCalender(User worker, Date date);

    void rEditService(Service service, int serviceId);
    void rRemoveService(int serviceId);
    State getState();

    void getWorkers();

    void getTypeOfServices();

    void disconnect();

    void rRegister(Register register);

    void homepageTransition();
    boolean rUpdateServices();

    Object getResponse();

    Request getRequest();

    Date getFirstDayOfWeek();

    void addServiceTransition();

    void weekChildrenTransition();

    void editServiceTransition();

    void removeServiceTransition();

    void rUpdateUserWeeklyCalendar(User user, Date date);

    void concludeServiceTransition();

    void rConcludeService(int serviceId);
}
