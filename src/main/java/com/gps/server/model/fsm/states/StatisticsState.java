package com.gps.server.model.fsm.states;

import com.gps.server.model.data.Server;
import com.gps.server.model.fsm.Context;
import com.gps.server.model.fsm.State;
import com.gps.server.model.fsm.StateAdapter;
import com.gps.shared_resources.User;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;


public class StatisticsState extends StateAdapter {
    private User user;
    public StatisticsState(Context context, Server data,User user) {
        super(context, data);
        this.user = user;
    }

    @Override
    public Map<String, Pair<Integer, Integer>> getStatistic() {
        return data.getStatistic(user);
    }

    @Override
    public void statisticsTransition(User user) {
        changeState(State.WORKER_MANAGEMENT);
    }

    @Override
    public State getState() {
        return State.WORKER_STATISTICS;
    }
}
