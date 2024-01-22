package com.gps.client.model.fsm;

import com.gps.client.model.data.Client;
import com.gps.client.model.fsm.states.*;

public enum State {
    LOGIN,REGISTER, MONTHLY_AGENDA, WEAKLY_AGENDA, SCHEDULING, EDIT_SERVICE, REMOVE_SERVICE;

    IState createState(Context context, Client data) {
        return switch (this) {
            case LOGIN -> new LoginState(context,data);
            case REGISTER -> new RegisterState(context,data);
            case MONTHLY_AGENDA -> new MonthlyAgenda(context,data);
            case WEAKLY_AGENDA -> new WeaklyAgenda(context,data,null);
            case SCHEDULING -> new SchedulingState(context,data,null);
            case EDIT_SERVICE -> new EditServiceState(context,data,null);
            case REMOVE_SERVICE -> new RemoveServiceState(context,data, null);
        };
    }
}
