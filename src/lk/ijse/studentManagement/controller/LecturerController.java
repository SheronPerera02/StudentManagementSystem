package lk.ijse.studentManagement.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.studentManagement.dto.LecturerDTO;
import lk.ijse.studentManagement.model.LecturerTM;
import lk.ijse.studentManagement.service.LecturerService;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class LecturerController implements Initializable {
    public JFXTextField txtName;
    public JFXTextField txtEmail;
    public JFXTextField txtContact;
    public JFXTextArea txtAddress;

    public JFXButton btnAddOrUpdate;
    public JFXButton btnCancel;

    public TableView<LecturerTM> tblAddLecturer;

    public TableColumn<LecturerTM, String> colId;
    public TableColumn<LecturerTM, String> colName;
    public TableColumn<LecturerTM, String> colEmail;
    public TableColumn<LecturerTM, String> colAddress;
    public TableColumn<LecturerTM, String> colContact;
    public TableColumn<LecturerTM, String> colDateOfBirth;

    public JFXDatePicker datePicker;


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colDateOfBirth.setCellValueFactory(new PropertyValueFactory<>("dob"));

        loadAllLecturers();

        tblAddLecturer.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<LecturerTM>() {
            @Override
            public void changed(ObservableValue<? extends LecturerTM> observable, LecturerTM oldValue, LecturerTM newValue) {
                btnAddOrUpdate.setText("Update");
                LecturerTM lecturerTM = tblAddLecturer.getSelectionModel().getSelectedItem();
                if (lecturerTM != null) {
                    txtName.setText(lecturerTM.getName());
                    txtEmail.setText(lecturerTM.getEmail());
                    txtAddress.setText(lecturerTM.getAddress());
                    txtContact.setText(lecturerTM.getContact());
                    String date = getSortedDate();

                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    datePicker.setValue(LocalDate.parse(date, dateTimeFormatter));

                    btnAddOrUpdate.setStyle("-fx-background-color: #28a745");
                }
            }
        });


    }

    private String getSortedDate() {
        LecturerTM lecturerTM = tblAddLecturer.getSelectionModel().getSelectedItem();
        String date = "";
        date += lecturerTM.getDob().charAt(8);
        date += lecturerTM.getDob().charAt(9) + "-";
        date += lecturerTM.getDob().charAt(5);
        date += lecturerTM.getDob().charAt(6) + "-";
        date += lecturerTM.getDob().charAt(0);
        date += lecturerTM.getDob().charAt(1);
        date += lecturerTM.getDob().charAt(2);
        date += lecturerTM.getDob().charAt(3);
        return date;
    }


    private void loadAllLecturers() {
        ArrayList<LecturerTM> allLecturers = LecturerService.getAllLecturers();
        ObservableList<LecturerTM> items = tblAddLecturer.getItems();

        for (int i = 0; i < allLecturers.size(); i++) {
            items.add(new LecturerTM(
                    allLecturers.get(i).getId(),
                    allLecturers.get(i).getName(),
                    allLecturers.get(i).getEmail(),
                    allLecturers.get(i).getAddress(),
                    allLecturers.get(i).getContact(),
                    allLecturers.get(i).getDob()
            ));
        }
    }


    public void btnAddOrUpdateOnAction(ActionEvent actionEvent) {
        try {
            if (btnAddOrUpdate.getText().contentEquals("Add")) {
                addLecturer();
            } else {
                updateLecturer();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Try Again!");
            alert.showAndWait();
        }

    }

    private void updateLecturer() {
        Alert alert = null;
        if (detailsNotEligible()) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Details Not Eligible!");
            alert.showAndWait();
        } else {
            LocalDate value = datePicker.getValue();
            String date = DateTimeFormatter.ofPattern("YYYY-MM-dd").format(value);
            LecturerDTO dto = new LecturerDTO(
                    tblAddLecturer.getSelectionModel().getSelectedItem().getId(),
                    txtName.getText(),
                    txtEmail.getText(),
                    txtAddress.getText(),
                    date,
                    txtContact.getText()
            );
            boolean isUpdated = LecturerService.updateLecturer(dto);
            if (isUpdated) {

                ObservableList<LecturerTM> items = tblAddLecturer.getItems();
                items.set(tblAddLecturer.getSelectionModel().getSelectedIndex(), new LecturerTM(
                        dto.getId(),
                        dto.getName(),
                        dto.getEmail(),
                        dto.getAddress(),
                        dto.getContact(),
                        dto.getDob()
                ));

                btnCancel.fire();
                alert = new Alert(Alert.AlertType.INFORMATION, "Lecturer Updated\nSuccessfully!");
                alert.showAndWait();
            }
        }
    }

    private void addLecturer() {
        Alert alert = null;

        if (detailsMissing()) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Complete The Form!");
            alert.showAndWait();
        } else if (detailsNotEligible()) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Details Not Eligible!");
            alert.showAndWait();
        } else {

            LocalDate value = datePicker.getValue();
            String date = DateTimeFormatter.ofPattern("YYYY-MM-dd").format(value);

            boolean added = false;
            String nextId = LecturerService.getNextId();
            if (nextId == null) {
                nextId = "Lec001";
            }
            added = LecturerService.addLecturer(new LecturerDTO(
                    nextId,
                    txtName.getText(),
                    txtEmail.getText(),
                    txtAddress.getText().replaceAll("\\s+", " "),
                    date,
                    txtContact.getText()
            ));
            ObservableList<LecturerTM> items = tblAddLecturer.getItems();
            if (added) {

                items.add(new LecturerTM(
                        nextId,
                        txtName.getText(),
                        txtEmail.getText(),
                        txtAddress.getText().replaceAll("\\s+", " "),
                        txtContact.getText(),
                        date

                ));
                btnCancel.fire();
                alert = new Alert(Alert.AlertType.INFORMATION, "Lecturer Added\nSuccessfully!");
                alert.showAndWait();
            }
        }
    }

    private boolean detailsNotEligible() {
        return !Pattern.matches("[a-zA-Z]{3,30}", txtName.getText().replaceAll("\\s+", ""))
                ||
                !Pattern.matches("[0-9]{10}", txtContact.getText());

    }

    private boolean detailsMissing() {
        return txtName.getText().contentEquals("")
                ||
                txtEmail.getText().contentEquals("")
                ||
                txtAddress.getText().contentEquals("")
                ||
                txtContact.getText().contentEquals("")
                ||
                datePicker.getValue() == null;
    }

    public void btnCancelOnAction(ActionEvent actionEvent) {
        tblAddLecturer.getSelectionModel().clearSelection();
        btnAddOrUpdate.setStyle("-fx-background-color:  #17a2b8");
        btnAddOrUpdate.setText("Add");
        txtName.clear();
        txtEmail.clear();
        txtAddress.clear();
        txtContact.clear();
        txtName.requestFocus();
        datePicker.setValue(null);


    }

    public void txtNameOnAction(ActionEvent actionEvent) {
        txtEmail.requestFocus();
    }

    public void txtEmailOnAction(ActionEvent actionEvent) {
        txtContact.requestFocus();
    }


    public void txtContactOnAction(ActionEvent actionEvent) {
        txtAddress.requestFocus();
    }
}
