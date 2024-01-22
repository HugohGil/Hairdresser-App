package com.gps.worker.model.fsm.states;

import com.gps.shared_resources.User;
import com.gps.worker.model.data.Worker;
import com.gps.worker.model.fsm.Context;
import com.gps.worker.model.fsm.State;
import com.gps.worker.model.fsm.StateAdapter;

import java.util.Date;

public class ConcludeServiceState extends StateAdapter {
    private Date firstDayOfWeek;
    public ConcludeServiceState(Context context, Worker data, Date date) {
        super(context, data);
        firstDayOfWeek = date;
    }

    @Override
    public void rConcludeService(int serviceId) {
        data.rConcludeService(serviceId);
    }

    @Override
    public void rUpdateWeeklyCalender(User user, Date date) {
        data.rUpdateWeeklyCalender(user, date);
    }

    @Override
    public Date getFirstDayOfWeek() {
        return firstDayOfWeek;
    }
    @Override
    public void weekChildrenTransition() {
        context.changeState(new WeaklyAgenda(context,data, firstDayOfWeek));
    }

    @Override
    public State getState() {
        return State.CONCLUDE_SERVICE;
    }
}