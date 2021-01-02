package lk.ijse.studentManagement.controller;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SubjectController implements Initializable {
    public Tab subjectAddTab;
    public Tab subjectViewTab;

    public AnchorPane subjectAddPane;
    public AnchorPane subjectViewPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void subjectAddTabOnAction(Event event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/AddSubject.fxml"));
            subjectAddPane.getChildren().clear();
            subjectAddPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void subjectViewTabOnAction(Event event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/ViewSubject.fxml"));
            subjectViewPane.getChildren().clear();
            subjectViewPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
