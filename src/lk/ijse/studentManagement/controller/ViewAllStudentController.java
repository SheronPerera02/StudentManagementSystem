package lk.ijse.studentManagement.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lk.ijse.studentManagement.model.ViewAllStudentTM;
import lk.ijse.studentManagement.service.StudentViewAllService;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ViewAllStudentController implements Initializable {
    public TableView<ViewAllStudentTM> tblViewAllStudent;

    public TableColumn<ViewAllStudentTM, String> colId;
    public TableColumn<ViewAllStudentTM, String> colName;
    public TableColumn<ViewAllStudentTM, String> colEmail;
    public TableColumn<ViewAllStudentTM, String> colAddress;
    public TableColumn<ViewAllStudentTM, String> colContact;
    public TableColumn<ViewAllStudentTM, String> colDateOfBirth;
    public TableColumn<ViewAllStudentTM, String> colParentName;
    public TableColumn<ViewAllStudentTM, String> colParentContact;

    public TextField txtSearch;

    public JFXComboBox<String> cmbSelectField;

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


    }

    private void loadCmbSelectField() {
        ObservableList<TableColumn<ViewAllStudentTM, ?>> columns = tblViewAllStudent.getColumns();
        for (TableColumn<ViewAllStudentTM, ?> column : columns) {
            cmbSelectField.getItems().add(column.getText());
        }
    }

    private void loadAllStudent() {
        ArrayList<ViewAllStudentTM> allStudents = StudentViewAllService.getAllStudent();
        ObservableList<ViewAllStudentTM> items = tblViewAllStudent.getItems();
        if (allStudents != null) {
            items.addAll(allStudents);
        }
    }

    public void tblViewAllStudentOnMouseClicked(MouseEvent mouseEvent) {
        int clickCount = mouseEvent.getClickCount();
        if (clickCount == 2) {
            ViewAllStudentTM selectedItem = tblViewAllStudent.getSelectionModel().getSelectedItem();
        }
    }

    public void txtSearchOnKeyReleased(KeyEvent keyEvent) {
        ArrayList<ViewAllStudentTM> allStudents = StudentViewAllService.getAllStudent();
        ObservableList<ViewAllStudentTM> items = tblViewAllStudent.getItems();

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
}
