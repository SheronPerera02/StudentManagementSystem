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

public class ExamController implements Initializable {
    public Tab addExamTab;
    public Tab examResultsTab;
    public AnchorPane addExamPane;
    public AnchorPane markExamResultsPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void examResultsTabOnSelectionChanged(Event event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/ExamResult.fxml"));
            markExamResultsPane.getChildren().clear();
            markExamResultsPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addExamTabOnSelectionChanged(Event event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/AddExam.fxml"));
            addExamPane.getChildren().clear();
            addExamPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
