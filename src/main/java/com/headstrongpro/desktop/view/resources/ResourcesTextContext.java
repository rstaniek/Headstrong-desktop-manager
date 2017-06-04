package com.headstrongpro.desktop.view.resources;

import com.headstrongpro.desktop.controller.ResourcesController;
import com.headstrongpro.desktop.core.SyncHandler;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.resource.Resource;
import com.headstrongpro.desktop.model.resource.TextResource;
import com.headstrongpro.desktop.view.ContextView;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by jakub on 26/05/2017.
 */
public class ResourcesTextContext extends ContextView<TextResource> implements Initializable {

    @FXML
    public TextField textResourcesNameTextfield;
    @FXML
    public HTMLEditor textResourcesEditor;
    @FXML
    public WebView textResourcesPreviewWeb;

    private ResourcesController controller;
    private SyncHandler<TextResource> syncHandler = () -> {
        try {
            return Resource.ofType(controller.getResourceById(contextItem.getId()));
        } catch (ModelSyncException e1) {
            e1.printStackTrace();
            mainWindowView.getContentView().footer.show(e1.getMessage(), Footer.NotificationType.ERROR);
        }
        return null;
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controller = new ResourcesController();
    }

    @FXML
    public void editButtonOnClick() {
        if (validateInput(textResourcesNameTextfield)) {
            contextItem.setName(contextItem.getName());
            contextItem.setContent(textResourcesEditor.getHtmlText());
            try {
                mainWindowView.getContentView().footer.show("Updating " + contextItem.getName() + "...", Footer.NotificationType.LOADING);
                controller.editResource(contextItem);
                mainWindowView.getContentView().footer.show("Resource updated.", Footer.NotificationType.COMPLETED, Footer.FADE_QUICK);
                mainWindowView.getContentView().refreshButton.fire();
            } catch (ModelSyncException e) {
                e.printStackTrace();
                mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR, Footer.FADE_LONG);
            } catch (DatabaseOutOfSyncException e) {
                e.printStackTrace();
                handleOutOfSync(syncHandler);
            }
        } else {
            mainWindowView.getContentView().footer.show("Values are invalid!", Footer.NotificationType.WARNING);
        }
    }

    @FXML
    public void deleteButtonOnClick() {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText("Are you sure you want to delete " + contextItem.getName() + "?");
        a.setContentText("You cannot take that action back");
        Optional<ButtonType> response = a.showAndWait();
        response.ifPresent(btn -> {
            if (ButtonType.OK.equals(btn)) {
                mainWindowView.getContentView().footer.show("Deleting " + contextItem.getName() + "...", Footer.NotificationType.LOADING);
                try {
                    controller.delete(contextItem);
                    mainWindowView.getContentView().footer.show("Resource deleted.", Footer.NotificationType.COMPLETED);
                    mainWindowView.getContentView().refreshButton.fire();
                } catch (DatabaseOutOfSyncException e) {
                    e.printStackTrace();
                    handleOutOfSync(syncHandler);
                } catch (ModelSyncException e) {
                    mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR, Footer.FADE_LONG);
                }
            }
        });
    }

    @Override
    public void populateForm() {
        textResourcesNameTextfield.setText(contextItem.getName());
        textResourcesEditor.setHtmlText(contextItem.getContent());

        Task<String> loadTextContent = new Task<String>() {
            @Override
            protected String call() throws Exception {
                return contextItem.getContent();
            }
        };

        loadTextContent.valueProperty().addListener((q, w, e) -> {
            if (e != null) {
                textResourcesEditor.setHtmlText(e);
            }
        });

        new Thread(loadTextContent).start();
    }

    @Override
    protected void clearFields() {
        textResourcesNameTextfield.clear();
    }

    @FXML
    public void editorOnKeyReleased(KeyEvent keyEvent) {
        System.out.println("editorOnKeyReleased");
        sendToPreview();
    }

    private void sendToPreview() {
        String html = textResourcesEditor.getHtmlText();
        textResourcesPreviewWeb.getEngine().load(html); //TODO: for some reason doesn'data work
    }

    @FXML
    public void editorOnMouseClicked(MouseEvent mouseEvent) {
        System.out.println("editorOnMouseClicked");
        sendToPreview();
    }
}
