package com.gps.worker.model.fsm.states;

import com.gps.shared_resources.User;
import com.gps.worker.model.data.Worker;
import com.gps.worker.model.fsm.Context;
import com.gps.worker.model.fsm.State;
import com.gps.worker.model.fsm.StateAdapter;

import java.util.Date;

public class RemoveServiceState extends StateAdapter {
    Date firstDayOfWeek;
    public RemoveServiceState(Context context, Worker data, Date date) {
        super(context, data);
        firstDayOfWeek = date;
    }

    @Override
    public void rRemoveService(int id_service) {
        data.rRemoveService(id_service);
    }

    @Override
    public void rUpdateUserWeeklyCalendar(User user, Date date) {
        data.rUpdateUserWeeklyCalendar(user, date);
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
        return State.REMOVE_SERVICE;
    }
}
