package com.gps.client.model.fsm.states;

import com.gps.client.model.data.Client;
import com.gps.client.model.fsm.Context;
import com.gps.client.model.fsm.State;
import com.gps.client.model.fsm.StateAdapter;

import java.util.Date;

public class MonthlyAgenda extends StateAdapter {
    public MonthlyAgenda(Context context, Client data) {
        super(context, data);
    }

    @Override
    public void weekTransition(Date date) {
        context.changeState(new WeaklyAgenda(context,data,date));
    }

    @Override
    public State getState() {
        return State.MONTHLY_AGENDA;
    }
}
