package com.gps.server.model.fsm.states;

import com.gps.server.model.data.Server;
import com.gps.server.model.fsm.Context;
import com.gps.server.model.fsm.State;
import com.gps.server.model.fsm.StateAdapter;
import com.gps.shared_resources.User;
import com.gps.shared_resources.utils.CellType;
import javafx.util.Pair;

import java.util.Date;
import java.util.List;


public class AgendaManagerState extends StateAdapter {
    public AgendaManagerState(Context context, Server data) {
        super(context, data);
    }

    @Override
    public List<User> getWorkers() {
        return data.getWorkers();
    }

    @Override
    public void agendaTransition() {
        changeState(State.WORKER_MANAGEMENT);
    }

    @Override
    public Pair<Boolean, String> toggleDay(User user, Date date) {
        return data.toggleDay(user, date);
    }

    @Override
    public List<CellType> updateAgenda(User user, Date date) {
        return data.updateCalendar(user,date);
    }

    @Override
    public State getState() {
        return State.AGENDA_MANAGER;
    }
}
