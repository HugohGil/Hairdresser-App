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

public class Edit {
    private final Popup stage = new Popup();
    public Button cancel;
    public Button edit;
    public ChoiceBox service;
    public ChoiceBox<TypeService> typeOfService;
    public Label dom;
    public Label seg;
    public Label ter;
    public Label qua;
    public Label qui;
    public Label sex;
    public Label sab;
    public GridPane gridPane;
    public BorderPane pane;
    public TextField txtClientName;
    private Date date;
    private ModelManager model;
    private Button[][] gridPaneArray;
    private int selected = 0;
    private int serviceId = 0;
    private boolean isSelected = false;

    public void setModel(ModelManager model) {
        this.model = model;
        registerHandlers();
        update();
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(RequestsType.GET_TYPE_OF_SERVICE.toString(), evt -> {
            try {
                UpdateTypeServiceResponse response = (UpdateTypeServiceResponse) model.getResponse();
                typeOfService.setItems(FXCollections.observableList(response.getTypeServices()));
                if (!response.getTypeServices().isEmpty()) {
                    typeOfService.setValue(response.getTypeServices().get(0));
                }
            }catch (ClassCastException e){}
        });

        model.addPropertyChangeListener(RequestsType.EDIT_SERVICE.toString(), evt -> {
            model.rUpdateWeeklyCalender(new User(model.getRequest().getId(), ""), date);
        });


        typeOfService.setOnAction(actionEvent -> {
            isSelected = false;
            selected = 0;
            for (int z = 0; z < gridPaneArray.length; z++) {
                for (int x = 0; x < gridPaneArray[0].length; x++) {
                    Button buttonSelected =  gridPaneArray[z][x];
                    if(buttonSelected.getStyle().contains("ffd13a")){
                        buttonSelected.setStyle("-fx-background-color: #ffe17f; -fx-border-color: #000000;");
                    }
                    if(buttonSelected.getStyle().contains("fdd039")){
                        buttonSelected.setStyle("-fx-background-color: #b3ffa4; -fx-border-color: #000000;");
                    }
                    if(buttonSelected.getStyle().contains("d9ad2e")){
                        buttonSelected.setStyle("-fx-background-color: #ffe17f; -fx-border-color: #000000;");
                    }
                }
            }
        });
        model.addPropertyChangeListener(ModelManager.PROP_STATE, evt -> {
            update();
            model.rUpdateWeeklyCalender(new User(model.getRequest().getId(), ""),model.getFirstDayOfWeek());
            model.getWorkers();

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
            try{
                UpdateWeeklyCalenderResponse response = (UpdateWeeklyCalenderResponse) model.getResponse();
                Request request = model.getRequest();
                CellService[][] cellServices = response.getCells();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.set(Calendar.HOUR_OF_DAY, 8);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                for (int i = 0; i < gridPaneArray.length; i++) {
                    for (int j = 0; j < gridPaneArray[0].length; j++) {
                        int gridSize = gridPaneArray.length * gridPaneArray[0].length;

                        //If is selected deselect
                    /*if (button.getStyle().contains("#00c000")) {
                        button.setStyle("-fx-background-color: #b3ffa4; -fx-border-color: #000000;");
                    }*/

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
                            Button button = gridPaneArray[i][j];
                            button.setStyle("-fx-background-color: #b3ffa4; -fx-border-color: #000000;");
                            button.setDisable(false);
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
                            if (request.getId() == cellServices[i][j].getIdUser())
                                button.setStyle("-fx-background-color: #ffe17f; -fx-border-color: #000000;");
                            else {
                                button.setStyle("-fx-background-color: #ff8c8c; -fx-border-color: #000000;");
                                button.setDisable(true);
                            }

                            int finalI = i;
                            int finalJ = j;
                            button.setOnMouseClicked(mouseEvent -> {
                                boolean overlapingScheduledSession = false;
                                int duration = typeOfService.getSelectionModel().getSelectedItem().getDuracao();
                                for (int k = 0; k < duration; k++) {
                                    //verifies if there is space to make the selection
                                    if (button.getStyle().contains("ff8c8c")) overlapingScheduledSession = true;
                                    try {
                                        if (((finalI+ 1) * (finalJ + 1) + ((k) * 7) <= gridSize)) {

                                        } else {
                                            overlapingScheduledSession = true;
                                        }
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        overlapingScheduledSession = true;
                                    }
                                }
                                if (!overlapingScheduledSession) {
                                    if (button.getStyle().contains("ffd13a")) {
                                        for (int m = 0; m < gridPaneArray.length; m++) { //removes other selection
                                            for (int n = 0; n < gridPaneArray[0].length; n++) {
                                                Button otherSelectionButton = gridPaneArray[m][n];
                                                if (otherSelectionButton.getStyle().contains("d9ad2e")) {
                                                    otherSelectionButton.setStyle("-fx-background-color: #ffd13a; -fx-border-color: #000000;");
                                                    selected--;
                                                }
                                                if (otherSelectionButton.getStyle().contains("fdd039")) {
                                                    selected--;
                                                    otherSelectionButton.setStyle("-fx-background-color: #b3ffa4; -fx-border-color: #000000;");
                                                }
                                            }
                                        }
                                        //int duration = typeOfService.getSelectionModel().getSelectedItem().getDuracao();
                                        for (int k = 0; k < duration; k++) {
                                            Button selectedButton = gridPaneArray[finalI + k][finalJ];
                                            if (selectedButton.getStyle().contains("ffd13a")) {
                                                selected++;
                                                selectedButton.setStyle("-fx-background-color: #d9ad2e; -fx-border-color: #000000;");
                                            }
                                            if (selectedButton.getStyle().contains("b3ffa4")) {
                                                selected++;
                                                selectedButton.setStyle("-fx-background-color: #fdd039; -fx-border-color: #000000;");
                                            }
                                        }
                                    }
                                    if (button.getStyle().contains("ffe17f")) {
                                        isSelected = false;
                                        //int duration = typeOfService.getSelectionModel().getSelectedItem().getDuracao();

                                        for (int m = 0; m < gridPaneArray.length; m++) { //removes other selection
                                            for (int n = 0; n < gridPaneArray[0].length; n++) {
                                                Button otherSelectionButton = gridPaneArray[m][n];
                                                if (otherSelectionButton.getStyle().contains("ffd13a")) {
                                                    otherSelectionButton.setStyle("-fx-background-color: #ffe17f; -fx-border-color: #000000;");
                                                }
                                            }
                                        }
                                        for (int m = 0; m < gridPaneArray.length; m++) {
                                            for (int n = 0; n < gridPaneArray[0].length; n++) {
                                                Button otherSelectionButton = gridPaneArray[m][n];
                                                if (otherSelectionButton.getStyle().contains("fdd039")) {
                                                    otherSelectionButton.setStyle("-fx-background-color: #b3ffa4; -fx-border-color: #000000;");
                                                    selected--;
                                                }
                                                if (otherSelectionButton.getStyle().contains("d9ad2e")) {
                                                    otherSelectionButton.setStyle("-fx-background-color: #ffe17f; -fx-border-color: #000000;");
                                                    selected--;
                                                }
                                            }
                                        }
                                        isSelected = true;
                                        serviceId = cellServices[finalI][finalJ].getIdService();
                                        for (int h = 0; h < gridPaneArray.length; h++) {
                                            for (int k = 0; k < gridPaneArray[0].length; k++) {
                                                Button otherSelectionButton = gridPaneArray[h][k];
                                                if (cellServices[h][k] != null) {
                                                    if (cellServices[h][k].getIdService() == serviceId)
                                                        otherSelectionButton.setStyle("-fx-background-color: #ffd13a; -fx-border-color: #000000;");
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                        } else {
                            Button button = gridPaneArray[i][j];
                            button.setOnMouseEntered(mouseEvent -> {
                                stage.getContent().clear();
                            });
                            button.setStyle("-fx-background-color: #b3ffa4; -fx-border-color: #000000;");
                            int row = i;
                            int col = j;
                            button.setOnMouseClicked(mouseEvent -> {
                                int duration = typeOfService.getSelectionModel().getSelectedItem().getDuracao();
                                boolean overlapingScheduledSession = false;
                                if (isSelected) {
                                    for (int k = 0; k < duration; k++) {
                                        //verifies if there is space to make the selection
                                        if (button.getStyle().contains("ff8c8c")) overlapingScheduledSession = true;
                                        try {
                                            if (((row + 1) * (col + 1) + ((k) * 7) <= gridSize)) {
                                                Button outOfBoundsTestButton = gridPaneArray[row + k][col];
                                                if (outOfBoundsTestButton.getStyle().contains("#ff8c8c")) {
                                                    overlapingScheduledSession = true;
                                                }
                                                if (outOfBoundsTestButton.getStyle().contains("#ffe17f")) {
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
                                                if (otherSelectionButton.getStyle().contains("fdd039")) {
                                                    selected--;
                                                    otherSelectionButton.setStyle("-fx-background-color: #b3ffa4; -fx-border-color: #000000;");
                                                }
                                                if (otherSelectionButton.getStyle().contains("d9ad2e")) {
                                                    selected--;
                                                    otherSelectionButton.setStyle("-fx-background-color: #ffd13a; -fx-border-color: #000000;");
                                                }
                                            }
                                        }
                                        for (int k = 0; k < duration; k++) {
                                            Button selectedButton = gridPaneArray[row + k][col];
                                            if (selectedButton.getStyle().contains("b3ffa4")) {
                                                selected++;
                                                selectedButton.setStyle("-fx-background-color: #fdd039; -fx-border-color: #000000;");    // same color different code for logic purposes
                                            }
                                            if (selectedButton.getStyle().contains("ffd13a")) {
                                                selected++;
                                                selectedButton.setStyle("-fx-background-color: #d9ad2e; -fx-border-color: #000000;");    // color for overlapping
                                            }
                                        }
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
            }
            catch (ClassCastException e){}
        });
        cancel.setOnAction(actionEvent -> {
            selected = 0;
            isSelected = false;
            stage.getContent().clear();
            model.weekChildrenTransition();
        });
        edit.setOnMouseClicked(mouseEvent -> {
            Calendar calendar = Calendar.getInstance();
            Request request = model.getRequest();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            Date date1 = null;
            Date date2 = null;
            int duration = typeOfService.getSelectionModel().getSelectedItem().getDuracao();

            for (int i = 0; i < gridPaneArray[0].length; i++) {
                calendar.set(Calendar.HOUR_OF_DAY, 8);
                for (Button[] buttons : gridPaneArray) {
                    Button button = buttons[i];
                    if (selected > 0) {
                        if (button.getStyle().contains("fdd039") || button.getStyle().contains("d9ad2e")) {
                            date1 = calendar.getTime();
                            calendar.setTime(date1);
                            for(int k = 0; k < duration; k++){
                                calendar.add(Calendar.HOUR_OF_DAY,1);
                            }
                            date2 = calendar.getTime();
                            break;
                        }
                    } else {
                        if (button.getStyle().contains("ffd13a")) {
                            date1 = calendar.getTime();
                            calendar.setTime(date1);
                            for(int k = 0; k < duration; k++){
                                calendar.add(Calendar.HOUR_OF_DAY,1);
                            }
                            date2 = calendar.getTime();
                            break;
                        }
                    }
                    calendar.add(Calendar.HOUR_OF_DAY, 1);
                }
                calendar.add(Calendar.DATE, 1);
            }
            if(date1 == null) {
                ToastMessage.show((pane.getScene().getWindow()), "Selecione um serviço para editar");
                selected = 0;
                isSelected = false;
                return;
            }
            model.rEditService(new Service(request.getId(), request.getId(), date1, date2,
                    List.of(typeOfService.getSelectionModel().getSelectedItem()), txtClientName.getText()), serviceId);


            selected = 0;
            isSelected = false;
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
        pane.setVisible(model != null && model.getState() == State.EDIT_SERVICE);
    }
}
