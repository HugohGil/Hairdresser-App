package com.gps.client.model.fsm.states;

import com.gps.client.model.data.Client;
import com.gps.client.model.fsm.Context;
import com.gps.client.model.fsm.State;
import com.gps.client.model.fsm.StateAdapter;
import com.gps.shared_resources.User;

import java.util.Date;

public class RemoveServiceState extends StateAdapter {
    Date firstDayOfWeek;
    public RemoveServiceState(Context context, Client data, Date date) {
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
