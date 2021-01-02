package lk.ijse.studentManagement.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import lk.ijse.studentManagement.dto.ExamDTO;
import lk.ijse.studentManagement.model.AddExamTM;
import lk.ijse.studentManagement.service.AddExamService;
import lk.ijse.studentManagement.service.BatchService;
import lk.ijse.studentManagement.service.StudentRegistrationService;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AddExamController implements Initializable {
    public TableView<AddExamTM> tblAddExam;

    public TableColumn<AddExamTM, String> colExamId;
    public TableColumn<AddExamTM, String> colDate;
    public TableColumn<AddExamTM, String> colCourse;
    public TableColumn<AddExamTM, String> colSubject;
    public TableColumn<AddExamTM, Integer> colBatchNo;
    public TableColumn<AddExamTM, String> colExamName;
    public TableColumn<AddExamTM, String> colStart;
    public TableColumn<AddExamTM, String> colEnd;
    public TableColumn<AddExamTM, Integer> colPassMark;

    public JFXDatePicker txtExamDate;

    public JFXTimePicker txtStartTime;
    public JFXTimePicker txtEndTime;

    public JFXComboBox<String> cmbCourse;
    public JFXComboBox<String> cmbBatch;
    public JFXComboBox<String> cmbSubject;
    public JFXComboBox<String> cmbSelectField;

    public TextField txtPassMark;
    public TextField txtSearch;
    public TextField txtExamName;

    public JFXButton btnAddOrUpdate;
    public JFXButton btnCancel;

    public RadioButton rdoMandatory;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colExamId.setCellValueFactory(new PropertyValueFactory<>("examId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colCourse.setCellValueFactory(new PropertyValueFactory<>("course"));
        colSubject.setCellValueFactory(new PropertyValueFactory<>("subject"));
        colBatchNo.setCellValueFactory(new PropertyValueFactory<>("batchNo"));
        colExamName.setCellValueFactory(new PropertyValueFactory<>("examName"));
        colStart.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        colEnd.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        colPassMark.setCellValueFactory(new PropertyValueFactory<>("passMark"));

        colExamId.setCellFactory(TextFieldTableCell.forTableColumn());

        loadAllCourses();
        loadAllExams();
        loadCmbSelectField();

        tblAddExam.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AddExamTM>() {
            @Override
            public void changed(ObservableValue<? extends AddExamTM> observable, AddExamTM oldValue, AddExamTM newValue) {
                AddExamTM selectedItem = tblAddExam.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    txtExamName.setDisable(true);
                    rdoMandatory.setDisable(true);
                    cmbCourse.setDisable(true);
                    cmbCourse.setValue(selectedItem.getCourse());
                    cmbBatch.setDisable(true);
                    cmbBatch.setValue(String.valueOf(selectedItem.getBatchNo()));
                    cmbSubject.setDisable(true);
                    cmbSubject.setValue(selectedItem.getSubject());
                    btnAddOrUpdate.setText("Update");
                    btnAddOrUpdate.setStyle("-fx-background-color: #28a745");
                    DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    txtExamDate.setValue(LocalDate.parse(getSortedDate(selectedItem.getDate()), dtf1));

                    txtStartTime.setValue(LocalTime.of(getHour(selectedItem.getStartTime()),
                            getMinute(selectedItem.getStartTime())));
                    txtEndTime.setValue(LocalTime.of(getHour(selectedItem.getEndTime()),
                            getMinute(selectedItem.getEndTime())));

                    txtPassMark.setText(String.valueOf(selectedItem.getPassMark()));
                    txtExamName.setText(selectedItem.getExamName());

                    boolean isMandatory = AddExamService.isMandatory(selectedItem.getExamId());
                    if (isMandatory) {
                        rdoMandatory.setSelected(true);
                    } else {
                        rdoMandatory.setSelected(false);
                    }
                }
            }
        });
        cmbCourse.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                cmbBatch.getItems().clear();
                ArrayList<String> allBatches = StudentRegistrationService.getAllBatches(cmbCourse.getValue());
                if (allBatches != null) {
                    cmbBatch.getItems().addAll(allBatches);
                }
                cmbSubject.getItems().clear();
                ArrayList<String> subjects = AddExamService.getSubjects(cmbCourse.getValue());
                if (subjects != null) {
                    cmbSubject.getItems().addAll(subjects);
                }
            }

        });
    }

    private void loadCmbSelectField() {
        ObservableList<TableColumn<AddExamTM, ?>> columns = tblAddExam.getColumns();
        for (TableColumn<AddExamTM, ?> column : columns) {
            cmbSelectField.getItems().add(column.getText());
        }
    }

    private int getHour(String time) {
        String hour = "";
        hour += time.charAt(0);
        hour += time.charAt(1);
        return Integer.parseInt(hour);
    }

    private int getMinute(String time) {
        String minute = "";
        minute += time.charAt(3);
        minute += time.charAt(4);
        return Integer.parseInt(minute);
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

    private void loadAllExams() {
        ArrayList<AddExamTM> allExams = AddExamService.getAllExams();
        ObservableList<AddExamTM> items = tblAddExam.getItems();
        if (allExams != null) {
            items.addAll(allExams);
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
        if (btnAddOrUpdate.getText().contentEquals("Add")) {
            addExam();
        } else {
            updateExam();
        }
    }

    private void updateExam() {
        Alert alert;
        if (detailsMissing()) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Complete The Form!");
            alert.showAndWait();
        } else if (
                !Pattern.matches("[0-9]{1,3}", txtPassMark.getText())
                        ||
                        !Pattern.matches("[a-zA-Z0-9]{4,50}", txtExamName.getText().replaceAll("\\s+", ""))
                        ||
                        Integer.parseInt(txtPassMark.getText()) == 0
                        ||
                        txtStartTime.getValue().isAfter(txtEndTime.getValue())
        ) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Details Not Eligible!");
            alert.showAndWait();
        } else {
            boolean isMandatory = rdoMandatory.isSelected();
            ExamDTO dto = new ExamDTO(
                    tblAddExam.getSelectionModel().getSelectedItem().getExamId(),
                    DateTimeFormatter.ofPattern("YYYY-MM-dd").format(txtExamDate.getValue()),
                    AddExamService.getCourseSubjectId(cmbCourse.getValue(), cmbSubject.getValue()),
                    DateTimeFormatter.ofPattern("HH:mm:ss").format(txtStartTime.getValue()),
                    DateTimeFormatter.ofPattern("HH:mm:ss").format(txtEndTime.getValue()),
                    Integer.parseInt(cmbBatch.getValue()),
                    isMandatory ? "(Mandatory)" + txtExamName.getText() : txtExamName.getText(),
                    Integer.parseInt(txtPassMark.getText())
            );

            boolean examUpdated = AddExamService.updateExam(dto);
            if (examUpdated) {
                ObservableList<AddExamTM> items = tblAddExam.getItems();
                items.set(tblAddExam.getSelectionModel().getSelectedIndex(), new AddExamTM(
                        dto.getId(),
                        dto.getDate(),
                        cmbCourse.getValue(),
                        cmbSubject.getValue(),
                        dto.getBatchNo(),
                        txtExamName.getText(),
                        dto.getStart(),
                        dto.getEnd(),
                        dto.getPassMark()
                ));
                btnCancel.fire();
                alert = new Alert(Alert.AlertType.INFORMATION, "Exam Updated\nSuccessfully!");
                alert.showAndWait();
            }
        }
    }

    private void addExam() {
        Alert alert;
        if (detailsMissing()) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Complete The Form!");
            alert.showAndWait();
        } else if (examNameAlreadyExists()) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Exam Name Already Exists!");
            alert.showAndWait();
        } else if (
                !Pattern.matches("[0-9]{1,3}", txtPassMark.getText())
                        ||
                        !Pattern.matches("[a-zA-Z0-9-()]{4,50}", txtExamName.getText().replaceAll("\\s+", ""))
                        ||
                        Integer.parseInt(txtPassMark.getText()) == 0
                        ||
                        txtStartTime.getValue().isAfter(txtEndTime.getValue())
        ) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Details Not Eligible!");
            alert.showAndWait();
        } else {
            boolean isMandatory = rdoMandatory.isSelected();
            String nextId = AddExamService.getNextId();
            ExamDTO dto = new ExamDTO(
                    nextId == null ? "Ex00001" : nextId,
                    DateTimeFormatter.ofPattern("YYYY-MM-dd").format(txtExamDate.getValue()),
                    AddExamService.getCourseSubjectId(cmbCourse.getValue(), cmbSubject.getValue()),
                    DateTimeFormatter.ofPattern("HH:mm:ss").format(txtStartTime.getValue()),
                    DateTimeFormatter.ofPattern("HH:mm:ss").format(txtEndTime.getValue()),
                    Integer.parseInt(cmbBatch.getValue()),
                    isMandatory ? "(Mandatory)" + txtExamName.getText() : txtExamName.getText(),
                    Integer.parseInt(txtPassMark.getText())

            );

            boolean examAdded = AddExamService.addExam(dto);
            if (examAdded) {
                ObservableList<AddExamTM> items = tblAddExam.getItems();
                items.add(new AddExamTM(
                        nextId == null ? "Ex00001" : nextId,
                        dto.getDate(),
                        cmbCourse.getValue(),
                        cmbSubject.getValue(),
                        Integer.parseInt(cmbBatch.getValue()),
                        txtExamName.getText(),
                        dto.getStart(),
                        dto.getEnd(),
                        dto.getPassMark()
                ));
                btnCancel.fire();
                alert = new Alert(Alert.AlertType.INFORMATION, "Exam Added\nSuccessfully!");
                alert.showAndWait();
            }

        }
    }

    private boolean examNameAlreadyExists() {
        ArrayList<String> examNames = AddExamService.getAllExamNames(cmbCourse.getValue(), cmbBatch.getValue());
        String name;
        for (String exam : examNames) {
            name = "";
            if (exam.toLowerCase().contains("mandatory")) {
                for (int i = 0; i < exam.length(); i++) {
                    if (i > 10) name += exam.charAt(i);
                }
            } else {
                name = exam;
            }
            if (name.toLowerCase().contentEquals(txtExamName.getText().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private boolean detailsMissing() {
        return cmbCourse.getValue() == null
                ||
                cmbSubject.getValue() == null
                ||
                cmbBatch.getValue() == null
                ||
                txtExamDate.getValue() == null
                ||
                txtStartTime.getValue() == null
                ||
                txtEndTime.getValue() == null
                ||
                txtPassMark.getText().contentEquals("")
                ||
                txtExamName.getText().contentEquals("");
    }

    public void btnCancelOnAction(ActionEvent actionEvent) {
        tblAddExam.getSelectionModel().clearSelection();
        btnAddOrUpdate.setStyle("-fx-background-color:  #17a2b8");
        btnAddOrUpdate.setText("Add");
        cmbCourse.setValue(null);
        cmbSubject.setValue(null);
        cmbBatch.setValue(null);
        txtExamDate.setValue(null);
        txtStartTime.setValue(null);
        txtEndTime.setValue(null);
        txtPassMark.clear();
        txtExamName.clear();
        cmbCourse.setDisable(false);
        cmbBatch.setDisable(false);
        cmbSubject.setDisable(false);
        rdoMandatory.setDisable(false);
        rdoMandatory.setSelected(false);
        txtExamName.setDisable(false);
    }

    public void txtSearchOnKeyReleased(KeyEvent keyEvent) {
        ArrayList<AddExamTM> allExams = AddExamService.getAllExams();
        ObservableList<AddExamTM> items = tblAddExam.getItems();

        String text = txtSearch.getText().toLowerCase();
        if (cmbSelectField.getValue() != null) {
            if (text.contentEquals("")) {
                items.clear();
                loadAllExams();
            } else if (cmbSelectField.getValue().contentEquals("Exam ID")) {
                items.clear();
                if (allExams != null) {
                    for (AddExamTM model : allExams) {
                        if (model.getExamId().toLowerCase().contains(text)) {
                            items.add(model);
                        }
                    }
                }
            } else if (cmbSelectField.getValue().contentEquals("Date")) {
                items.clear();
                if (allExams != null) {
                    for (AddExamTM model : allExams) {
                        if (model.getDate().toLowerCase().contains(text)) {
                            items.add(model);
                        }
                    }
                }
            } else if (cmbSelectField.getValue().contentEquals("Course")) {
                items.clear();
                if (allExams != null) {
                    for (AddExamTM model : allExams) {
                        if (model.getCourse().toLowerCase().contains(text)) {
                            items.add(model);
                        }
                    }
                }
            } else if (cmbSelectField.getValue().contentEquals("Subject")) {
                items.clear();
                if (allExams != null) {
                    for (AddExamTM model : allExams) {
                        if (model.getSubject().toLowerCase().contains(text)) {
                            items.add(model);
                        }
                    }
                }
            } else if (cmbSelectField.getValue().contentEquals("Batch No")) {
                items.clear();
                if (allExams != null) {
                    for (AddExamTM model : allExams) {
                        if (String.valueOf(model.getBatchNo()).contains(text)) {
                            items.add(model);
                        }
                    }
                }
            } else if (cmbSelectField.getValue().contentEquals("Exam Name")) {
                items.clear();
                if (allExams != null) {
                    for (AddExamTM model : allExams) {
                        if (model.getExamName().toLowerCase().contains(text)) {
                            items.add(model);
                        }
                    }
                }
            } else if (cmbSelectField.getValue().contentEquals("Start(Time)")) {
                items.clear();
                if (allExams != null) {
                    for (AddExamTM model : allExams) {
                        if (model.getStartTime().toLowerCase().contains(text)) {
                            items.add(model);
                        }
                    }
                }
            } else if (cmbSelectField.getValue().contentEquals("End(Time)")) {
                items.clear();
                if (allExams != null) {
                    for (AddExamTM model : allExams) {
                        if (model.getEndTime().toLowerCase().contains(text)) {
                            items.add(model);
                        }
                    }
                }
            } else if (cmbSelectField.getValue().contentEquals("Pass Mark")) {
                items.clear();
                if (allExams != null) {
                    for (AddExamTM model : allExams) {
                        if (String.valueOf(model.getPassMark()).contains(text)) {
                            items.add(model);
                        }
                    }
                }
            }

        }
    }
}
