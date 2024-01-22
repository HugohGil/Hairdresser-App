package com.gps.worker.ui;

import com.gps.shared_resources.CellService;
import com.gps.shared_resources.RequestsType;
import com.gps.shared_resources.User;
import com.gps.shared_resources.responses.UpdateUserWeeklyCalendarResponse;
import com.gps.shared_resources.responses.UpdateWeeklyCalenderResponse;
import com.gps.worker.model.ModelManager;
import com.gps.worker.model.fsm.State;
import com.gps.worker.ui.util.ToastMessage;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;

import java.util.Calendar;
import java.util.Date;

public class Conclude {
    public Button conclude;
    public Button cancel;
    public Label dom;
    public Label seg;
    public Label ter;
    public Label qua;
    public Label qui;
    public Label sex;
    public Label sab;
    public GridPane gridPane;
    public BorderPane pane;
    private Button[][] gridPaneArray;
    private CellService[][] cellServicesArray;
    private Date date;
    private ModelManager model;
    private Popup stage = new Popup();

    public void setModel(ModelManager model) {
        this.model = model;
        registerHandlers();
        update();
    }
    private void registerHandlers() {
        model.addPropertyChangeListener(ModelManager.PROP_STATE, evt -> {
            update();
            model.rUpdateWeeklyCalender(new User(model.getRequest().getId(), ""),model.getFirstDayOfWeek());
            gridPaneArray = new Button[8][7];

            for(Node node : gridPane.getChildren())
            {
                if (node instanceof Button button) {
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
            model.rUpdateWeeklyCalender(model.getUser(),model.getFirstDayOfWeek());
        });
        model.addPropertyChangeListener(RequestsType.UPDATE_WEEK.toString(), evt -> {
            try{
                UpdateWeeklyCalenderResponse response = (UpdateWeeklyCalenderResponse) model.getResponse();

                CellService[][] cellServices = response.getCells();
                cellServicesArray = cellServices;

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

                            if(cellServices[i][j].getIsConcluded() == 1){
                                button.setStyle("-fx-background-color: #ffe17f; -fx-border-color: #000000;");
                            } else
                                button.setStyle("-fx-background-color: #ff8c8c; -fx-border-color: #000000;");

                            int idCell = cellServices[i][j].getIdService();
                            button.setOnMouseClicked(mouserEvent ->{
                                if(button.getStyle().contains("#ff3c3c")){
                                    button.setStyle("-fx-background-color: #ff8c8c; -fx-border-color: #000000;");
                                    for (int h = 0; h < gridPaneArray.length; h++) {
                                        for (int k = 0; k < gridPaneArray[0].length; k++){
                                            Button buttonCompare = gridPaneArray[h][k];
                                            if(cellServices[h][k].getIdService() != 0){
                                                if(cellServices[h][k].getIdService() == idCell){
                                                    buttonCompare.setStyle("-fx-background-color: #ff8c8c; -fx-border-color: #000000;");
                                                }
                                            }

                                        }
                                    }
                                }
                                else if(button.getStyle().contains("#ff8c8c")){
                                    button.setStyle("-fx-background-color: #ff3c3c; -fx-border-color: #000000;");
                                    for (int h = 0; h < gridPaneArray.length; h++) {
                                        for (int k = 0; k < gridPaneArray[0].length; k++){
                                            Button buttonCompare = gridPaneArray[h][k];
                                            if(cellServices[h][k].getIdService() != 0){
                                                if(cellServices[h][k].getIdService() == idCell){
                                                    buttonCompare.setStyle("-fx-background-color: #ff3c3c; -fx-border-color: #000000;");
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                        }
                        else{
                            Button button = gridPaneArray[i][j];
                            button.setOnMouseEntered(mouseEvent -> {
                                stage.getContent().clear();
                            });
                            button.setStyle("-fx-background-color: #b3ffa4; -fx-border-color: #000000;");
                        }
                    }
                }
            }
            catch (ClassCastException e){}
            

        });
        conclude.setOnAction(actionEvent -> {
            int vef = 0;
            for (int i = 0; i < gridPaneArray.length; i++) {
                for (int j = 0; j < gridPaneArray[0].length; j++) {
                    if (cellServicesArray[i][j].getIdService() != 0) {
                        Button button = gridPaneArray[i][j];
                        if(button.getStyle().contains("#ff3c3c")){
                            model.rConcludeService(cellServicesArray[i][j].getIdService());
                            vef = 1;
                            break;
                        }
                    }
                }
                if(vef == 1){
                    return;
                }
            }
            ToastMessage.show((pane.getScene().getWindow()), "Selecione um serviço para concluir");
        });
        cancel.setOnAction(actionEvent -> {
            model.weekChildrenTransition();
        });

        model.addPropertyChangeListener(String.valueOf(RequestsType.CONCLUDE_SERVICE), evt ->{
            model.rUpdateWeeklyCalender(model.getUser(),model.getFirstDayOfWeek());
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
        pane.setVisible(model != null && model.getState() == State.CONCLUDE_SERVICE);
    }
}

