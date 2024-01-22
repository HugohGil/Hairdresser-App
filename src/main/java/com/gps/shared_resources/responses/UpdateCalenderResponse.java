package com.gps.shared_resources.responses;

import com.gps.shared_resources.utils.CellType;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.List;

public class UpdateCalenderResponse implements Serializable {
    List<Pair<CellType, Boolean>> calendar;

    public UpdateCalenderResponse(List<Pair<CellType, Boolean>> calendar) {
        this.calendar = calendar;
    }

    public List<Pair<CellType, Boolean>> getCalendar() {
        return calendar;
    }
}
