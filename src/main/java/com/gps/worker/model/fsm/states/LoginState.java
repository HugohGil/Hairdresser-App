package com.gps.worker.model.fsm.states;

import com.gps.worker.model.data.Worker;
import com.gps.worker.model.fsm.Context;
import com.gps.worker.model.fsm.State;
import com.gps.worker.model.fsm.StateAdapter;
import javafx.util.Pair;

public class LoginState extends StateAdapter {
    public LoginState(Context context, Worker data) {
        super(context, data);
    }

    @Override
    public void rLogin(Pair<String, String> info) {
        data.rLogin(info);
    }

    @Override
    public void registerLoginTransition() {
        changeState(State.REGISTER);
    }

    @Override
    public State getState() {
        return State.LOGIN;
    }

    @Override
    public void homepageTransition() {
        changeState(State.MONTHLY_AGENDA);
    }
}
