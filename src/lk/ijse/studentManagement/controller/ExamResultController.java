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

public class ExamResultController implements Initializable {
    public Tab viewResultsTab;
    public Tab markResultsTab;
    
    public AnchorPane viewResultsPane;
    public AnchorPane markResultsPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void viewResultsTabOnSelectionChanged(Event event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/ViewResult.fxml"));
            viewResultsPane.getChildren().clear();
            viewResultsPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void markResultsTabOnSelectionChanged(Event event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/MarkResult.fxml"));
            markResultsPane.getChildren().clear();
            markResultsPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
