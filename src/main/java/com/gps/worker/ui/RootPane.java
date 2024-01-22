package com.gps.worker.ui;

import com.gps.worker.model.ModelManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class RootPane extends BorderPane {
    ModelManager model;

    public RootPane(ModelManager model) {
        this.model = model;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        FXMLLoader login = new FXMLLoader(getClass().getResource("/com.gps.worker/LoginFx.fxml"));
        FXMLLoader register = new FXMLLoader(getClass().getResource("/com.gps.worker/RegisterFx.fxml"));
        FXMLLoader monthlyAgenda = new FXMLLoader(getClass().getResource("/com.gps.worker/MonthlyAgenda.fxml"));
        FXMLLoader weaklyAgenda = new FXMLLoader(getClass().getResource("/com.gps.worker/WeaklyAgenda.fxml"));
        FXMLLoader schedule = new FXMLLoader(getClass().getResource("/com.gps.worker/Schedule.fxml"));
        FXMLLoader edit = new FXMLLoader(getClass().getResource("/com.gps.worker/Edit.fxml"));
        FXMLLoader remove = new FXMLLoader(getClass().getResource("/com.gps.worker/Remove.fxml"));
        FXMLLoader conclude = new FXMLLoader(getClass().getResource("/com.gps.worker/Conclude.fxml"));

        StackPane stackPane;
        try {
            stackPane = new StackPane(login.load(),register.load(),monthlyAgenda.load(),
                    weaklyAgenda.load(), schedule.load(), edit.load(), remove.load(), conclude.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        login.<LoginFx>getController().setModel(model);
        register.<RegisterFx>getController().setModel(model);
        monthlyAgenda.<MonthlyAgendaFx>getController().setModel(model);
        weaklyAgenda.<WeaklyAgendaFx>getController().setModel(model);
        schedule.<Schedule>getController().setModel(model);
        edit.<Edit>getController().setModel(model);
        remove.<Remove>getController().setModel(model);
        conclude.<Conclude>getController().setModel(model);

        this.setCenter(stackPane);
    }

    private void registerHandlers() { }

    private void update() { }
}
