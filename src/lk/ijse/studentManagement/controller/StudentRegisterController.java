package lk.ijse.studentManagement.controller;

import com.jfoenix.controls.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import lk.ijse.studentManagement.db.DBConnection;
import lk.ijse.studentManagement.dto.PaymentDTO;
import lk.ijse.studentManagement.dto.RegistrationDTO;
import lk.ijse.studentManagement.dto.StudentDTO;
import lk.ijse.studentManagement.service.BatchService;
import lk.ijse.studentManagement.service.StudentRegistrationService;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class StudentRegisterController implements Initializable {
    public JFXTextField txtStudentName;
    public JFXTextField txtEmail;
    public JFXTextField txtContact;
    public JFXTextField txtParentName;
    public JFXTextField txtParentContact;
    public TextField txtRegDate;
    public TextField txtFee;
    public TextField txtPayment;
    public TextField txtRegId;
    public TextField txtStartDate;
    public TextField txtRegFee;
    public TextField txtOccupantNumber;

    public JFXTextArea txtAddress;

    public JFXDatePicker txtDateOfBirth;


    public JFXComboBox<String> cmbCourse;
    public JFXComboBox<String> cmbBatch;

    public JFXButton btnRegister;
    public JFXButton btnCancel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadRegistrationId();
        loadRegistrationDate();
        loadAllCourses();
        cmbCourse.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (cmbCourse.getValue() != null) {
                    loadBatches();
                }
            }
        });
        cmbBatch.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                loadOccupantNumber();
                txtFee.setText(String.valueOf(StudentRegistrationService.getCourseFee(cmbCourse.getValue(), cmbBatch.getValue())));
                txtStartDate.setText(StudentRegistrationService.getBatchStartDate(cmbCourse.getValue(), cmbBatch.getValue()));
                txtRegFee.setText(String.valueOf(StudentRegistrationService.getRegistrationFee(cmbCourse.getValue(), cmbBatch.getValue())));
            }
        });
    }

    private void loadOccupantNumber() {
        int occupantNumber = StudentRegistrationService.getOccupantNumber(cmbCourse.getValue(), cmbBatch.getValue());
        txtOccupantNumber.setText(String.valueOf(occupantNumber));
    }

    private void loadBatches() {
        ArrayList<String> allBatches = StudentRegistrationService.getAllBatches(cmbCourse.getValue());
        if (allBatches != null) {
            ObservableList<String> items = cmbBatch.getItems();
            items.clear();
            items.addAll(allBatches);
        }
    }


    private void loadAllCourses() {
        ArrayList<String> allCourses = BatchService.getAllCourses();
        if (allCourses != null) {
            cmbCourse.getItems().addAll(allCourses);
        }
    }

    private void loadRegistrationDate() {
        LocalDate now = LocalDate.now();
        String date = DateTimeFormatter.ofPattern("YYYY-MM-dd").format(now);
        txtRegDate.setText(date);
    }

    private void loadRegistrationId() {
        String nextRegisterId = StudentRegistrationService.getNextRegisterId();
        txtRegId.setText(nextRegisterId == null ? "R00001" : nextRegisterId);
    }

    public void btnRegisterOnAction(ActionEvent actionEvent) {
        double amount = Double.parseDouble(txtFee.getText()) + Double.parseDouble(txtRegFee.getText());
        Alert alert;
        if (detailsMissing()) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Complete The Form!");
            alert.showAndWait();
        } else if (parentDetailsNotEligible() || otherDetailNotEligible()) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Details Not Eligible!");
            alert.showAndWait();
        } else if (alreadyRegisteredToThisCourse()) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Already Registered To This Course!");
            alert.showAndWait();
        } else if (amount < Double.parseDouble(txtPayment.getText())) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Cannot Make Payments More Than " + amount + "/= !");
            alert.showAndWait();
        } else {

            Connection connection;
            try {
                connection = DBConnection.getInstance().getConnection();
                connection.setAutoCommit(false);

                String dateOfBirth = DateTimeFormatter.ofPattern("YYYY-MM-dd").format(txtDateOfBirth.getValue());
                double fee = Double.parseDouble(txtFee.getText()) + Double.parseDouble(txtRegFee.getText());
                boolean registered;
                boolean studentAdded;
                boolean paymentAdded;
                String nextSID = StudentRegistrationService.getNextStudentId();
                String nextPID = StudentRegistrationService.getNextPaymentId();

                if (txtRegId.getText().contentEquals("R00001")) {
                    nextPID = "P00001";
                    studentAdded = StudentRegistrationService.addStudent(new StudentDTO(
                            "S00001",
                            txtStudentName.getText(),
                            txtEmail.getText(),
                            txtAddress.getText(),
                            txtContact.getText(),
                            dateOfBirth,
                            txtParentName.getText(),
                            txtParentContact.getText()
                    ));
                    registered = StudentRegistrationService.registerStudent(new RegistrationDTO(
                            txtRegId.getText(),
                            "S00001",
                            BatchService.getBatchId(cmbCourse.getValue(), cmbBatch.getValue()),
                            txtRegDate.getText(),
                            fee
                    ));
                } else {

                    if (StudentRegistrationService.studentAlreadyExists(txtContact.getText())) {
                        studentAdded = true;
                        registered = StudentRegistrationService.registerStudent(new RegistrationDTO(
                                txtRegId.getText(),
                                StudentRegistrationService.getStudentId(txtContact.getText()),
                                BatchService.getBatchId(cmbCourse.getValue(), cmbBatch.getValue()),
                                txtRegDate.getText(),
                                fee
                        ));
                    } else {
                        studentAdded = StudentRegistrationService.addStudent(new StudentDTO(
                                nextSID,
                                txtStudentName.getText(),
                                txtEmail.getText(),
                                txtAddress.getText(),
                                txtContact.getText(),
                                dateOfBirth,
                                txtParentName.getText(),
                                txtParentContact.getText()
                        ));
                        registered = StudentRegistrationService.registerStudent(new RegistrationDTO(
                                txtRegId.getText(),
                                nextSID,
                                BatchService.getBatchId(cmbCourse.getValue(), cmbBatch.getValue()),
                                txtRegDate.getText(),
                                fee
                        ));
                    }

                }
                paymentAdded = StudentRegistrationService.addPayment(new PaymentDTO(
                        nextPID,
                        txtRegId.getText(),
                        txtRegDate.getText(),
                        Double.parseDouble(txtPayment.getText())

                ));
                if (studentAdded && paymentAdded && registered) {
                    connection.commit();
                    connection.setAutoCommit(true);

                    btnCancel.fire();
                    alert = new Alert(Alert.AlertType.INFORMATION, "Registration Successful!");
                } else {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    alert = new Alert(Alert.AlertType.INFORMATION, "Try Again!");

                }
                alert.showAndWait();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean alreadyRegisteredToThisCourse() {
        boolean allowed = StudentRegistrationService.getRegistrationVerification(txtContact.getText(), cmbCourse.getValue());
        return !allowed;
    }

    private boolean otherDetailNotEligible() {
        return !Pattern.matches("[a-zA-Z]{3,30}", txtStudentName.getText().replaceAll("\\s+", ""))
                ||
                !Pattern.matches("[0-9]{10}", txtContact.getText())
                ||
                !Pattern.matches("[0-9]{1,10}", txtPayment.getText())
                ||
                txtPayment.getText().contentEquals("0");

    }

    private boolean parentDetailsNotEligible() {
        return !Pattern.matches("[a-zA-Z]{3,30}", txtParentName.getText().replaceAll("\\s+", ""))
                ||
                !Pattern.matches("[0-9]{10}", txtParentContact.getText());
    }

    private boolean detailsMissing() {
        return txtStudentName.getText().contentEquals("")
                ||
                txtEmail.getText().contentEquals("")
                ||
                txtAddress.getText().contentEquals("")
                ||
                txtContact.getText().contentEquals("")
                ||
                txtDateOfBirth.getValue() == null
                ||
                cmbCourse.getValue() == null
                ||
                cmbBatch.getValue() == null
                ||
                txtPayment.getText().contentEquals("")
                ||
                txtParentName.getText().contentEquals("")
                ||
                txtParentContact.getText().contentEquals("");
    }


    public void btnCancelOnAction(ActionEvent actionEvent) {
        txtStudentName.clear();
        txtEmail.clear();
        txtAddress.clear();
        txtContact.clear();
        txtDateOfBirth.setValue(null);
        txtParentName.clear();
        txtParentContact.clear();
        loadRegistrationId();
        loadRegistrationDate();
        cmbCourse.setValue(null);
        cmbBatch.getItems().clear();
        txtFee.clear();
        txtRegFee.clear();
        txtPayment.clear();
        txtOccupantNumber.clear();
        txtStudentName.requestFocus();
    }

    public void txtStudentNameOnAction(ActionEvent actionEvent) {
        txtEmail.requestFocus();
    }

    public void txtEmailOnAction(ActionEvent actionEvent) {
        txtAddress.requestFocus();
    }

    public void txtParentNameOnAction(ActionEvent actionEvent) {
        txtParentContact.requestFocus();

    }


}
