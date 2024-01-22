package com.gps.server.ui;

import com.gps.client.ui.util.ToastMessage;
import com.gps.server.model.fsm.ModelManager;
import com.gps.server.model.fsm.State;
import com.gps.shared_resources.MyButton;
import com.gps.shared_resources.RequestsType;
import com.gps.shared_resources.User;
import com.gps.shared_resources.utils.CellType;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.text.DateFormatSymbols;
import java.util.*;

public class AgendaManager {
    public AnchorPane pane;
    public Button gestao;
    public Label monthLabel;
    public Button nextMonth;
    public Button previousMonth;
    public GridPane gridPane;
    public ComboBox<User> workers;
    public Button toggleDay;
    public MyButton selectedDay;
    private ModelManager model;
    private List<MyButton> buttons = new ArrayList<>();

    int month;
    int year;

    public void setModel(ModelManager model) {
        this.model = model;

        registerHandlers();
        update();
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(ModelManager.PROP_STATE, evt -> {
            update();
        });

        model.addPropertyChangeListener(RequestsType.UPDATE_CALENDAR.toString(), evt -> {
            System.out.println("here");
            update();
        });

        gestao.setOnAction(event -> {
            model.agendaTransition();
        });

        calendarDatesHandler();

        toggleDay.setOnAction(event -> {
            updateCalendar(workers.getSelectionModel().getSelectedItem());
            if (selectedDay == null) {
                ToastMessage.show((pane.getScene().getWindow()),"Selecione um dia do mês");
            } else {
                User worker = workers.getSelectionModel().getSelectedItem();

                Pair<Boolean,String> result = model.toggleDay(worker,selectedDay.getDate());
                updateCalendar(workers.getSelectionModel().getSelectedItem());
                ToastMessage.show((pane.getScene().getWindow()),result.getValue());
            }
        });

        workers.setOnAction(actionEvent -> {
            updateCalendar(workers.getSelectionModel().getSelectedItem());
        });
    }

    private void setupWorkers() {
        List<User> users = model.getWorkers();
        if (users != null && !users.isEmpty()) {
            workers.setValue(users.get(0));
            workers.setItems(FXCollections.observableList(users));
        }
    }

    private void updateCalendar(User worker) {
        if (worker != null) {

            List<CellType> update = model.updateAgenda(worker,buttons.get(0).getDate());

            if (update != null) {
                int i = 0;
                for (var item : buttons) {
                    switch (update.get(i++)) {
                        case FULL -> {
                            if (!item.getButton().isDisable()) {
                                item.getButton().setStyle("-fx-background-color: #ff8c8c; -fx-border-color: #000000;");
                                item.setStyle("-fx-background-color: #ff8c8c; -fx-border-color: #000000;");
                            }
                        }
                        case DISABLE -> {
                            item.getButton().setStyle("-fx-background-color: #d9d9d9; -fx-border-color: #000000;");
                            item.setStyle("-fx-background-color: #d9d9d9; -fx-border-color: #000000;");
                        }
                        case AVAILABLE -> {
                            if (!item.getButton().isDisable()) {
                                item.getButton().setStyle("-fx-background-color: #b3ffa4; -fx-border-color: #000000;");
                                item.setStyle("-fx-background-color: #b3ffa4; -fx-border-color: #000000;");
                            }
                        }
                    }
                }
            }
        }
    }

    private void calendarDatesHandler() {
        Date date = new Date();
        month = date.getMonth();
        year = date.getYear();

        refreshCalendar(year,month);

        nextMonth.setOnAction(event -> {
            ++month;
            if (month == 12) {
                month = 0;
                year++;
            }
            refreshCalendar(year,month);
        });

        previousMonth.setOnAction(event -> {
            --month;
            if (month == -1) {
                month = 11;
                year--;
            }
            refreshCalendar(year,month);
        });
    }

    private void refreshCalendar(int year,int month) {
        buttons.clear();
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Button button) {
                button.setDisable(false);
                button.setStyle("-fx-background-color: #b3ffa4; -fx-border-color: #000000;");
            }
        }
        monthLabel.setText(getMonthForInt(month));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year + 1900);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DATE,1);
        calendar.set(Calendar.HOUR_OF_DAY,15);          // end of time where last service available day becomes unavailable after this time
        Calendar today = Calendar.getInstance();
        int startDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        calendar.add(Calendar.DATE,-startDay);

        for(Node node : gridPane.getChildren()) {
            if (node instanceof Button button) {
                button.setText(calendar.get(Calendar.DATE) + "");

                buttons.add(new MyButton(button,calendar.getTime()));
                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY || calendar.before(today)) {
                    button.setDisable(true);
                    button.setStyle("-fx-background-color: #d9d9d9; -fx-border-color: #000000;");
                }
                calendar.add(Calendar.DATE,1);
            }
        }

        for(var item : buttons) {
            item.getButton().setOnAction(event -> {
                updateCalendar(workers.getSelectionModel().getSelectedItem());
                if (item.getButton().getStyle().contains("ff8c8c")) {
                    ToastMessage.show((pane.getScene().getWindow()),"Esse dia esta com reservas");
                    return;
                }
                else if(selectedDay == item) {
                    item.getButton().setStyle(item.getStyle());
                    selectedDay = null;
                    return;
                }
                if (selectedDay != null) {
                    selectedDay.getButton().setStyle(selectedDay.getStyle());
                }
                item.getButton().setStyle("-fx-background-color: #00c000; -fx-border-color: #000000;");
                selectedDay = item;
            });
        }

        updateCalendar(workers.getSelectionModel().getSelectedItem());
    }

    String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }

        return month.toUpperCase(Locale.ROOT).substring(0,3);
    }

    private void update() {
        setupWorkers();
        updateCalendar(workers.getSelectionModel().getSelectedItem());
        pane.setVisible(model != null && model.getState() == State.AGENDA_MANAGER);
    }
}
