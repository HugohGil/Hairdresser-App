package com.gps.client.ui;

import com.gps.client.model.ModelManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;

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
        FXMLLoader login = new FXMLLoader(getClass().getResource("/com.gps.client/LoginFx.fxml"));
        FXMLLoader register = new FXMLLoader(getClass().getResource("/com.gps.client/RegisterFx.fxml"));
        FXMLLoader monthlyAgenda = new FXMLLoader(getClass().getResource("/com.gps.client/MonthlyAgenda.fxml"));
        FXMLLoader weaklyAgenda = new FXMLLoader(getClass().getResource("/com.gps.client/WeaklyAgenda.fxml"));
        FXMLLoader schedule = new FXMLLoader(getClass().getResource("/com.gps.client/Schedule.fxml"));
        FXMLLoader edit = new FXMLLoader(getClass().getResource("/com.gps.client/Edit.fxml"));
        FXMLLoader remove = new FXMLLoader(getClass().getResource("/com.gps.client/Remove.fxml"));

        StackPane stackPane;
        try {
            stackPane = new StackPane(login.load(),register.load(),monthlyAgenda.load(),
                    weaklyAgenda.load(), schedule.load(), edit.load(), remove.load());
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

        this.setCenter(stackPane);
    }

    private void registerHandlers() { }

    private void update() { }
}
