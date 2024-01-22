package com.gps.worker.model.fsm.states;

import com.gps.worker.model.data.Worker;
import com.gps.worker.model.fsm.Context;
import com.gps.worker.model.fsm.State;
import com.gps.worker.model.fsm.StateAdapter;

import java.util.Date;

public class MonthlyAgenda extends StateAdapter {
    public MonthlyAgenda(Context context, Worker data) {
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
