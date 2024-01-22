package com.gps.worker.model.fsm;

import com.gps.server.Register;
import com.gps.shared_resources.Request;
import com.gps.shared_resources.Service;
import com.gps.shared_resources.User;
import com.gps.worker.model.data.Worker;
import javafx.util.Pair;

import java.util.Date;

public abstract class StateAdapter implements IState {
    protected Context context;
    protected Worker data;

    protected StateAdapter(Context context, Worker data) {
        this.context = context;
        this.data = data;
    }

    protected void changeState(State newState) {
        context.changeState(newState.createState(context,data));
    }

    @Override
    public void rLogin(Pair<String, String> info) {}

    @Override
    public void registerLoginTransition() {}

    @Override
    public void disconnect() {
        data.disconnect();
    }

    @Override
    public void rRegister(Register register) {
    }

    @Override
    public void weekTransition(Date date) {} //só utilizar esta transição na transição mês->semana
    @Override
    public void weekChildrenTransition() { //utilizar esta para transição, por exemplo, de marcação->semana
    }
    @Override
    public void addServiceTransition() {}

    @Override
    public void concludeServiceTransition() {}

    @Override
    public void homepageTransition() {}
    @Override
    public void rAddService(Service service) {}
    @Override
    public void rEditService(Service service, int serviceId) {

    }
    @Override
    public void rRemoveService(int serviceId) {}
    @Override
    public boolean rUpdateServices(){
        return data.rUpdateServices();
    }

    @Override
    public void rUpdateCalender(Date date) {
        data.rUpdateCalender(date);
    }

    @Override
    public Object getResponse() {
        return data.getResponse();
    }

    @Override
    public Request getRequest() {
        return data.getRequest();
    }

    @Override
    public Date getFirstDayOfWeek() {
        return null;
    }

    @Override
    public void editServiceTransition() {
    }

    @Override
    public void removeServiceTransition() {

    }

    @Override
    public void rUpdateWeeklyCalender(User worker, Date date) {

    }
    @Override
    public void rUpdateUserWeeklyCalendar(User user, Date date){}

    @Override
    public void getWorkers() {

    }

    @Override
    public void getTypeOfServices() {

    }

    @Override
    public void rConcludeService(int serviceId) {

    }
}
