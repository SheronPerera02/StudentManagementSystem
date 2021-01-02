package lk.ijse.studentManagement.controller;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminMainFormController implements Initializable {
    public AnchorPane rootAnchorPane;
    public AnchorPane settingsPane;

    public JFXButton btnStudent;
    public JFXButton btnDashboard;
    public JFXButton btnAcademic;
    public JFXButton btnLecturer;
    public JFXButton btnAdminAccount;
    public JFXButton btnLogOut;
    public JFXButton btnExam;
    public JFXButton btnAttendance;
    public JFXButton btnLectureSchedule;

    public Label lblUsername;

    public FontAwesomeIconView settingsIcon;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/AdminDashboard.fxml"));
            rootAnchorPane.getChildren().add(parent);
            String username = LoginController.dashBoardDisplayName;
            lblUsername.setText(username == null ? "" : username);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (lblUsername.getText().contentEquals("Main")) {
            btnAdminAccount.setDisable(false);
        }
    }


    public void btnStudentOnAction(ActionEvent actionEvent) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/Student.fxml"));
            rootAnchorPane.getChildren().clear();
            rootAnchorPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnDashboardOnAction(ActionEvent actionEvent) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/AdminDashboard.fxml"));
            rootAnchorPane.getChildren().clear();
            rootAnchorPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnAcademicOnAction(ActionEvent actionEvent) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/Academic.fxml"));
            rootAnchorPane.getChildren().clear();
            rootAnchorPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnLecturerOnAction(ActionEvent actionEvent) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/Lecturer.fxml"));
            rootAnchorPane.getChildren().clear();
            rootAnchorPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void settingsIconOnMouseClicked(MouseEvent mouseEvent) {
        if (settingsPane.isVisible()) {
            settingsPane.setVisible(false);
        } else {
            settingsPane.setVisible(true);
        }
    }

    public void btnAdminAccountOnAction(ActionEvent actionEvent) {
        settingsPane.setVisible(false);
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/AdminAccount.fxml"));
            rootAnchorPane.getChildren().clear();
            rootAnchorPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnLogOutOnAction(ActionEvent actionEvent) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/LandingScreen.fxml"));
            Stage newStage = new Stage();
            newStage.setScene(new Scene(parent));

            Stage window = (Stage) this.btnLogOut.getScene().getWindow();
            window.close();

            newStage.setTitle("Student Management");
            newStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnExamOnAction(ActionEvent actionEvent) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/Exam.fxml"));
            rootAnchorPane.getChildren().clear();
            rootAnchorPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnAttendanceOnAction(ActionEvent actionEvent) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/Attendance.fxml"));
            rootAnchorPane.getChildren().clear();
            rootAnchorPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnLectureScheduleOnAction(ActionEvent actionEvent) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/LectureSchedule.fxml"));
            rootAnchorPane.getChildren().clear();
            rootAnchorPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
