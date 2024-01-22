package com.gps.client.model.fsm.states;

import com.gps.client.model.data.Client;
import com.gps.client.model.fsm.Context;
import com.gps.client.model.fsm.State;
import com.gps.client.model.fsm.StateAdapter;
import com.gps.shared_resources.Service;
import com.gps.shared_resources.User;

import java.util.Date;

public class EditServiceState extends StateAdapter {
    private Date firstDayOfWeek;
    public EditServiceState(Context context, Client data, Date date) {
        super(context, data);
        firstDayOfWeek = date;
    }
    @Override
    public void rEditService(Service service, int serviceId) {
        data.rEditService(service,serviceId);
    }
    @Override
    public void rAddService(Service service) {
        data.rAddService(service);
    }
    @Override
    public void weekChildrenTransition() {
        context.changeState(new WeaklyAgenda(context,data, firstDayOfWeek));
    }
    @Override
    public void rUpdateWeeklyCalender(User worker, Date date) {
        data.rUpdateWeeklyCalender(worker, date);
    }
    @Override
    public void getTypeOfServices() {
        data.getTypeOfServices();
    }

    @Override
    public Date getFirstDayOfWeek() {
        return firstDayOfWeek;
    }
    @Override
    public State getState() {
        return State.EDIT_SERVICE;
    }
}
