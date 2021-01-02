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

public class StudentUpdateController implements Initializable {
    public Tab updateRegistrationTab;
    public Tab updateStudentTab;

    public AnchorPane updateStudentPane;
    public AnchorPane updateRegistrationPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void updateStudentTabOnAction(Event event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/UpdateStudent.fxml"));
            updateStudentPane.getChildren().clear();
            updateStudentPane.getChildren().add(parent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateRegistrationTabOnAction(Event event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/UpdateRegistration.fxml"));
            updateRegistrationPane.getChildren().clear();
            updateRegistrationPane.getChildren().add(parent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
