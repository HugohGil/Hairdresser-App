package com.gps.client.ui;

import com.gps.client.model.ModelManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainJFX extends Application {
    ModelManager model;

    @Override
    public void start(Stage stage) {
        model = new ModelManager();

        BorderPane root = new RootPane(model);
        Scene scene = new Scene(root,1280,720);
        stage.setScene(scene);
        stage.setTitle("GPS - Cliente");
        stage.setMinWidth(400);
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                model.disconnect();
                Platform.exit();
                System.exit(1);
            }
        });
    }
}
