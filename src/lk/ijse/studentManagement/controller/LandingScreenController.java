package lk.ijse.studentManagement.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LandingScreenController implements Initializable {
    public JFXButton btnAdmin;
    public JFXButton btnGuest;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void btnAdminOnAction(ActionEvent actionEvent) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/Login.fxml"));
            Stage newStage = new Stage();
            newStage.setScene(new Scene(parent));
            newStage.setTitle("Login");
            newStage.show();
            Stage window = (Stage) this.btnAdmin.getScene().getWindow();
            window.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void btnGuestOnAction(ActionEvent actionEvent) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/GuestMainForm.fxml"));
            Stage newStage = new Stage();
            newStage.setScene(new Scene(parent));
            newStage.setTitle("Student");
            newStage.show();
            Stage window = (Stage) this.btnGuest.getScene().getWindow();
            window.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
