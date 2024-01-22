package com.gps.client.model.fsm.states;

import com.gps.client.model.data.Client;
import com.gps.client.model.fsm.Context;
import com.gps.client.model.fsm.State;
import com.gps.client.model.fsm.StateAdapter;
import javafx.util.Pair;

public class LoginState extends StateAdapter {
    public LoginState(Context context, Client data) {
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
