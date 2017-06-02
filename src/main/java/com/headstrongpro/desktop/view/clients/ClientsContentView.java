package com.headstrongpro.desktop.view.clients;

import com.headstrongpro.desktop.controller.ClientsController;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.entity.Person;
import com.headstrongpro.desktop.view.ContentView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.concurrent.Worker.State.CANCELLED;
import static javafx.concurrent.Worker.State.FAILED;
import static javafx.concurrent.Worker.State.SUCCEEDED;

/**
 * Created by Ondřej Soukup on 23.05.2017.
 */
public class ClientsContentView extends ContentView implements Initializable {

    @FXML
    public TextField searchClientsTextfield;
    @FXML
    public TableView<Person> clientsTable;

    @FXML
    public TableColumn<Person, Integer> clientCompanyCol;
    @FXML
    public TableColumn<Person, String> clientNameCol, clientEmailCol, clientPhoneCol, clientGenderCol, clientLoginCol, clientPassCol, clientDateCol;
    @FXML
    public Button newClientButton;
    @FXML
    public Text clientsHeader;
    @FXML
    public Button assignMoreButton;

    private ClientsController clientsController;
    private ObservableList<Person> clients;

    private void loadClients() {
        try {
            clients = clientsController.getClients();
        } catch (ModelSyncException e) {
            e.printStackTrace();
            //TODO: pls handle with care <3
        }
    }

    private void loadTable(ObservableList<Person> clients) {
        clientsTable.getColumns().removeAll(clientNameCol, clientEmailCol, clientPhoneCol, clientGenderCol);
        clientsTable.setItems(clients);
        clientNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        clientEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        clientPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        clientGenderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        //clientLoginCol.setCellValueFactory(new PropertyValueFactory<>("login"));
        //clientPassCol.setCellValueFactory(new PropertyValueFactory<>("pass"));
        //clientDateCol.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        //clientCompanyCol.setCellValueFactory(new PropertyValueFactory<>("companyId"));
        clientsTable.getColumns().addAll(clientNameCol, clientEmailCol, clientPhoneCol, clientGenderCol);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clients = FXCollections.observableArrayList();
        Task<Void> init = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> footer.show("Loading clients...", Footer.NotificationType.LOADING));
                clientsController = new ClientsController();
                loadClients();
                return null;
            }
        };

        init.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.equals(SUCCEEDED)) {
                loadTable(clients);
                footer.show("Clients loaded successfully!", Footer.NotificationType.COMPLETED);
            } else if (newValue.equals(FAILED) || newValue.equals(CANCELLED)) {
                footer.show("Error while loading clients!", Footer.NotificationType.ERROR, Footer.FADE_LONG);
            }
        }));

        clientsTable.getSelectionModel().selectedItemProperty().addListener((o, e, c) -> {
            if (c != null) {
                footer.show(c.getName() + " selected.", Footer.NotificationType.INFORMATION, Footer.FADE_SUPER_QUICK);
                mainWindowView.getContextView().changeContextItem(c);
            }
        });

        Thread th = new Thread(init);
        th.setDaemon(true);
        th.start();
    }

    @FXML
    public void clientSearch() {
        try {
            loadTable(FXCollections.observableArrayList(clientsController.search(searchClientsTextfield.getText())));
        } catch (ModelSyncException e) {
            e.printStackTrace();
            //TODO: handle dis with care too~
        }
    }

    @FXML
    public void clientsRefreshButtonOnClick() {
        searchClientsTextfield.clear();
        //TODO protected bullshit context clearing stufff

        Task<Void> sync = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> footer.show("Synchronising data...", Footer.NotificationType.LOADING));
                loadClients();
                return null;
            }
        };

        sync.stateProperty().addListener((q, w, e) -> {
            if (e.equals(SUCCEEDED)) {
                loadTable(clients);
                footer.show("Clients reloaded successfully!", Footer.NotificationType.COMPLETED, Footer.FADE_NORMAL);
            } else if (e.equals(FAILED) || e.equals(CANCELLED)) {
                footer.show("Error while loading clients!", Footer.NotificationType.ERROR, Footer.FADE_LONG);
            }
        });

        Thread th = new Thread(sync);
        th.setDaemon(true);
        th.start();
    }



}