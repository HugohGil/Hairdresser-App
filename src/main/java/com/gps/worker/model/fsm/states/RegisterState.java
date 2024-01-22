package com.gps.worker.model.fsm.states;

import com.gps.server.Register;
import com.gps.worker.model.data.Worker;
import com.gps.worker.model.fsm.Context;
import com.gps.worker.model.fsm.State;
import com.gps.worker.model.fsm.StateAdapter;

public class RegisterState extends StateAdapter {
    public RegisterState(Context context, Worker data) {
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
