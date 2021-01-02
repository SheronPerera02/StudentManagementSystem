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

public class AttendanceController implements Initializable {
    public Tab viewAttendanceTab;
    public Tab markAttendanceTab;

    public AnchorPane viewAttendancePane;
    public AnchorPane markAttendancePane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void viewAttendanceTabOnSelection(Event event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/ViewAttendance.fxml"));
            viewAttendancePane.getChildren().clear();
            viewAttendancePane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void markAttendanceTabOnSelection(Event event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/MarkAttendance.fxml"));
            markAttendancePane.getChildren().clear();
            markAttendancePane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
