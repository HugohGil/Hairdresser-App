package com.gps.server.model.fsm;


import com.gps.shared_resources.User;
import com.gps.shared_resources.utils.CellType;
import javafx.scene.control.Cell;
import javafx.util.Pair;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ModelManager {
    public static final String PROP_STATE = "state";
    public static final String PROP_DATA  = "data";

    Context context;
    PropertyChangeSupport pcs;

    public ModelManager() {
        pcs = new PropertyChangeSupport(this);
        this.context = new Context(pcs);
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property, listener);
    }

    public State getState() {
        return context.getState();
    }

    public void agendaTransition() {
        context.agendaTransition();
        pcs.firePropertyChange(PROP_STATE,null,null);
    }

    public List<User> getWorkers() {
        return context.getWorkers();
    }

    public Pair<Boolean,String> removeWorker(User user) {
        return context.removeWorker(user);
    }

    public void statisticsTransition(User user) {
         context.statisticsTransition(user);
        pcs.firePropertyChange(PROP_STATE,null,null);
    }

    public Map<String,Pair<Integer,Integer>> getStatistic() {
        return context.getStatistic();
    }

    public Pair<Boolean, String> toggleDay(User user, Date date) {
        return context.toggleDay(user,date);
    }

    public List<CellType> updateAgenda(User user, Date date) {
        return context.updateAgenda(user,date);
    }

}
