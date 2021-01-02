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

public class AcademicController implements Initializable {
    public Tab academicCoursesTab;
    public Tab academicBatchesTab;
    public Tab academicSubjectsTab;

    public AnchorPane academicCoursesPane;
    public AnchorPane academicBatchesPane;
    public AnchorPane academicSubjectsPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void academicCoursesTabOnSelectionChanged(Event event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/Course.fxml"));
            academicCoursesPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void academicBatchesTabOnSelectionChanged(Event event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/Batch.fxml"));
            academicBatchesPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void academicSubjectsTabOnSelectionChanged(Event event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/Subject.fxml"));
            academicSubjectsPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
