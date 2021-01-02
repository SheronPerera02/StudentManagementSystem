package lk.ijse.studentManagement.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import lk.ijse.studentManagement.model.ViewAllPaymentTM;
import lk.ijse.studentManagement.service.StudentViewAllService;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ViewAllPaymentController implements Initializable {
    public TableView<ViewAllPaymentTM> tblViewAllPayment;

    public TableColumn<ViewAllPaymentTM, String> colId;
    public TableColumn<ViewAllPaymentTM, String> colRegistrationId;
    public TableColumn<ViewAllPaymentTM, String> colStudentName;
    public TableColumn<ViewAllPaymentTM, String> colDate;
    public TableColumn<ViewAllPaymentTM, Double> colAmount;

    public TextField txtSearch;

    public JFXComboBox<String> cmbSelectField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRegistrationId.setCellValueFactory(new PropertyValueFactory<>("registrationId"));
        colStudentName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        loadAllPayment();
        loadCmbSelectField();
    }

    private void loadAllPayment() {
        ArrayList<ViewAllPaymentTM> allPayments = StudentViewAllService.getAllPayment();
        ObservableList<ViewAllPaymentTM> items = tblViewAllPayment.getItems();
        if (allPayments != null) {
            items.addAll(allPayments);
        }
    }

    private void loadCmbSelectField() {
        ObservableList<TableColumn<ViewAllPaymentTM, ?>> columns = tblViewAllPayment.getColumns();
        for (TableColumn<ViewAllPaymentTM, ?> column : columns) {
            cmbSelectField.getItems().add(column.getText());
        }
    }

    public void txtSearchOnKeyReleased(KeyEvent keyEvent) {
        ArrayList<ViewAllPaymentTM> allPayments = StudentViewAllService.getAllPayment();
        ObservableList<ViewAllPaymentTM> items = tblViewAllPayment.getItems();

        String text = txtSearch.getText().toLowerCase();
        if (cmbSelectField.getValue() != null) {
            if (text.contentEquals("")) {
                items.clear();
                loadAllPayment();
            } else if (cmbSelectField.getValue().contentEquals("Invoice No")) {
                items.clear();
                if (allPayments != null) {
                    for (ViewAllPaymentTM model : allPayments) {
                        if (model.getId().toLowerCase().contains(text)) {
                            items.add(model);
                        }
                    }
                }
            } else if (cmbSelectField.getValue().contentEquals("RegID")) {
                items.clear();
                if (allPayments != null) {
                    for (ViewAllPaymentTM model : allPayments) {
                        if (model.getRegistrationId().toLowerCase().contains(text)) {
                            items.add(model);
                        }
                    }
                }
            } else if (cmbSelectField.getValue().contentEquals("Student Name")) {
                items.clear();
                if (allPayments != null) {
                    for (ViewAllPaymentTM model : allPayments) {
                        if (model.getStudentName().toLowerCase().contains(text)) {
                            items.add(model);
                        }
                    }
                }
            } else if (cmbSelectField.getValue().contentEquals("Date")) {
                items.clear();
                if (allPayments != null) {
                    for (ViewAllPaymentTM model : allPayments) {
                        if (model.getDate().contains(text)) {
                            items.add(model);
                        }
                    }
                }
            } else if (cmbSelectField.getValue().contentEquals("Amount")) {
                items.clear();
                if (allPayments != null) {
                    for (ViewAllPaymentTM model : allPayments) {
                        if (String.valueOf(model.getAmount()).contains(text)) {
                            items.add(model);
                        }
                    }
                }
            }
        }
    }
}
