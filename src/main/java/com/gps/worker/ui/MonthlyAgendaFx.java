package com.gps.worker.ui;

import com.gps.shared_resources.RequestsType;
import com.gps.shared_resources.responses.UpdateCalenderResponse;
import com.gps.shared_resources.utils.CellType;
import com.gps.worker.model.ModelManager;
import com.gps.worker.model.fsm.State;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.text.DateFormatSymbols;
import java.util.*;

public class MonthlyAgendaFx {
    public AnchorPane pane;
    public GridPane gridPane;
    public Label monthLabel;
    public Button nextMonth;
    public Button previousMonth;
    int month = 0;
    int year = 0;
    private ModelManager model;
    private final List<Date> dates = new ArrayList<>();

    public void setModel(ModelManager model) {
        this.model = model;

        registerHandlers();
        update();
    }

    private void registerHandlers() {
        Date date = new Date();
        month = date.getMonth();
        year = date.getYear();

        refreshCalendar(year, month);

        model.addPropertyChangeListener(ModelManager.PROP_STATE, evt -> {
            refreshCalendar(year, month);
            update();
        });

        model.addPropertyChangeListener(ModelManager.PROP_DATA, evt -> {
            update();
        });

        model.addPropertyChangeListener(RequestsType.UPDATE_CALENDAR_ASSINC.toString(), evt -> {
            refreshCalendar(year, month);
        });

        model.addPropertyChangeListener(RequestsType.UPDATE_CALENDAR_WORKER.toString(), evt -> {
            try {
                UpdateCalenderResponse response = (UpdateCalenderResponse) model.getResponse();

                int i = 0;
                for (Node node : gridPane.getChildren()) {
                    if (node instanceof Button button) {
                        Pair<CellType, Boolean> isFull = response.getCalendar().get(i++);
                        if (isFull.getValue()) {
                            if (!button.getStyle().contains("d9d9d9")) {
                                button.setStyle("-fx-background-color: #ff8c8c; -fx-border-color: #000000;");
                                button.setDisable(false);
                            }
                        } else {
                            if (isFull.getKey() == CellType.DISABLE) {
                                button.setStyle("-fx-background-color: #d9d9d9; -fx-border-color: #000000;");
                                button.setDisable(true);
                            } else {
                                if (!button.getStyle().contains("d9d9d9")) {
                                    button.setStyle("-fx-background-color: #b3ffa4; -fx-border-color: #000000;");
                                    button.setDisable(false);
                                }
                            }
                        }
                    }
                }
                i = 0;

                for (Node node : gridPane.getChildren()) {
                    if (node instanceof Button button) {
                        Date d = dates.get(i++);

                        //Get the first day of the week the user selected
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(d);
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                        button.setOnAction(event -> {
                            model.weekTransition(calendar.getTime());
                        });

                    }
                }
            } catch (ClassCastException e) {

            }
        });


        nextMonth.setOnAction(event -> {
            ++month;
            if (month == 12) {
                month = 0;
                year++;
            }
            refreshCalendar(year, month);
        });


        previousMonth.setOnAction(event -> {
            --month;
            if (month == -1) {
                month = 11;
                year--;
            }
            refreshCalendar(year, month);
        });
    }

    private void refreshCalendar(int year, int month) {

        dates.clear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year + 1900);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, 1);

        for (Node node : gridPane.getChildren()) {
            if (node instanceof Button button) {
                button.setDisable(false);
                button.setStyle("-fx-background-color: #b3ffa4; -fx-border-color: #000000;");
            }
        }

        monthLabel.setText(getMonthForInt(month));
        int startDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        calendar.add(Calendar.DATE, -startDay);

        model.rUpdateCalendar(calendar.getTime());

        for (Node node : gridPane.getChildren()) {
            if (node instanceof Button button) {
                button.setText(calendar.get(Calendar.DATE) + "");
                dates.add(calendar.getTime());
                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                    button.setDisable(true);
                    button.setStyle("-fx-background-color: #d9d9d9; -fx-border-color: #000000;");
                }
                calendar.add(Calendar.DATE, 1);
            }
        }
    }
    String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }

        return month.toUpperCase(Locale.ROOT);
    }

    private void update() {
        pane.setVisible(model != null && model.getState() == State.MONTHLY_AGENDA);
    }
}
