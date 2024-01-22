package com.gps.worker.model.fsm.states;

import com.gps.shared_resources.Service;
import com.gps.shared_resources.User;
import com.gps.worker.model.data.Worker;
import com.gps.worker.model.fsm.Context;
import com.gps.worker.model.fsm.State;
import com.gps.worker.model.fsm.StateAdapter;

import java.util.Date;

public class SchedulingState extends StateAdapter {
    private Date firstDayOfWeek;
    public SchedulingState(Context context, Worker data, Date date) {
        super(context, data);
        firstDayOfWeek = date;
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
        return State.SCHEDULING;
    }
}
