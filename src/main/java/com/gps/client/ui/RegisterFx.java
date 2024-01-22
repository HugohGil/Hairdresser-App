package com.gps.client.ui;

import com.gps.client.model.ModelManager;
import com.gps.client.model.fsm.State;
import com.gps.client.ui.util.ToastMessage;
import com.gps.server.Register;
import com.gps.shared_resources.RequestsType;
import com.gps.shared_resources.responses.RegisterResponse;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

public class RegisterFx {
    public AnchorPane pane;
    public Button submit;
    public TextField nameField;
    public TextField emailField;
    public DatePicker dateField;
    public PasswordField passField;
    public Button loginButton;

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

        model.addPropertyChangeListener(RequestsType.REGISTER.toString(), evt -> {
            RegisterResponse response = (RegisterResponse) model.getResponse();

            if (response.isSuccess()) {
                model.registerLoginTransition();
            } else if (Objects.equals(response.getFieldError(), "email")) {
                ToastMessage.show((pane.getScene().getWindow()), response.getMsg());
                emailField.setStyle("-fx-text-box-border: #AA0000");
            } else if (Objects.equals(response.getFieldError(), "nome")) {
                ToastMessage.show((pane.getScene().getWindow()), response.getMsg());
                nameField.setStyle("-fx-text-box-border: #AA0000");
            } else if (Objects.equals(response.getFieldError(), "password")) {
                ToastMessage.show((pane.getScene().getWindow()), response.getMsg());
                passField.setStyle("-fx-text-box-border: #AA0000");
            }
            else if (Objects.equals(response.getFieldError(), "unique")) {
                ToastMessage.show((pane.getScene().getWindow()), response.getMsg());
                nameField.setStyle("-fx-text-box-border: #AA0000");
                emailField.setStyle("-fx-text-box-border: #AA0000");
            }
        });
        loginButton.setOnAction(event -> {
            model.registerLoginTransition();
        });

        submit.setOnAction(event -> {
            if (nameField.getText().isEmpty()) {
                nameField.setStyle("-fx-text-box-border: #AA0000");
                return;
            }

            if (emailField.getText().isEmpty()) {
                emailField.setStyle("-fx-text-box-border: #AA0000");
                return;
            }

            if (passField.getText().isEmpty()) {
                passField.setStyle("-fx-text-box-border: #AA0000");
                return;
            }

            emailField.setStyle("");
            passField.setStyle("");
            nameField.setStyle("");
            dateField.setStyle("");

            try {
                LocalDate localDate = dateField.getValue();
                Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                if (date.compareTo(new Date()) > 0) {
                    throw new NullPointerException();
                }

                dateField.setStyle("");
                model.rRegister(new Register(nameField.getText(), emailField.getText(), passField.getText(), date));
            } catch (NullPointerException e) {
                dateField.setStyle("-fx-text-box-border: #AA0000");
            }
        });
    }


    private void update() {
        pane.setVisible(model != null && model.getState() == State.REGISTER);
    }
}
