package com.gps.client.model.fsm.states;

import com.gps.client.model.data.Client;
import com.gps.client.model.fsm.Context;
import com.gps.client.model.fsm.State;
import com.gps.client.model.fsm.StateAdapter;
import com.gps.shared_resources.User;

import java.util.Date;

public class WeaklyAgenda extends StateAdapter {
    private final Date firstDayOfWeek;
    public WeaklyAgenda(Context context, Client data,Date firstDayOfWeek) {
        super(context, data);
        this.firstDayOfWeek = firstDayOfWeek;
    }

    @Override
    public void homepageTransition() {
        changeState(State.MONTHLY_AGENDA);
    }
    @Override
    public void addServiceTransition(){
        context.changeState(new SchedulingState(context,data,firstDayOfWeek));
    }

    @Override
    public void editServiceTransition() {
        context.changeState(new EditServiceState(context, data, firstDayOfWeek));
    }
    @Override
    public void removeServiceTransition() {
        context.changeState(new RemoveServiceState(context, data, firstDayOfWeek));
    }

    @Override
    public Date getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    @Override
    public void rUpdateWeeklyCalender(User worker, Date date) {
        data.rUpdateWeeklyCalender(worker, date);
    }

    @Override
    public void getWorkers() {
        data.getWorkers();
    }

    @Override
    public State getState() {
        return State.WEAKLY_AGENDA;
    }
}
