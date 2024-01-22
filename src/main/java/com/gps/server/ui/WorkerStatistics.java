package com.gps.server.ui;

import com.gps.client.ui.util.ToastMessage;
import com.gps.server.model.fsm.ModelManager;
import com.gps.server.model.fsm.State;
import com.gps.shared_resources.User;
import javafx.collections.FXCollections;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;

import java.util.Map;

public class WorkerStatistics {
    public BorderPane pane;
    public StackedBarChart<String,Integer> stackedBarChart;
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
            model.statisticsTransition(null);
        });
    }

    private void setupStackedBarChart() {
        clearView();
        Map<String, Pair<Integer, Integer>> statistic =  model.getStatistic();
        if (statistic != null) {
            XYChart.Series<String,Integer> doneService = new XYChart.Series<>();
            doneService.setName("Serviços Concluídos");
            XYChart.Series<String,Integer> notDoneService = new XYChart.Series<>();
            notDoneService.setName("Serviços não concluídos");

            for (var item : statistic.entrySet()) {
                String date = item.getKey().substring(0,10);
                notDoneService.getData().add(new XYChart.Data<>(date,item.getValue().getKey()));
                doneService.getData().add(new XYChart.Data<>(date,item.getValue().getValue()));
            }
            stackedBarChart.setAnimated(false);         // fixes label stack problem
            stackedBarChart.getData().addAll(notDoneService,doneService);

        }
    }

    private void update() {
        pane.setVisible(model != null && model.getState() == State.WORKER_STATISTICS);
        setupStackedBarChart();
    }

    private void clearView() {
        stackedBarChart.getData().clear();
    }
}
