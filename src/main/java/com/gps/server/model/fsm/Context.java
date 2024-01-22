package com.gps.server.model.fsm;


import com.gps.server.model.data.Server;
import com.gps.shared_resources.User;
import com.gps.shared_resources.utils.CellType;
import javafx.util.Pair;

import java.beans.PropertyChangeSupport;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Context {
    Server data;
    IState state;

    public Context(PropertyChangeSupport pcs) {
        data = new Server(pcs);
        state = State.AGENDA_MANAGER.createState(this,data);
    }

    public void changeState(IState state) {
        this.state = state;
    }

    public State getState() {
        return state.getState();
    }

    public void agendaTransition() {
        state.agendaTransition();
    }

    public List<User> getWorkers() {
        return state.getWorkers();
    }

    public Pair<Boolean,String> removeWorker(User user) {
        return state.removeWorker(user);
    }

    public void statisticsTransition(User user) {
        state.statisticsTransition(user);
    }

    public Map<String,Pair<Integer,Integer>> getStatistic() {
        return state.getStatistic();
    }

    public Pair<Boolean, String> toggleDay(User user, Date date) {
        return state.toggleDay(user,date);
    }

    public List<CellType> updateAgenda(User user, Date date) {
        return state.updateAgenda(user,date);
    }
}
