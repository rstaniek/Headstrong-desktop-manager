package com.headstrongpro.desktop.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Ondřej Soukup on 23.05.2017.
 */
public class CoursesView implements Initializable {

    @FXML
    public Text coursesHeader;
    @FXML
    public TextField searchCoursesTextfield;
    @FXML
    public TableView coursesTable;
    @FXML
    public Button newCourseButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
