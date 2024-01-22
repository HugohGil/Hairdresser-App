package com.gps.shared_resources.responses;

import com.gps.shared_resources.CellService;

import java.io.Serializable;

public class UpdateUserWeeklyCalendarResponse implements Serializable {
    CellService[][] cells;

    public UpdateUserWeeklyCalendarResponse(CellService[][] cells) {
        this.cells = cells;
    }

    public CellService[][] getCells() {
        return cells;
    }
}
