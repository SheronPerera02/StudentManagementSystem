package lk.ijse.studentManagement.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import lk.ijse.studentManagement.dto.RegistrationDTO;
import lk.ijse.studentManagement.model.ViewAllRegistrationTM;
import lk.ijse.studentManagement.service.BatchService;
import lk.ijse.studentManagement.service.StudentRegistrationService;
import lk.ijse.studentManagement.service.StudentUpdateService;
import lk.ijse.studentManagement.service.StudentViewAllService;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class UpdateRegistrationController implements Initializable {
    public TableView<ViewAllRegistrationTM> tblUpdateRegistration;

    public TableColumn<ViewAllRegistrationTM, String> colId;
    public TableColumn<ViewAllRegistrationTM, String> colStudentId;
    public TableColumn<ViewAllRegistrationTM, String> colStudentName;
    public TableColumn<ViewAllRegistrationTM, String> colCourse;
    public TableColumn<ViewAllRegistrationTM, String> colBatch;
    public TableColumn<ViewAllRegistrationTM, String> colRegDate;
    public TableColumn<ViewAllRegistrationTM, Double> colFee;

    public TextField txtStudentId;
    public TextField txtCourse;
    public TextField txtRegDate;
    public TextField txtFee;
    public TextField txtAdditionalFee;
    public TextField txtRegId;
    public TextField txtSearch;

    public JFXComboBox<String> cmbBatch;
    public JFXComboBox<String> cmbSelectField;

    public JFXButton btnCancel;
    public JFXButton btnUpdate;

    public Label lblAdditionalFee;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colStudentId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colStudentName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        colCourse.setCellValueFactory(new PropertyValueFactory<>("course"));
        colBatch.setCellValueFactory(new PropertyValueFactory<>("batch"));
        colRegDate.setCellValueFactory(new PropertyValueFactory<>("regDate"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("fee"));
        loadAllRegistration();
        loadCmbSelectField();

        tblUpdateRegistration.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ViewAllRegistrationTM>() {
            @Override
            public void changed(ObservableValue<? extends ViewAllRegistrationTM> observable, ViewAllRegistrationTM oldValue, ViewAllRegistrationTM newValue) {
                ViewAllRegistrationTM selectedItem = tblUpdateRegistration.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    txtRegId.setText(selectedItem.getId());
                    txtStudentId.setText(selectedItem.getStudentId());
                    txtCourse.setText(selectedItem.getCourse());
                    txtRegDate.setText(selectedItem.getRegDate());
                    txtFee.setText(String.valueOf(selectedItem.getFee()));
                    ArrayList<String> allBatches = StudentRegistrationService.getAllBatches(txtCourse.getText());
                    if (allBatches != null) {
                        ObservableList<String> items = cmbBatch.getItems();
                        items.clear();
                        items.addAll(allBatches);
                        cmbBatch.setValue(selectedItem.getBatch());
                    }
                }
            }
        });
        cmbBatch.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    String batch = tblUpdateRegistration.getSelectionModel().getSelectedItem().getBatch();
                    String selectedItem = cmbBatch.getSelectionModel().getSelectedItem();
                    if (batch != null && selectedItem != null) {
                        if (Integer.parseInt(selectedItem) > Integer.parseInt(batch)) {
                            lblAdditionalFee.setVisible(true);
                            txtAdditionalFee.setDisable(false);
                            txtAdditionalFee.setVisible(true);
                            btnUpdate.setVisible(true);
                            btnUpdate.setDisable(false);
                            btnCancel.setVisible(true);
                            btnCancel.setDisable(false);
                        } else {
                            txtAdditionalFee.setDisable(true);
                            txtAdditionalFee.setVisible(false);
                            lblAdditionalFee.setVisible(false);
                            btnUpdate.setVisible(false);
                            btnUpdate.setDisable(true);
                            btnCancel.setVisible(false);
                            btnCancel.setDisable(true);
                        }
                    }
                } catch (NullPointerException ignored) {

                }
            }
        });
    }

    private void loadCmbSelectField() {
        ObservableList<TableColumn<ViewAllRegistrationTM, ?>> columns = tblUpdateRegistration.getColumns();
        for (TableColumn<ViewAllRegistrationTM, ?> column : columns) {
            cmbSelectField.getItems().add(column.getText());
        }
    }

    private void loadAllRegistration() {
        ArrayList<ViewAllRegistrationTM> allRegistration = StudentViewAllService.getAllRegistration();
        ObservableList<ViewAllRegistrationTM> items = tblUpdateRegistration.getItems();
        if (allRegistration != null) {
            items.addAll(allRegistration);
        }
    }


    public void btnCancelOnAction(ActionEvent actionEvent) {
        cmbBatch.getItems().clear();
        tblUpdateRegistration.getSelectionModel().clearSelection();
        txtRegId.clear();
        txtStudentId.clear();
        txtCourse.clear();
        txtRegDate.clear();
        txtFee.clear();
        txtAdditionalFee.clear();
        txtAdditionalFee.setDisable(true);
        txtAdditionalFee.setVisible(false);
        lblAdditionalFee.setVisible(false);
        btnUpdate.setVisible(false);
        btnUpdate.setDisable(true);
        btnCancel.setVisible(false);
        btnCancel.setDisable(true);
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        Alert alert;
        if (txtAdditionalFee.getText().contentEquals("")) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Complete The Form!");
            alert.showAndWait();
        } else if (
                !Pattern.matches("[0-9]{1,10}", txtAdditionalFee.getText())
                        ||
                        Integer.parseInt(txtAdditionalFee.getText()) == 0
        ) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Details Not Eligible!");
            alert.showAndWait();
        } else {
            double fee = Double.parseDouble(txtFee.getText());
            fee += Double.parseDouble(txtAdditionalFee.getText());
            RegistrationDTO dto = new RegistrationDTO(
                    txtRegId.getText(),
                    txtStudentId.getText(),
                    BatchService.getBatchId(txtCourse.getText(), cmbBatch.getValue()),
                    txtRegDate.getText(),
                    fee
            );
            boolean registrationUpdated = StudentUpdateService.updateRegistration(dto);
            if (registrationUpdated) {
                tblUpdateRegistration.getItems().set(tblUpdateRegistration.getSelectionModel().getSelectedIndex(),
                        new ViewAllRegistrationTM(
                                dto.getId(),
                                dto.getStudentId(),
                                tblUpdateRegistration.getSelectionModel().getSelectedItem().getStudentName(),
                                txtCourse.getText(),
                                cmbBatch.getValue(),
                                txtRegDate.getText(),
                                fee,
                                StudentViewAllService.getDueFee(dto.getId(), fee)
                        ));
                btnCancel.fire();
                alert = new Alert(Alert.AlertType.INFORMATION, "Registration Updated\nSuccessfully!");
                alert.showAndWait();
            }
        }
    }

    public void txtSearchOnKeyReleased(KeyEvent keyEvent) {
        ArrayList<ViewAllRegistrationTM> allRegistrations = StudentViewAllService.getAllRegistration();
        ObservableList<ViewAllRegistrationTM> items = tblUpdateRegistration.getItems();

        String text = txtSearch.getText().toLowerCase();
        if (cmbSelectField.getValue() != null) {
            if (text.contentEquals("")) {
                items.clear();
                loadAllRegistration();
            } else if (cmbSelectField.getValue().contentEquals("ID")) {
                items.clear();
                if (allRegistrations != null) {
                    for (ViewAllRegistrationTM model : allRegistrations) {
                        if (model.getId().toLowerCase().contains(text)) {
                            items.add(model);
                        }
                    }
                }
            } else if (cmbSelectField.getValue().contentEquals("Student ID")) {
                items.clear();
                if (allRegistrations != null) {
                    for (ViewAllRegistrationTM model : allRegistrations) {
                        if (model.getStudentId().toLowerCase().contains(text)) {
                            items.add(model);
                        }
                    }
                }
            } else if (cmbSelectField.getValue().contentEquals("Student Name")) {
                items.clear();
                if (allRegistrations != null) {
                    for (ViewAllRegistrationTM model : allRegistrations) {
                        if (model.getStudentName().toLowerCase().contains(text)) {
                            items.add(model);
                        }
                    }
                }
            } else if (cmbSelectField.getValue().contentEquals("Course")) {
                items.clear();
                if (allRegistrations != null) {
                    for (ViewAllRegistrationTM model : allRegistrations) {
                        if (model.getCourse().toLowerCase().contains(text)) {
                            items.add(model);
                        }
                    }
                }
            } else if (cmbSelectField.getValue().contentEquals("Batch No")) {
                items.clear();
                if (allRegistrations != null) {
                    for (ViewAllRegistrationTM model : allRegistrations) {
                        if (model.getBatch().contains(text)) {
                            items.add(model);
                        }
                    }
                }
            } else if (cmbSelectField.getValue().contentEquals("Reg Date")) {
                items.clear();
                if (allRegistrations != null) {
                    for (ViewAllRegistrationTM model : allRegistrations) {
                        if (model.getRegDate().contains(text)) {
                            items.add(model);
                        }
                    }
                }
            } else if (cmbSelectField.getValue().contentEquals("Fee")) {
                items.clear();
                if (allRegistrations != null) {
                    for (ViewAllRegistrationTM model : allRegistrations) {
                        if (String.valueOf(model.getFee()).contains(text)) {
                            items.add(model);
                        }
                    }
                }
            }
        }
    }
}
