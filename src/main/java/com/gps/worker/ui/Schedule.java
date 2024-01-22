package com.gps.worker.ui;

import com.gps.shared_resources.*;
import com.gps.shared_resources.responses.UpdateTypeServiceResponse;
import com.gps.shared_resources.responses.UpdateWeeklyCalenderResponse;
import com.gps.shared_resources.responses.WorkersResponse;
import com.gps.shared_resources.utils.CellType;
import com.gps.worker.model.ModelManager;
import com.gps.worker.model.fsm.State;
import com.gps.worker.ui.util.ToastMessage;
import javafx.collections.FXCollections;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Schedule {
    public BorderPane pane;
    public Button cancel;

    public Button schedule;
    public Label dom;
    public Label seg;
    public Label ter;
    public Label qua;
    public Label qui;
    public Label sex;
    public Label sab;
    public GridPane gridPane;
    public ChoiceBox<TypeService> typeOfService;
    public TextField txtClientName;
    private Date date;
    private ModelManager model;
    private Button[][] gridPaneArray;
    private final Popup stage = new Popup();


    public void setModel(ModelManager model) {
        this.model = model;
        registerHandlers();
        update();
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(RequestsType.GET_TYPE_OF_SERVICE.toString(), evt -> {
            UpdateTypeServiceResponse response = (UpdateTypeServiceResponse) model.getResponse();
            typeOfService.setItems(FXCollections.observableList(response.getTypeServices()));
            if (!response.getTypeServices().isEmpty()) {
                typeOfService.setValue(response.getTypeServices().get(0));
            }
        });

        model.addPropertyChangeListener(RequestsType.ADD_SERVICE.toString(), evt -> {
            model.rUpdateWeeklyCalender(new User(model.getRequest().getId(), ""), date);
        });

        typeOfService.setOnAction(actionEvent -> {
            for (int z = 0; z < gridPaneArray.length; z++) {
                for (int x = 0; x < gridPaneArray[0].length; x++) {
                    Button buttonSelected = gridPaneArray[z][x];
                    if (buttonSelected.getStyle().contains("00c000")) {
                        buttonSelected.setStyle("-fx-background-color: #b3ffa4; -fx-border-color: #000000;");
                    }
                }
            }
        });

        model.addPropertyChangeListener(ModelManager.PROP_STATE, evt -> {
            update();
            model.rUpdateWeeklyCalender(new User(model.getRequest().getId(), ""),model.getFirstDayOfWeek());
            model.getTypeOfServices();

            gridPaneArray = new Button[8][7];

            for (Node node : gridPane.getChildren()) {
                if (node instanceof Button button) {
                    gridPaneArray[GridPane.getRowIndex(button) - 1][GridPane.getColumnIndex(button) - 1] = button;
                }
            }

            date = model.getFirstDayOfWeek();

            if (date != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                dom.setText("DOM " + calendar.get(Calendar.DATE) + "");
                calendar.add(Calendar.DATE, 1);
                seg.setText("SEG " + calendar.get(Calendar.DATE) + "");
                calendar.add(Calendar.DATE, 1);
                ter.setText("TER " + calendar.get(Calendar.DATE) + "");
                calendar.add(Calendar.DATE, 1);
                qua.setText("QUA " + calendar.get(Calendar.DATE) + "");
                calendar.add(Calendar.DATE, 1);
                qui.setText("QUI " + calendar.get(Calendar.DATE) + "");
                calendar.add(Calendar.DATE, 1);
                sex.setText("SEX " + calendar.get(Calendar.DATE) + "");
                calendar.add(Calendar.DATE, 1);
                sab.setText("SAB " + calendar.get(Calendar.DATE) + "");
            }

        });


        model.addPropertyChangeListener(RequestsType.UPDATE_WEEK.toString(), evt -> {
            try {
                UpdateWeeklyCalenderResponse response = (UpdateWeeklyCalenderResponse) model.getResponse();


            CellService[][] cellServices = response.getCells();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            for (int i = 0; i < gridPaneArray.length; i++) {
                for (int j = 0; j < gridPaneArray[0].length; j++) {
                    Button button = gridPaneArray[i][j];
                    int gridSize = gridPaneArray.length * gridPaneArray[0].length;

                    //If is selected deselect


                    if (cellServices[i][j].getIdService() != 0) {
                        VBox info = new VBox();
                        Label rowLabel = new Label("Id da marcação: " + cellServices[i][j].getIdService());
                        Label number = new Label("Hora inicio: " + cellServices[i][j].getHoraInicio());
                        Label price = new Label("Hora fim: " + cellServices[i][j].getHoraFim());
                        rowLabel.setStyle("-fx-text-fill: white");
                        number.setStyle("-fx-text-fill: white");
                        price.setStyle("-fx-text-fill: white");
                        info.setBackground(Background.fill(Color.rgb(1, 1, 1, 0.8)));
                        info.getChildren().addAll(rowLabel, number, price);
                        info.setPadding(new Insets(10));

                        Bounds bound = button.localToScene(button.getBoundsInLocal());
                        if (button.getStyle().contains("#00c000")) {
                            button.setStyle("-fx-background-color: #b3ffa4; -fx-border-color: #000000;");
                        }
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
                        button.setDisable(true);
                    } else {
                        button.setStyle("-fx-background-color: #b3ffa4; -fx-border-color: #000000;");
                        button.setOnMouseEntered(mouseEvent -> {
                            stage.getContent().clear();
                        });
                        int row = i;
                        int col = j;
                        button.setOnMouseClicked(mouseEvent -> {
                            int duration = typeOfService.getSelectionModel().getSelectedItem().getDuracao();
                            boolean overlapingScheduledSession = false;

                            for (int k = 0; k < duration; k++) {
                                //verifies if there is space to make the selection
                                if (button.getStyle().contains("ff8c8c")) overlapingScheduledSession = true;
                                try {

                                    if (((row + 1) * (col + 1) + ((k) * 7) <= gridSize)) {
                                        Button outOfBoundsTestButton = gridPaneArray[row + k][col];
                                        if (outOfBoundsTestButton.getStyle().contains("#ff8c8c")) {
                                            overlapingScheduledSession = true;
                                        }
                                    } else {
                                        overlapingScheduledSession = true;
                                    }

                                } catch (ArrayIndexOutOfBoundsException e) {
                                    overlapingScheduledSession = true;
                                }
                            }
                            if (!overlapingScheduledSession) { //makes selection accounting for the duration of the service selected
                                for (int m = 0; m < gridPaneArray.length; m++) { //removes other selection
                                    for (int n = 0; n < gridPaneArray[0].length; n++) {
                                        Button otherSelectionButton = gridPaneArray[m][n];
                                        if (otherSelectionButton.getStyle().contains("00c000")) {
                                            otherSelectionButton.setStyle("-fx-background-color: #b3ffa4; -fx-border-color: #000000;");
                                        }
                                    }
                                }
                                for (int k = 0; k < duration; k++) {
                                    Button selectedButton = gridPaneArray[row + k][col];
                                    selectedButton.setStyle("-fx-background-color: #00c000; -fx-border-color: #000000;");
                                }
                            }
                        });
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
            }catch (ClassCastException e){

            }
        });
        cancel.setOnAction(actionEvent -> {
            model.weekChildrenTransition();
        });

        schedule.setOnMouseClicked(mouseEvent -> {
            Calendar calendar = Calendar.getInstance();
            Request request = model.getRequest();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            Date dateStart = null;
            Date dateEnd = null;
            int duration = typeOfService.getSelectionModel().getSelectedItem().getDuracao();

            for (int i = 0; i < gridPaneArray[0].length; i++) { //encontra hora inicial e hora final das horas selecionadas
                for (int j = 0; j < gridPaneArray.length; j++) {
                    Button button = gridPaneArray[j][i];
                    if (button.getStyle().contains("00c000")) {
                        dateStart = calendar.getTime();
                        calendar.setTime(dateStart);
                        for (int k = 0; k < duration; k++) {
                            calendar.add(Calendar.HOUR_OF_DAY, 1);
                        }
                        dateEnd = calendar.getTime();
                        break;
                    }
                    calendar.add(Calendar.HOUR_OF_DAY, 1);
                }
                calendar.add(Calendar.DATE, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 8);
            }

            if (dateStart == null) {
                ToastMessage.show((pane.getScene().getWindow()), "Selecione uma hora para marcar");
                return;
            }
            model.rAddService(new Service(request.getId(), request.getId()
                    , dateStart, dateEnd, List.of(typeOfService.getSelectionModel().getSelectedItem()), txtClientName.getText()));

        });

        model.addPropertyChangeListener(ModelManager.PROP_STATE, evt -> {
            update();
        });

        initializeGridPaneArray();
    }

    private void initializeGridPaneArray() {
        this.gridPaneArray = new Button[gridPane.getRowCount()][gridPane.getColumnCount()];
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Button button) {
                this.gridPaneArray[GridPane.getRowIndex(node)][GridPane.getColumnIndex(node)] = button;
            }
        }
    }

    private void update() {
        pane.setVisible(model != null && model.getState() == State.SCHEDULING);
    }
}
