package com.gps.server.ui;

import com.gps.server.model.fsm.ModelManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
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
        FXMLLoader workers = new FXMLLoader(getClass().getResource("/com.gps.server/WorkersManagement.fxml"));
        FXMLLoader agenda = new FXMLLoader(getClass().getResource("/com.gps.server/AgendaManager.fxml"));
        FXMLLoader statistics = new FXMLLoader(getClass().getResource("/com.gps.server/WorkerStatistics.fxml"));

        StackPane stackPane;
        try {
            stackPane = new StackPane((Pane) workers.load(),(Pane) agenda.load(),(Pane) statistics.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        workers.<WorkerManagement>getController().setModel(model);
        agenda.<AgendaManager>getController().setModel(model);
        statistics.<WorkerStatistics>getController().setModel(model);

        this.setCenter(stackPane);
    }

    private void registerHandlers() { }

    private void update() { }
}
