package lk.ijse.studentManagement.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
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
import lk.ijse.studentManagement.dto.BatchDTO;
import lk.ijse.studentManagement.model.BatchTM;
import lk.ijse.studentManagement.service.BatchService;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class BatchController implements Initializable {

    public JFXComboBox<String> cmbCourse;
    public JFXComboBox<String> cmbSelectField;

    public TextField txtBatch;
    public TextField txtSearch;
    public TextField txtRegFee;

    public JFXDatePicker txtStartDatePicker;
    public JFXDatePicker txtEndDatePicker;

    public TableView<BatchTM> tblAddBatch;

    public TableColumn<BatchTM, String> colId;
    public TableColumn<BatchTM, String> colCourse;
    public TableColumn<BatchTM, String> colBatch;
    public TableColumn<BatchTM, String> colStartDate;
    public TableColumn<BatchTM, String> colEndDate;
    public TableColumn<BatchTM, Double> colRegFee;

    public JFXButton btnAddOrUpdate;
    public JFXButton btnCancel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colBatch.setCellValueFactory(new PropertyValueFactory<>("batch"));
        colCourse.setCellValueFactory(new PropertyValueFactory<>("course"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        colRegFee.setCellValueFactory(new PropertyValueFactory<>("regFee"));
        loadAllCourses();
        loadAllBatches();
        loadCmbSelectField();
        tblAddBatch.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BatchTM>() {
            @Override
            public void changed(ObservableValue<? extends BatchTM> observable, BatchTM oldValue, BatchTM newValue) {
                btnAddOrUpdate.setText("Update");
                cmbCourse.setDisable(true);
                BatchTM batchTM = tblAddBatch.getSelectionModel().getSelectedItem();
                if (batchTM != null) {
                    txtRegFee.setDisable(true);
                    txtRegFee.setText(String.valueOf((int) batchTM.getRegFee()));
                    cmbCourse.setValue(batchTM.getCourse());
                    txtBatch.setText(String.valueOf(batchTM.getBatch()));
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    txtStartDatePicker.
                            setValue(LocalDate.parse(getSortedDate(batchTM.getStartDate()), dtf));
                    txtEndDatePicker.
                            setValue(LocalDate.parse(getSortedDate(batchTM.getEndDate()), dtf));
                    btnAddOrUpdate.setStyle("-fx-background-color: #28a745");
                }
            }
        });
        cmbCourse.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                loadBatch(newValue);
            }
        });
    }

    private void loadCmbSelectField() {
        ObservableList<TableColumn<BatchTM, ?>> columns = tblAddBatch.getColumns();
        for (TableColumn<BatchTM, ?> column : columns) {
            cmbSelectField.getItems().add(column.getText());
        }
    }

    private String getSortedDate(String date) {
        String sortedDate = "";
        sortedDate += date.charAt(8);
        sortedDate += date.charAt(9) + "-";
        sortedDate += date.charAt(5);
        sortedDate += date.charAt(6) + "-";
        sortedDate += date.charAt(0);
        sortedDate += date.charAt(1);
        sortedDate += date.charAt(2);
        sortedDate += date.charAt(3);
        return sortedDate;
    }

    private void loadBatch(String newValue) {
        String batch = BatchService.getNextBatch(newValue);
        txtBatch.setText(batch == null ? "1" : String.valueOf(Integer.parseInt(batch) + 1));
    }

    private void loadAllBatches() {
        ArrayList<BatchTM> allBatches = BatchService.getAllBatches();
        ObservableList<BatchTM> items = tblAddBatch.getItems();
        if (allBatches != null) {
            for (BatchTM allBatch : allBatches) {
                items.add(new BatchTM(
                        allBatch.getId(),
                        allBatch.getBatch(),
                        allBatch.getCourse(),
                        allBatch.getStartDate(),
                        allBatch.getEndDate(),
                        allBatch.getRegFee()
                ));
            }
        }
    }

    private void loadAllCourses() {
        ArrayList<String> allCourses = BatchService.getAllCourses();
        ObservableList<String> items = cmbCourse.getItems();
        if (allCourses != null) {
            items.addAll(allCourses);
        }
    }

    public void btnAddOrUpdateOnAction(ActionEvent actionEvent) {
        try {
            if (btnAddOrUpdate.getText().contentEquals("Add")) {
                addBatch();
            } else {
                updateBatch();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Try Again!");
            alert.showAndWait();
        }
    }

    private void updateBatch() {
        Alert alert = null;
        if (detailsNotEligible()) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Details Not Eligible!");
            alert.showAndWait();
        } else {
            String startDate = DateTimeFormatter.ofPattern("YYYY-MM-dd").format(txtStartDatePicker.getValue());
            String endDate = DateTimeFormatter.ofPattern("YYYY-MM-dd").format(txtEndDatePicker.getValue());
            BatchTM selectedItem = tblAddBatch.getSelectionModel().getSelectedItem();
            BatchDTO dto = new BatchDTO(
                    selectedItem.getId(),
                    selectedItem.getBatch(),
                    BatchService.getCourseId(selectedItem.getCourse()),
                    startDate,
                    endDate,
                    selectedItem.getRegFee(),
                    BatchService.getCourseFee(selectedItem.getCourse())
            );
            boolean isUpdated = BatchService.updateBatch(dto);
            if (isUpdated) {

                ObservableList<BatchTM> items = tblAddBatch.getItems();
                items.set(tblAddBatch.getSelectionModel().getSelectedIndex(), new BatchTM(
                        dto.getId(),
                        dto.getBatch(),
                        BatchService.getCourseName(dto.getCourseId()),
                        dto.getStartDate(),
                        dto.getEndDate(),
                        dto.getRegFee()
                ));

                btnCancel.fire();
                alert = new Alert(Alert.AlertType.INFORMATION, "Batch Updated\nSuccessfully!");
                alert.showAndWait();
            }
        }
    }

    private void addBatch() {
        Alert alert;
        if (detailsMissing()) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Complete The Form!");
            alert.showAndWait();
        } else if (detailsNotEligible()) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Details Not Eligible!");
            alert.showAndWait();
        } else {

            String startDate = DateTimeFormatter.ofPattern("YYYY-MM-dd").format(txtStartDatePicker.getValue());
            String endDate = DateTimeFormatter.ofPattern("YYYY-MM-dd").format(txtEndDatePicker.getValue());

            boolean added = false;
            String nextId = BatchService.getNextId();
            if (nextId == null) {
                nextId = "B00001";
            }
            added = BatchService.addBatch(new BatchDTO(
                    nextId,
                    Integer.parseInt(txtBatch.getText()),
                    getCourseId(),
                    startDate,
                    endDate,
                    Double.parseDouble(txtRegFee.getText()),
                    BatchService.getCourseFee(cmbCourse.getValue())
            ));
            ObservableList<BatchTM> items = tblAddBatch.getItems();
            if (added) {
                items.add(new BatchTM(
                        nextId,
                        Integer.parseInt(txtBatch.getText()),
                        cmbCourse.getSelectionModel().getSelectedItem(),
                        startDate,
                        endDate,
                        Double.parseDouble(txtRegFee.getText())
                ));
                btnCancel.fire();
                alert = new Alert(Alert.AlertType.INFORMATION, "Batch Added\nSuccessfully!");
                alert.showAndWait();
            }
        }
    }

    private boolean detailsNotEligible() {
        return txtStartDatePicker.getValue().isAfter(txtEndDatePicker.getValue())
                ||
                !Pattern.matches("[0-9]{1,10}", txtRegFee.getText())
                ||
                txtRegFee.getText().contentEquals("0");
    }

    private String getCourseId() {
        return BatchService.getCourseId(cmbCourse.getSelectionModel().getSelectedItem());
    }


    private boolean detailsMissing() {
        return cmbCourse.getValue() == null
                ||
                txtStartDatePicker.getValue() == null
                ||
                txtEndDatePicker.getValue() == null
                ||
                txtRegFee.getText().contentEquals("");
    }

    public void btnCancelOnAction(ActionEvent actionEvent) {
        tblAddBatch.getSelectionModel().clearSelection();
        btnAddOrUpdate.setStyle("-fx-background-color:  #17a2b8");
        cmbCourse.setDisable(false);
        btnAddOrUpdate.setText("Add");
        cmbCourse.setValue(null);
        txtRegFee.setDisable(false);
        txtRegFee.clear();
        txtBatch.clear();
        txtStartDatePicker.setValue(null);
        txtEndDatePicker.setValue(null);

    }

    public void txtSearchOnKeyReleased(KeyEvent keyEvent) {
        ArrayList<BatchTM> allBatches = BatchService.getAllBatches();
        ObservableList<BatchTM> items = tblAddBatch.getItems();

        String text = txtSearch.getText().toLowerCase();
        if (cmbSelectField.getValue() != null) {
            if (text.contentEquals("")) {
                items.clear();
                loadAllBatches();
            } else if (cmbSelectField.getValue().contentEquals("ID")) {
                items.clear();
                if (allBatches != null) {
                    for (BatchTM model : allBatches) {
                        if (model.getId().toLowerCase().contains(text)) {
                            items.add(model);
                        }
                    }
                }
            } else if (cmbSelectField.getValue().contentEquals("Course")) {
                items.clear();
                if (allBatches != null) {
                    for (BatchTM model : allBatches) {
                        if (model.getCourse().toLowerCase().contains(text)) {
                            items.add(model);
                        }
                    }
                }
            } else if (cmbSelectField.getValue().contentEquals("Batch No")) {
                items.clear();
                if (allBatches != null) {
                    for (BatchTM model : allBatches) {
                        if (String.valueOf(model.getBatch()).contains(text)) {
                            items.add(model);
                        }
                    }
                }
            } else if (cmbSelectField.getValue().contentEquals("Start Date")) {
                items.clear();
                if (allBatches != null) {
                    for (BatchTM model : allBatches) {
                        if (model.getStartDate().contains(text)) {
                            items.add(model);
                        }
                    }
                }
            } else if (cmbSelectField.getValue().contentEquals("End Date")) {
                items.clear();
                if (allBatches != null) {
                    for (BatchTM model : allBatches) {
                        if (model.getEndDate().contains(text)) {
                            items.add(model);
                        }
                    }
                }
            } else if (cmbSelectField.getValue().contentEquals("Reg Fee")) {
                items.clear();
                if (allBatches != null) {
                    for (BatchTM model : allBatches) {
                        if (String.valueOf(model.getRegFee()).contains(text)) {
                            items.add(model);
                        }
                    }
                }
            }
        }
    }
}

