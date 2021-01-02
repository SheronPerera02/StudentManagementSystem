package lk.ijse.studentManagement.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.studentManagement.dto.SubjectDTO;
import lk.ijse.studentManagement.model.ViewSubjectTM;
import lk.ijse.studentManagement.service.SubjectService;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ViewSubjectController implements Initializable {
    public TableView<ViewSubjectTM> tblViewSubject;

    public TableColumn<ViewSubjectTM, String> colId;
    public TableColumn<ViewSubjectTM, String> colSubName;

    public JFXButton btnUpdate;
    public JFXButton btnCancel;

    public TextField txtSubjectId;
    public JFXTextField txtSubjectName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSubName.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        loadAllSubjects();
        tblViewSubject.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ViewSubjectTM>() {
            @Override
            public void changed(ObservableValue<? extends ViewSubjectTM> observable, ViewSubjectTM oldValue, ViewSubjectTM newValue) {
                ViewSubjectTM viewSubjectTM = tblViewSubject.getSelectionModel().getSelectedItem();
                if (viewSubjectTM != null) {
                    txtSubjectName.setDisable(false);
                    txtSubjectId.setText(viewSubjectTM.getId());
                    txtSubjectName.setText(viewSubjectTM.getSubjectName());
                }
            }
        });
    }

    private void loadAllSubjects() {
        ArrayList<ViewSubjectTM> allSubjects = SubjectService.getAllSubjects();
        ObservableList<ViewSubjectTM> items = tblViewSubject.getItems();
        if (allSubjects != null) {
            items.addAll(allSubjects);
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        Alert alert;
        if (txtSubjectId.getText().contentEquals("") && txtSubjectName.getText().contentEquals("")) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Select A Row!");
            alert.showAndWait();
        } else if (txtSubjectName.getText().contentEquals("")) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Complete The Form!");
            alert.showAndWait();
        } else if (detailsNotEligible()) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Details Not Eligible!");
            alert.showAndWait();
        } else {
            alert= new Alert(Alert.AlertType.CONFIRMATION,"You're about to update the corresponding\n" +
                    "Exam, Attendance, Lecture-Schedule records too!");
            alert.showAndWait();


            if(alert.getResult().getText().contentEquals("OK")){
                SubjectDTO dto = new SubjectDTO(
                        txtSubjectId.getText(),
                        txtSubjectName.getText()
                );

                boolean isUpdated = SubjectService.updateSubject(dto);

                if (isUpdated) {

                    ObservableList<ViewSubjectTM> items = tblViewSubject.getItems();
                    items.set(tblViewSubject.getSelectionModel().getSelectedIndex(), new ViewSubjectTM(
                            dto.getId(),
                            dto.getSubjectName()
                    ));

                    btnCancel.fire();
                    alert = new Alert(Alert.AlertType.INFORMATION, "Subject Updated\nSuccessfully!");
                    alert.showAndWait();
                }

            }
        }
    }


    private boolean detailsNotEligible() {
        return !Pattern.matches("[a-zA-Z]{2,30}", txtSubjectName.getText().replaceAll("\\s+", ""));
    }

    public void btnCancelOnAction(ActionEvent actionEvent) {
        tblViewSubject.getSelectionModel().clearSelection();
        txtSubjectName.setDisable(true);
        txtSubjectId.clear();
        txtSubjectName.clear();
    }

}
