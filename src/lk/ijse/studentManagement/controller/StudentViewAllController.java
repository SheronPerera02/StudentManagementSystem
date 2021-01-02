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

public class StudentViewAllController implements Initializable {
    public Tab viewAllStudentTab;
    public Tab viewAllRegistrationTab;
    public Tab viewAllPaymentTab;

    public AnchorPane viewAllStudentPane;
    public AnchorPane viewAllRegistrationPane;
    public AnchorPane viewAllPaymentPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void viewAllStudentTabOnAction(Event event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/ViewAllStudent.fxml"));
            viewAllStudentPane.getChildren().clear();
            viewAllStudentPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void viewAllRegistrationTabOnAction(Event event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/ViewAllRegistration.fxml"));
            viewAllRegistrationPane.getChildren().clear();
            viewAllRegistrationPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void viewAllPaymentTabOnAction(Event event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/ViewAllPayment.fxml"));
            viewAllPaymentPane.getChildren().clear();
            viewAllPaymentPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
