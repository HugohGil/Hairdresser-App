package com.gps.server.model.fsm;

import com.gps.shared_resources.User;
import com.gps.shared_resources.utils.CellType;
import javafx.util.Pair;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IState {
    State getState();

    void agendaTransition();

    void statisticsTransition(User user);

    List<User> getWorkers();

    Pair<Boolean,String> toggleDay(User user, Date date);

    Pair<Boolean, String> removeWorker(User user);

    List<CellType> updateAgenda(User user, Date date);

    Map<String, Pair<Integer, Integer>> getStatistic();
}
