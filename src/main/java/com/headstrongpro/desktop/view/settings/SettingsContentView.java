package com.headstrongpro.desktop.view.settings;

import com.headstrongpro.desktop.controller.LogsController;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.Log;
import com.headstrongpro.desktop.view.ContentView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

/**
 * Settings ContentView
 */
public class SettingsContentView extends ContentView<Log> implements Initializable {

    // Table columns
    @FXML
    public TableColumn<Log, Integer> empIdCol, itemIdCol;
    @FXML
    public TableColumn<Log, String> tableCol, actionCol;
    @FXML
    public TableColumn<Log, Date> timeCol;

    private ObservableList<Log> logs;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logs = FXCollections.emptyObservableList();

        setColumns();

        Task<Void> initLogs = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> footer.show("Loading logs...", Footer.NotificationType.LOADING));
                loadLogs();
                return null;
            }
        };

        initLogs.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.equals(Worker.State.SUCCEEDED)) {
                loadTable(logs);
                footer.show("Logs loaded.", Footer.NotificationType.COMPLETED);
            } else if (newValue.equals(Worker.State.FAILED) || newValue.equals(Worker.State.CANCELLED)) {
                footer.show("Loading failed!", Footer.NotificationType.ERROR, Footer.FADE_LONG);
            }
        }));

        new Thread(initLogs).start();
    }

    @FXML
    public void handleSearch() {
    }

    @FXML
    public void refreshOnClick() {
    }

    private void setColumns() {
        empIdCol.setCellValueFactory(new PropertyValueFactory<>("headstrongEmpID"));
        tableCol.setCellValueFactory(new PropertyValueFactory<>("tableName"));
        itemIdCol.setCellValueFactory(new PropertyValueFactory<>("itemID"));
        actionCol.setCellValueFactory(new PropertyValueFactory<>("actionType"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    private void loadLogs() {
        try {
            logs = new LogsController().getAll();
        } catch (ModelSyncException e) {
            e.printStackTrace();
            footer.show(e.getMessage(), Footer.NotificationType.ERROR, Footer.FADE_LONG);
        }
    }
}
