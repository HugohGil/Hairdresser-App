package com.gps.client.model.fsm.states;

import com.gps.client.model.data.Client;
import com.gps.client.model.fsm.Context;
import com.gps.client.model.fsm.State;
import com.gps.client.model.fsm.StateAdapter;
import com.gps.server.Register;

public class RegisterState extends StateAdapter {
    public RegisterState(Context context, Client data) {
        super(context, data);
    }

    @Override
    public void registerLoginTransition() {
        changeState(State.LOGIN);
    }

    @Override
    public void rRegister(Register register) {
        data.rRegister(register);
    }

    @Override
    public State getState() {
        return State.REGISTER;
    }
}
