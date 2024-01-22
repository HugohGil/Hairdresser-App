package com.gps.client.ui;

import com.gps.client.model.ModelManager;
import com.gps.client.model.data.Client;
import com.gps.client.model.fsm.State;
import com.gps.shared_resources.CellService;
import com.gps.shared_resources.RequestsType;
import com.gps.shared_resources.User;
import com.gps.shared_resources.responses.*;
import com.gps.shared_resources.utils.CellType;
import javafx.collections.FXCollections;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Popup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WeaklyAgendaFx {
    public BorderPane pane;

    public GridPane gridPane;
    public Button btnReturnToMonthlyAgenda;
    public Label dom;
    public Label seg;
    public Label ter;
    public Label qua;
    public Label sex;
    public Label qui;
    public Label sab;
    public Button marcar;
    public Button editar;
    public Button remover;
    public ComboBox<User> hairDresser;
    public Button concluir;
    private ModelManager model;
    private Button[][] gridPaneArray;
    private Date date;
    private Popup stage = new Popup();

    public void setModel(ModelManager model) {
        this.model = model;

        registerHandlers();
        update();
    }
    private void registerHandlers() {
        model.addPropertyChangeListener(RequestsType.GET_WORKERS.toString(), evt -> {
            WorkersResponse response = (WorkersResponse) model.getResponse();
            if (!response.getUsers().isEmpty()) {
                hairDresser.setValue(response.getUsers().get(0));
            }
            model.rUpdateWeeklyCalender(hairDresser.getSelectionModel().getSelectedItem(),model.getFirstDayOfWeek());

            hairDresser.setItems(FXCollections.observableList(response.getUsers()));
        });

        hairDresser.setOnAction(actionEvent -> {
            User user = hairDresser.getSelectionModel().getSelectedItem();
            if (user != null) {
                model.rUpdateWeeklyCalender(user,model.getFirstDayOfWeek());

            }

        });

        model.addPropertyChangeListener(ModelManager.PROP_STATE, evt -> {
            update();

            model.getWorkers();

            gridPaneArray = new Button[8][7];

            for(Node node : gridPane.getChildren())
            {

                if (node instanceof Button button) {
                    button.setOnMouseEntered(mouseEvent -> {
                    });
                    gridPaneArray[GridPane.getRowIndex(button) - 1][GridPane.getColumnIndex(button) - 1] = button;
                }
            }


            date = model.getFirstDayOfWeek();

            if (date != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                dom.setText("DOM " + calendar.get(Calendar.DATE) + "");
                calendar.add(Calendar.DATE,1);
                seg.setText("SEG " + calendar.get(Calendar.DATE) + "");
                calendar.add(Calendar.DATE,1);
                ter.setText("TER " + calendar.get(Calendar.DATE) + "");
                calendar.add(Calendar.DATE,1);
                qua.setText("QUA " + calendar.get(Calendar.DATE) + "");
                calendar.add(Calendar.DATE,1);
                qui.setText("QUI " +calendar.get(Calendar.DATE) + "");
                calendar.add(Calendar.DATE,1);
                sex.setText("SEX " + calendar.get(Calendar.DATE) + "");
                calendar.add(Calendar.DATE,1);
                sab.setText("SAB " + calendar.get(Calendar.DATE) + "");
            }
        });


        model.addPropertyChangeListener(RequestsType.UPDATE_WEEK.toString(), evt -> {
            UpdateWeeklyCalenderResponse response = (UpdateWeeklyCalenderResponse) model.getResponse();

            CellService[][] cellServices = response.getCells();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            for (int i = 0; i < gridPaneArray.length; i++) {
                for (int j = 0; j < gridPaneArray[0].length; j++) {
                    if (cellServices[i][j].getIdService() != 0) {
                        VBox info = new VBox();
                        Label rowLabel = new Label("Id da marcação: " + cellServices[i][j].getIdService());
                        Label number = new Label("Hora inicio: " + cellServices[i][j].getHoraInicio());
                        Label price = new Label("Hora fim: " + cellServices[i][j].getHoraFim());
                        rowLabel.setStyle("-fx-text-fill: white");
                        number.setStyle("-fx-text-fill: white");
                        price.setStyle("-fx-text-fill: white");
                        info.setBackground(Background.fill(Color.rgb(1,1,1,0.8)));
                        info.getChildren().addAll(rowLabel,number,price);
                        info.setPadding(new Insets(10));

                        Button button = gridPaneArray[i][j];
                        Bounds bound = button.localToScene(button.getBoundsInLocal());

                        button.setOnMouseEntered(mouseEvent -> {
                            stage.getContent().clear();
                            stage.getContent().add(info);
                            stage.setX(pane.getScene().getWindow().getX() + bound.getMaxX());
                            stage.setY(pane.getScene().getWindow().getY() + bound.getMaxY());
                            stage.setAutoHide(true);
                            stage.show(pane.getScene().getWindow());
                        });

                        button.setOnMouseExited(mouseEvent -> {
                            stage.getContent().clear();
                        });
                        button.setStyle("-fx-background-color: #ff8c8c; -fx-border-color: #000000;");
                    }
                    else{
                        Button button = gridPaneArray[i][j];
                        button.setStyle("-fx-background-color: #b3ffa4; -fx-border-color: #000000;");
                        for (int m = 0; m < gridPaneArray.length; m++) {
                            for (int n = 0; n < gridPaneArray[0].length; n++) {
                                Button otherButton = gridPaneArray[m][n];
                                if(cellServices[m][n].getIsDayOff() == CellType.DISABLE) {
                                    otherButton.setStyle("-fx-background-color: #d9d9d9; -fx-border-color: #000000;");
                                    otherButton.setDisable(true);
                                }
                            }
                        }
                        button.setDisable(false);
                    }
                }
            }
        });


        btnReturnToMonthlyAgenda.setOnAction(event -> {
            model.homepageTransition();

        });
        marcar.setOnAction(actionEvent ->{
            model.addServiceTransition();
        });
        editar.setOnAction(actionEvent -> {
            model.editServiceTransition();
        });
        remover.setOnAction(actionEvent -> {
            model.removeServiceTransition();
        });
        initializeGridPaneArray();
    }


    private void initializeGridPaneArray()
    {
        this.gridPaneArray = new Button[gridPane.getRowCount()][gridPane.getColumnCount()];
        for(Node node : gridPane.getChildren())
        {
            if (node instanceof Button button) {
                this.gridPaneArray[GridPane.getRowIndex(node)][GridPane.getColumnIndex(node)] = button;
            }
        }
    }

    private void update() {
        pane.setVisible(model != null && model.getState() == State.WEAKLY_AGENDA);
    }
}
