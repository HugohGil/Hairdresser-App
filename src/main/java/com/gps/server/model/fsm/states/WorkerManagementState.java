package com.gps.server.model.fsm.states;

import com.gps.server.model.data.Server;
import com.gps.server.model.fsm.Context;
import com.gps.server.model.fsm.State;
import com.gps.server.model.fsm.StateAdapter;
import com.gps.shared_resources.User;
import javafx.util.Pair;

import java.util.List;


public class WorkerManagementState extends StateAdapter {
    public WorkerManagementState(Context context, Server data) {
        super(context, data);
    }

    @Override
    public List<User> getWorkers() {
        return data.getWorkers();
    }

    @Override
    public Pair<Boolean, String> removeWorker(User user) {
        return data.removeWorker(user);
    }

    @Override
    public void agendaTransition() {
        changeState(State.AGENDA_MANAGER);
    }

    @Override
    public void statisticsTransition(User user) {
        context.changeState(new StatisticsState(context,data,user));
    }

    @Override
    public State getState() {
        return State.WORKER_MANAGEMENT;
    }
}
