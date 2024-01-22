package com.gps.server.model.fsm;


import com.gps.server.model.data.Server;
import com.gps.server.model.fsm.states.AgendaManagerState;
import com.gps.server.model.fsm.states.WorkerManagementState;

public enum State {
    WORKER_MANAGEMENT,AGENDA_MANAGER,WORKER_STATISTICS;

    IState createState(Context context, Server data) {
        return switch (this) {
            case WORKER_MANAGEMENT -> new WorkerManagementState(context,data);
            case AGENDA_MANAGER -> new AgendaManagerState(context,data);
            default -> null;
        };
    }
}
