package lk.ijse.studentManagement.controller;

import com.jfoenix.controls.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import lk.ijse.studentManagement.dto.StudentDTO;
import lk.ijse.studentManagement.model.ViewAllStudentTM;
import lk.ijse.studentManagement.service.StudentUpdateService;
import lk.ijse.studentManagement.service.StudentViewAllService;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class UpdateStudentController implements Initializable {
    public TableView<ViewAllStudentTM> tblUpdateStudent;

    public TableColumn<ViewAllStudentTM, String> colId;
    public TableColumn<ViewAllStudentTM, String> colName;
    public TableColumn<ViewAllStudentTM, String> colEmail;
    public TableColumn<ViewAllStudentTM, String> colAddress;
    public TableColumn<ViewAllStudentTM, String> colContact;
    public TableColumn<ViewAllStudentTM, String> colDateOfBirth;
    public TableColumn<ViewAllStudentTM, String> colParentName;
    public TableColumn<ViewAllStudentTM, String> colParentContact;

    public JFXComboBox<String> cmbSelectField;

    public JFXTextField txtStudentName;
    public JFXTextField txtEmail;
    public JFXTextField txtContact;
    public JFXTextField txtParentName;
    public JFXTextField txtParentContact;

    public TextField txtSearch;

    public JFXTextArea txtAddress;

    public JFXDatePicker txtDateOfBirth;

    public JFXButton btnCancel;
    public JFXButton btnUpdate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colDateOfBirth.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        colParentName.setCellValueFactory(new PropertyValueFactory<>("parentName"));
        colParentContact.setCellValueFactory(new PropertyValueFactory<>("parentContact"));
        loadAllStudent();
        loadCmbSelectField();
        tblUpdateStudent.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ViewAllStudentTM>() {
            @Override
            public void changed(ObservableValue<? extends ViewAllStudentTM> observable, ViewAllStudentTM oldValue, ViewAllStudentTM newValue) {
                ViewAllStudentTM selectedItem = tblUpdateStudent.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    String parentName = selectedItem.getParentName().contentEquals("-")? ""
                            : selectedItem.getParentName();
                    String parentContact = selectedItem.getParentContact().contentEquals("-")? ""
                            : selectedItem.getParentContact();
                    txtStudentName.setText(selectedItem.getName());
                    txtEmail.setText(selectedItem.getEmail());
                    txtAddress.setText(selectedItem.getAddress());
                    txtParentName.setText(parentName);
                    txtParentContact.setText(parentContact);
                    txtContact.setText(selectedItem.getContact());
                    txtDateOfBirth.setValue(LocalDate.parse(getSortedDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")));


                }
            }
        });
    }

    private String getSortedDate() {
        ViewAllStudentTM viewAllStudentTM = tblUpdateStudent.getSelectionModel().getSelectedItem();
        String date = "";
        date += viewAllStudentTM.getDateOfBirth().charAt(8);
        date += viewAllStudentTM.getDateOfBirth().charAt(9) + "-";
        date += viewAllStudentTM.getDateOfBirth().charAt(5);
        date += viewAllStudentTM.getDateOfBirth().charAt(6) + "-";
        date += viewAllStudentTM.getDateOfBirth().charAt(0);
        date += viewAllStudentTM.getDateOfBirth().charAt(1);
        date += viewAllStudentTM.getDateOfBirth().charAt(2);
        date += viewAllStudentTM.getDateOfBirth().charAt(3);
        return date;
    }

    private void loadAllStudent() {
        ArrayList<ViewAllStudentTM> allStudents = StudentViewAllService.getAllStudent();
        ObservableList<ViewAllStudentTM> items = tblUpdateStudent.getItems();
        if (allStudents != null) {
            items.addAll(allStudents);
        }
    }

    private void loadCmbSelectField() {
        ObservableList<TableColumn<ViewAllStudentTM, ?>> columns = tblUpdateStudent.getColumns();
        for (TableColumn<ViewAllStudentTM, ?> column : columns) {
            cmbSelectField.getItems().add(column.getText());
        }
    }

    public void txtSearchOnKeyReleased(KeyEvent keyEvent) {

        ArrayList<ViewAllStudentTM> allStudents = StudentViewAllService.getAllStudent();
        ObservableList<ViewAllStudentTM> items = tblUpdateStudent.getItems();

        String text = txtSearch.getText().toLowerCase();
        if (cmbSelectField.getValue() != null) {
            if (text.contentEquals("")) {
                items.clear();
                loadAllStudent();
            } else if (cmbSelectField.getValue().contentEquals("ID")) {
                items.clear();
                if (allStudents != null) {
                    for (ViewAllStudentTM model : allStudents) {
                        if (model.getId().toLowerCase().contains(text)) {
                            items.add(model);
                        }
                    }
                }
            } else if (cmbSelectField.getValue().contentEquals("Name")) {
                items.clear();
                if (allStudents != null) {
                    for (ViewAllStudentTM model : allStudents) {
                        if (model.getName().toLowerCase().contains(text)) {
                            items.add(model);
                        }
                    }
                }
            } else if (cmbSelectField.getValue().contentEquals("E-mail")) {
                items.clear();
                if (allStudents != null) {
                    for (ViewAllStudentTM model : allStudents) {
                        if (model.getEmail().toLowerCase().contains(text)) {
                            items.add(model);
                        }
                    }
                }
            } else if (cmbSelectField.getValue().contentEquals("Address")) {
                items.clear();
                if (allStudents != null) {
                    for (ViewAllStudentTM model : allStudents) {
                        if (model.getAddress().toLowerCase().contains(text)) {
                            items.add(model);
                        }
                    }
                }
            } else if (cmbSelectField.getValue().contentEquals("Contact")) {
                items.clear();
                if (allStudents != null) {
                    for (ViewAllStudentTM model : allStudents) {
                        if (model.getContact().toLowerCase().contains(text)) {
                            items.add(model);
                        }
                    }
                }
            } else if (cmbSelectField.getValue().contentEquals("Date Of Birth")) {
                items.clear();
                if (allStudents != null) {
                    for (ViewAllStudentTM model : allStudents) {
                        if (model.getDateOfBirth().toLowerCase().contains(text)) {
                            items.add(model);
                        }
                    }
                }
            } else if (cmbSelectField.getValue().contentEquals("Name(Parent/Other)")) {
                items.clear();
                if (allStudents != null) {
                    for (ViewAllStudentTM model : allStudents) {
                        if (model.getParentName().toLowerCase().contains(text)) {
                            items.add(model);
                        }
                    }
                }
            } else if (cmbSelectField.getValue().contentEquals("Contact(Parent/Other)")) {
                items.clear();
                if (allStudents != null) {
                    for (ViewAllStudentTM model : allStudents) {
                        if (model.getParentContact().toLowerCase().contains(text)) {
                            items.add(model);
                        }
                    }
                }
            }
        }
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

    public void txtParentContactOnAction(ActionEvent actionEvent) {
        txtContact.requestFocus();
    }

    public void btnCancelOnAction(ActionEvent actionEvent) {
        tblUpdateStudent.getSelectionModel().clearSelection();
        txtStudentName.clear();
        txtEmail.clear();
        txtAddress.clear();
        txtParentName.clear();
        txtParentContact.clear();
        txtContact.clear();
        txtDateOfBirth.setValue(null);
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        Alert alert;
        if (detailsMissing()) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Complete The Form!");
            alert.showAndWait();
        } else if (detailsNotEligible()) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Details Not Eligible!");
            alert.showAndWait();
        } else {
            StudentDTO dto = new StudentDTO(
                    tblUpdateStudent.getSelectionModel().getSelectedItem().getId(),
                    txtStudentName.getText(),
                    txtEmail.getText(),
                    txtAddress.getText(),
                    txtContact.getText(),
                    DateTimeFormatter.ofPattern("YYYY-MM-dd").format(txtDateOfBirth.getValue()),
                    txtParentName.getText().contentEquals("") ? "-" : txtParentName.getText(),
                    txtParentContact.getText().contentEquals("") ? "-" : txtParentContact.getText()
            );
            boolean isUpdated = StudentUpdateService.updateStudent(dto);
            if (isUpdated) {
                tblUpdateStudent.getItems().set(tblUpdateStudent.getSelectionModel().getSelectedIndex(), new ViewAllStudentTM(
                        dto.getId(),
                        dto.getName(),
                        dto.getEmail(),
                        dto.getAddress(),
                        dto.getContact(),
                        dto.getDateOfBirth(),
                        dto.getParentName(),
                        dto.getParentContact()
                ));
                btnCancel.fire();
                alert = new Alert(Alert.AlertType.INFORMATION, "Student Updated\nSuccessfully!");
                alert.showAndWait();
            }
        }


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
                tblUpdateStudent.getSelectionModel().getSelectedItem()==null;
    }

    private boolean detailsNotEligible() {
        return !Pattern.matches("[a-zA-Z]{3,30}", txtStudentName.getText().replaceAll("\\s+", ""))
                ||
                !Pattern.matches("[0-9]{10}", txtContact.getText())
                ||
                parentDetailsNotEligible();
    }

    private boolean parentDetailsNotEligible() {
        if (txtParentName.getText().contentEquals("")) {
            return false;
        } else {
            return !Pattern.matches("[a-zA-Z]{3,30}", txtParentName.getText().replaceAll("\\s+", ""))
                    ||
                    !Pattern.matches("[0-9]{10}", txtParentContact.getText());
        }

    }
}
