package com.headstrongpro.desktop.view.payments;

import com.headstrongpro.desktop.controller.PaymentsController;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.Payment;
import com.headstrongpro.desktop.view.ContentView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

import static javafx.concurrent.Worker.State.*;

/**
 * Payments ContentView
 */
public class PaymentsContentView extends ContentView<Payment> implements Initializable {

    @FXML
    public Text paymentsHeader;
    @FXML
    public TextField searchPaymentsTextfield;
    @FXML
    public TableView<Payment> paymentsTable;
    @FXML
    public TableColumn<Payment, Double> valCol;
    @FXML
    public TableColumn<Payment, Date> dueCol;
    @FXML
    public TableColumn<Payment, Date> payCol;
    @FXML
    public TableColumn<Payment, String> companyNameCol;
    @FXML
    public TableColumn<Payment, String> cvrCol;

    private PaymentsController controller;
    private ObservableList<Payment> payments;
    private Payment selected;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controller = new PaymentsController();
        payments = FXCollections.emptyObservableList();

        setColumns();

        Task<Void> init = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> footer.show("Initializing payments", Footer.NotificationType.LOADING));
                loadPayments();
                return null;
            }
        };

        init.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.equals(SUCCEEDED)) {
                loadTable(payments);
                footer.show("Payments loaded", Footer.NotificationType.COMPLETED);
            } else if (newValue.equals(FAILED) || newValue.equals(CANCELLED)) {
                footer.show("Error! Payments could not be loaded!", Footer.NotificationType.ERROR, Footer.FADE_LONG);
            }
        }));

        new Thread(init).start();

        paymentsTable.getSelectionModel()
                .selectedItemProperty()
                .addListener(((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        selected = newValue;
                        mainWindowView.getContextView().changeContextItem(selected);
                    }
                }));
    }

    private void loadPayments() {
        try {
            payments = controller.getAll();
        } catch (ModelSyncException e) {
            e.printStackTrace();
            footer.show(e.getMessage(), Footer.NotificationType.ERROR, Footer.FADE_QUICK);
        }
    }

    private void setColumns() {
        valCol.setCellValueFactory(new PropertyValueFactory<>("value"));
        dueCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        payCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        companyNameCol.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        cvrCol.setCellValueFactory(new PropertyValueFactory<>("companyCvr"));
    }

    @FXML
    public void searchPaymentsOnKeyReleased() {
        loadTable(controller.searchByPhrase(searchPaymentsTextfield.getText()));
    }

    @FXML
    public void refreshButtonOnClick() {
        searchPaymentsTextfield.clear();
        Task<Void> sync = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> footer.show("Synchronising data...", Footer.NotificationType.LOADING));
                loadPayments();
                return null;
            }
        };

        sync.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.equals(SUCCEEDED)) {
                loadTable(payments);
                footer.show("Payments reloaded successfully", Footer.NotificationType.COMPLETED);
            } else if (newValue.equals(CANCELLED) || newValue.equals(FAILED)) {
                footer.show("Coult not reload payments!", Footer.NotificationType.ERROR, Footer.FADE_LONG);
            }
        }));

        new Thread(sync).start();
    }

}
