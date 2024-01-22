package com.gps.server.model.fsm;


import com.gps.server.model.data.Server;
import com.gps.shared_resources.User;
import com.gps.shared_resources.utils.CellType;
import javafx.util.Pair;

import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class StateAdapter implements IState {
    protected Context context;
    protected Server data;

    protected StateAdapter(Context context, Server data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public void agendaTransition() {

    }

    @Override
    public List<User> getWorkers() {
        return null;
    }

    @Override
    public Pair<Boolean, String> removeWorker(User user) {
        return null;
    }

    @Override
    public void statisticsTransition(User user) {

    }

    @Override
    public Map<String, Pair<Integer, Integer>> getStatistic() {
        return null;
    }

    @Override
    public Pair<Boolean, String> toggleDay(User user, Date date) {
        return null;
    }

    @Override
    public List<CellType> updateAgenda(User user, Date date) {
        return null;
    }

    protected void changeState(State newState) {
        context.changeState(newState.createState(context,data));
    }
}
