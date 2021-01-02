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
import lk.ijse.studentManagement.dto.PaymentDTO;
import lk.ijse.studentManagement.model.ViewAllRegistrationTM;
import lk.ijse.studentManagement.service.StudentRegistrationService;
import lk.ijse.studentManagement.service.StudentViewAllService;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ViewAllRegistrationController implements Initializable {

    public TableView<ViewAllRegistrationTM> tblViewAllRegistration;

    public TableColumn<ViewAllRegistrationTM, String> colId;
    public TableColumn<ViewAllRegistrationTM, String> colStudentId;
    public TableColumn<ViewAllRegistrationTM, String> colStudentName;
    public TableColumn<ViewAllRegistrationTM, String> colCourse;
    public TableColumn<ViewAllRegistrationTM, String> colBatch;
    public TableColumn<ViewAllRegistrationTM, String> colRegDate;
    public TableColumn<ViewAllRegistrationTM, Double> colFee;
    public TableColumn<ViewAllRegistrationTM, Double> colDueFee;

    public TextField txtRegistrationId;
    public TextField txtPaymentDate;
    public TextField txtAmount;
    public TextField txtSearch;

    public JFXButton btnPayment;
    public JFXButton btnPay;
    public JFXButton btnCancel;

    public Label lblRegId;
    public Label lblDate;
    public Label lblAmount;

    public JFXComboBox<String> cmbSelectField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colStudentId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colStudentName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        colCourse.setCellValueFactory(new PropertyValueFactory<>("course"));
        colBatch.setCellValueFactory(new PropertyValueFactory<>("batch"));
        colRegDate.setCellValueFactory(new PropertyValueFactory<>("regDate"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("fee"));
        colDueFee.setCellValueFactory(new PropertyValueFactory<>("dueFee"));
        loadAllRegistration();
        loadPaymentDate();
        loadCmbSelectField();
        tblViewAllRegistration.getSelectionModel().selectedItemProperty().addListener((new ChangeListener<ViewAllRegistrationTM>() {
            @Override
            public void changed(ObservableValue<? extends ViewAllRegistrationTM> observable, ViewAllRegistrationTM oldValue, ViewAllRegistrationTM newValue) {
                ViewAllRegistrationTM model = tblViewAllRegistration.getSelectionModel().getSelectedItem();
                if (model != null) {
                    txtRegistrationId.setText(model.getId());
                }
            }
        }));

    }

    private void loadCmbSelectField() {
        ObservableList<TableColumn<ViewAllRegistrationTM, ?>> columns = tblViewAllRegistration.getColumns();
        for (TableColumn<ViewAllRegistrationTM, ?> column : columns) {
            cmbSelectField.getItems().add(column.getText());
        }
    }

    private void loadPaymentDate() {
        txtPaymentDate.setText(DateTimeFormatter.ofPattern("YYYY-MM-dd").format(LocalDate.now()));
    }

    private void loadAllRegistration() {
        ArrayList<ViewAllRegistrationTM> allRegistration = StudentViewAllService.getAllRegistration();
        ObservableList<ViewAllRegistrationTM> items = tblViewAllRegistration.getItems();
        if (allRegistration != null) {
            items.addAll(allRegistration);
        }
    }

    public void btnPaymentOnAction(ActionEvent actionEvent) {
        if (btnPay.isVisible()) {
            btnPay.setVisible(false);
            btnCancel.setVisible(false);
            lblRegId.setVisible(false);
            txtRegistrationId.setVisible(false);
            lblDate.setVisible(false);
            txtPaymentDate.setVisible(false);
            lblAmount.setVisible(false);
            txtAmount.setVisible(false);
            btnPayment.setText("Payment");
            btnCancel.fire();
        } else {
            btnPay.setVisible(true);
            btnCancel.setVisible(true);
            lblRegId.setVisible(true);
            txtRegistrationId.setVisible(true);
            lblDate.setVisible(true);
            txtPaymentDate.setVisible(true);
            lblAmount.setVisible(true);
            txtAmount.setVisible(true);
            btnPayment.setText("Hide");
        }
    }

    public void btnPayOnAction(ActionEvent actionEvent) {
        Alert alert;
        String name = StudentViewAllService.getStudentName(txtRegistrationId.getText());
        ViewAllRegistrationTM item = tblViewAllRegistration.getSelectionModel().getSelectedItem();
        if (txtRegistrationId.getText().contentEquals("")) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Select A Row!");
            alert.showAndWait();
        } else if (txtAmount.getText().contentEquals("")) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Input Amount!");
            alert.showAndWait();
        } else if (
                !Pattern.matches("[0-9]{1,9}", txtAmount.getText())
                        ||
                        Double.parseDouble(txtAmount.getText()) > tblViewAllRegistration.getSelectionModel().getSelectedItem().getDueFee()
                        ||
                        Integer.parseInt(txtAmount.getText()) == 0
        ) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Details Not Eligible!");
            alert.showAndWait();
        } else {
            alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Adding A Payment Of (" + txtAmount.getText() + "/=)\nTo "
                            + name + "[" + txtRegistrationId.getText() + "]\nIn " +
                            item.getCourse() + " - Batch " + item.getBatch());
            alert.showAndWait();
            if (alert.getResult().getText().contentEquals("OK")) {
                boolean paymentAdded = StudentRegistrationService.addPayment(new PaymentDTO(
                        StudentRegistrationService.getNextPaymentId(),
                        txtRegistrationId.getText(),
                        txtPaymentDate.getText(),
                        Double.parseDouble(txtAmount.getText())
                ));
                if (paymentAdded) {
                    tblViewAllRegistration.getItems().set(
                            tblViewAllRegistration.getSelectionModel().getSelectedIndex(),
                            new ViewAllRegistrationTM(
                                    item.getId(),
                                    item.getStudentId(),
                                    item.getStudentName(),
                                    item.getCourse(),
                                    item.getBatch(),
                                    txtPaymentDate.getText(),
                                    item.getFee(),
                                    (item.getDueFee() - Double.parseDouble(txtAmount.getText()))
                            )
                    );
                    btnCancel.fire();
                    alert = new Alert(Alert.AlertType.INFORMATION, "Payment Successful!");
                } else {
                    alert = new Alert(Alert.AlertType.INFORMATION, "Try Again!");
                }
                alert.showAndWait();
            }
        }
    }


    public void btnCancelOnAction(ActionEvent actionEvent) {
        tblViewAllRegistration.getSelectionModel().clearSelection();
        txtRegistrationId.clear();
        loadPaymentDate();
        txtAmount.clear();
    }

    public void txtSearchOnKeyReleased(KeyEvent keyEvent) {
        ArrayList<ViewAllRegistrationTM> allRegistrations = StudentViewAllService.getAllRegistration();
        ObservableList<ViewAllRegistrationTM> items = tblViewAllRegistration.getItems();

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
            } else if (cmbSelectField.getValue().contentEquals("Due Fee")) {
                items.clear();
                if (allRegistrations != null) {
                    for (ViewAllRegistrationTM model : allRegistrations) {
                        if (String.valueOf(model.getDueFee()).contains(text)) {
                            items.add(model);
                        }
                    }
                }
            }
        }
    }
}
