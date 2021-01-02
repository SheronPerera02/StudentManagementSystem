package lk.ijse.studentManagement.controller;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class
StudentController implements Initializable {

    public Tab studentViewAllTab;
    public Tab studentRegisterTab;
    public Tab studentUpdateTab;

    public AnchorPane studentViewAllPane;
    public AnchorPane studentRegisterPane;
    public AnchorPane studentUpdatePane;
    public TabPane tabPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tabPane.getSelectionModel().select(studentRegisterTab);
    }

    public void studentViewAllTabOnSelectionChanged(Event event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/StudentViewAll.fxml"));
            studentViewAllPane.getChildren().clear();
            studentViewAllPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void studentRegisterTabOnSelectionChanged(Event event) {

        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/StudentRegister.fxml"));
            studentRegisterPane.getChildren().clear();
            studentRegisterPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void studentUpdateTabOnSelectionChanged(Event event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/StudentUpdate.fxml"));
            studentUpdatePane.getChildren().clear();
            studentUpdatePane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
