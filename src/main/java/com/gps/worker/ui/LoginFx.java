package com.gps.worker.ui;

import com.gps.shared_resources.RequestsType;
import com.gps.shared_resources.responses.Response;
import com.gps.worker.model.ModelManager;
import com.gps.worker.model.fsm.State;
import com.gps.worker.ui.util.ToastMessage;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;

public class LoginFx {
    public AnchorPane pane;
    public Button submit;
    public TextField emailField;
    public PasswordField passField;
    public Button registerButton;

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

        model.addPropertyChangeListener(RequestsType.LOGIN_WORKER.toString(), evt -> {
            Response response = (Response) model.getResponse();
            if (response.isSuccess()) {
                model.homepageTransition();
            }
            else {
                ToastMessage.show((pane.getScene().getWindow()), response.getMsg());
                emailField.setStyle("-fx-text-box-border: #AA0000");
                passField.setStyle("-fx-text-box-border: #AA0000");
            }
        });

        registerButton.setOnAction(event -> {
            model.registerLoginTransition();
        });

        submit.setOnAction(event -> {
            if (emailField.getText().isEmpty())
                emailField.setStyle("-fx-text-box-border: #AA0000");
            else {
                emailField.setStyle("");
            }
            if (passField.getText().isEmpty())
                passField.setStyle("-fx-text-box-border: #AA0000");
            else {
                passField.setStyle("");
            }
            if (!emailField.getText().isEmpty() && !passField.getText().isEmpty()) {
                model.rLogin(new Pair<>(emailField.getText(),passField.getText()));
            }
        });
    }


    private void update() {
        pane.setVisible(model != null && model.getState() == State.LOGIN);
    }
}
