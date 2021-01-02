package lk.ijse.studentManagement.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lk.ijse.studentManagement.service.LoginService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public JFXTextField txtUserName;
    public JFXPasswordField txtPassWord;
    
    public JFXButton btnLogin;
    public JFXButton btnCancel;
    public JFXButton btnPrevious;
    public static String dashBoardDisplayName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void btnCancelOnAction(ActionEvent actionEvent) {
        txtUserName.clear();
        txtPassWord.clear();
        txtUserName.requestFocus();
    }

    public void btnPreviousOnAction(ActionEvent actionEvent) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/LandingScreen.fxml"));
            Stage newStage = new Stage();
            newStage.setScene(new Scene(parent));
            newStage.setTitle("Student Management");
            newStage.show();

            Stage window = (Stage)this.btnPrevious.getScene().getWindow();
            window.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void txtUserNameOnAction(ActionEvent actionEvent) {
        txtPassWord.requestFocus();
    }

    public void btnLoginOnAction(ActionEvent actionEvent) {
        Alert alert;
        String username = txtUserName.getText();
        String password = txtPassWord.getText();
        if(
                username.contentEquals("")
                        ||
                        password.contentEquals("")
        ){

            alert= new Alert(Alert.AlertType.INFORMATION,"Complete The Form!");
            alert.showAndWait();

        } else if(LoginService.userNameDoesNotExist(username)){
            alert= new Alert(Alert.AlertType.INFORMATION,"Username Does Not Exist!");
            alert.showAndWait();
        } else if(LoginService.accessGranted(username,password)){
            try {
                dashBoardDisplayName=username;
                Parent parent = FXMLLoader.load(getClass().
                        getResource("/lk/ijse/studentManagement/views/AdminMainForm.fxml"));
                Stage newStage = new Stage();
                Scene scene = new Scene(parent);
                newStage.setScene(scene);
                Stage stage = (Stage)btnLogin.getScene().getWindow();
                stage.close();
                newStage.setTitle("Student Management");
                newStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else{
            alert=new Alert(Alert.AlertType.INFORMATION,"Wrong Password!");
            alert.showAndWait();
            txtPassWord.clear();
        }
    }

    public void txtPassWordOnAction(ActionEvent actionEvent) {
        btnLogin.fire();
    }
}
