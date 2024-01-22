package com.gps.server.ui;

import com.gps.client.ui.util.ToastMessage;
import com.gps.server.model.fsm.ModelManager;
import com.gps.server.model.fsm.State;
import com.gps.shared_resources.User;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;

import java.util.List;

public class WorkerManagement {
    public BorderPane pane;
    public ListView<User> workersList;
    public Button remove;
    public Button statistic;
    public Button back;
    private ModelManager model;

    public void setModel(ModelManager model) {
        this.model = model;

        registerHandlers();
        update();
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(ModelManager.PROP_STATE, evt -> {
            update();
        });

        back.setOnAction(event -> {
            model.agendaTransition();
        });

        remove.setOnAction(event -> {
            clearView();
            User user = workersList.getSelectionModel().getSelectedItem();
            if (user != null) {
                Pair<Boolean,String> response = model.removeWorker(user);
                if(response.getKey()) {
                    workersList.setItems(FXCollections.observableList(model.getWorkers()));
                    workersList.setStyle("-fx-background-color: #b3ffa4");
                } else{
                    workersList.setStyle("-fx-background-color: #AA0000");
                }
                ToastMessage.show((pane.getScene().getWindow()), response.getValue());
            } else {
                ToastMessage.show((pane.getScene().getWindow()), "Selecione o funcionário que quer remover");
                workersList.setStyle("-fx-background-color: #AA0000");
            }
        });

        statistic.setOnAction(event -> {
            clearView();
            User user = workersList.getSelectionModel().getSelectedItem();
            if (user != null) {
                model.statisticsTransition(user);
            } else {
                ToastMessage.show((pane.getScene().getWindow()),"Selecione um funcionário para poder ver as suas estatísticas");
                workersList.setStyle("-fx-background-color: #AA0000");
            }
        });
    }

    private void update() {
        clearView();
        List<User> users = model.getWorkers();
        if (users != null) {
            workersList.setItems(FXCollections.observableList(users));
        }
     
        pane.setVisible(model != null && model.getState() == State.WORKER_MANAGEMENT);
    }

    private void clearView() {
        workersList.setStyle("");
    }
}
