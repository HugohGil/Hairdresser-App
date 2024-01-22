package com.gps.shared_resources.responses;

import com.gps.shared_resources.CellService;

import java.io.Serializable;

public class UpdateWeeklyCalenderResponse implements Serializable {
    CellService[][] cells;

    public UpdateWeeklyCalenderResponse(CellService[][] cells) {
        this.cells = cells;
    }

    public CellService[][] getCells() {
        return cells;
    }
}
