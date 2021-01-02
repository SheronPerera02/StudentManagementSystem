package lk.ijse.studentManagement.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import lk.ijse.studentManagement.db.DBConnection;
import lk.ijse.studentManagement.dto.ExamMarkDTO;
import lk.ijse.studentManagement.model.MarkResultTM;
import lk.ijse.studentManagement.service.AddExamService;
import lk.ijse.studentManagement.service.BatchService;
import lk.ijse.studentManagement.service.MarkResultService;
import lk.ijse.studentManagement.service.StudentRegistrationService;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class MarkResultController implements Initializable {

    public TableView<MarkResultTM> tblMarkResult;

    public TableColumn<MarkResultTM, String> colId;
    public TableColumn<MarkResultTM, String> colRegId;
    public TableColumn<MarkResultTM, String> colStudentName;
    public TableColumn<MarkResultTM, TextField> colMarks;

    public JFXComboBox<String> cmbCourse;
    public JFXComboBox<String> cmbBatch;
    public JFXComboBox<String> cmbSubject;
    public JFXComboBox<String> cmbExam;

    public JFXButton btnSetOrUpdate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colRegId.setCellValueFactory(new PropertyValueFactory<>("regId"));
        colStudentName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        colMarks.setCellValueFactory(new PropertyValueFactory<>("marks"));

        loadAllCourses();

        cmbBatch.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                tblMarkResult.getItems().clear();
                loadAllExams();

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
                cmbBatch.setDisable(false);
                cmbSubject.setDisable(false);
            }

        });
        cmbSubject.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                tblMarkResult.getItems().clear();
                loadAllExams();
            }
        });
    }

    private void loadAllExams() {
        if (cmbSubject.getValue() != null && cmbBatch.getValue() != null) {
            cmbExam.setDisable(false);
        }
        ObservableList<String> items = cmbExam.getItems();
        items.clear();
        ArrayList<String> exams = MarkResultService.
                getAllExamNames(AddExamService.getCourseSubjectId(cmbCourse.getValue(), cmbSubject.getValue()), cmbBatch.getValue());
        if (exams != null) {
            items.addAll(exams);
        }
    }

    private void loadAllCourses() {
        ArrayList<String> allCourses = BatchService.getAllCourses();
        ObservableList<String> items = cmbCourse.getItems();
        if (allCourses != null) {
            items.addAll(allCourses);
        }
    }

    public void btnSetOrUpdateOnAction(ActionEvent actionEvent) {
        Alert alert;
        try {
            if (btnSetOrUpdate.getText().contentEquals("Set")) {
                setMarks();
            } else {
                updateMarks();
            }
        } catch (Exception ex) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Try Again!");
            alert.showAndWait();
        }
    }

    private void updateMarks() {
        Alert alert;

        if (detailsNotEligible()) {

            alert = new Alert(Alert.AlertType.INFORMATION, "Input Numerical Values For Marks!\nIn Case Of Absence " +
                    "Leave The Field Empty");
            alert.showAndWait();

            ObservableList<MarkResultTM> items = tblMarkResult.getItems();
            for (MarkResultTM tm : items) {
                if (!Pattern.matches("[0-9]{1,3}", tm.getMarks().getText())) {
                    tm.setMarks("");
                }
            }

        } else {
            boolean isMandatory = MarkResultService.isMandatory(AddExamService.getCourseSubjectId(cmbCourse.getValue(), cmbSubject.getValue()),
                    cmbBatch.getValue(), cmbExam.getValue());

            String examId = MarkResultService.getExamId(AddExamService.getCourseSubjectId(cmbCourse.getValue(), cmbSubject.getValue()),
                    cmbBatch.getValue(), isMandatory ? "(Mandatory)" + cmbExam.getValue() : cmbExam.getValue());

            ObservableList<MarkResultTM> items = tblMarkResult.getItems();
            ArrayList<ExamMarkDTO> results = new ArrayList<>();
            int marks;
            for (MarkResultTM tm : items) {
                marks = tm.getMarks().getText().contentEquals("") ? 0 : Integer.parseInt(tm.getMarks().getText());
                results.add(new ExamMarkDTO(
                        tm.getId(),
                        tm.getRegId(),
                        examId,
                        marks,
                        marks == 0 ? "abs" : marks >= MarkResultService.getPassMark(examId) ? "Pass" : "Fail"

                ));
            }

            try {
                Connection connection = DBConnection.getInstance().getConnection();
                connection.setAutoCommit(false);
                boolean marksUpdated = MarkResultService.updateExamMarks(results);
                if (marksUpdated) {
                    connection.commit();
                    cmbExamOnAction(new ActionEvent());
                    alert = new Alert(Alert.AlertType.INFORMATION, "Results Updated Successfully!");
                    alert.showAndWait();
                } else {
                    connection.rollback();
                }
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    private void setMarks() {
        Alert alert;
        boolean isMandatory = MarkResultService.isMandatory(AddExamService.getCourseSubjectId(cmbCourse.getValue(), cmbSubject.getValue()),
                cmbBatch.getValue(), cmbExam.getValue());

        String examId = MarkResultService.getExamId(AddExamService.getCourseSubjectId(cmbCourse.getValue(), cmbSubject.getValue()),
                cmbBatch.getValue(), isMandatory ? "(Mandatory)" + cmbExam.getValue() : cmbExam.getValue());

        if (setMarksNotAllowed(examId)) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Cannot Set Marks Prior To Exam Date!");
            alert.showAndWait();
        } else if (detailsNotEligible()) {

            alert = new Alert(Alert.AlertType.INFORMATION, "Input Numerical Values For Marks!\nIn Case Of Absence " +
                    "Leave The Field Empty");
            alert.showAndWait();

            ObservableList<MarkResultTM> items = tblMarkResult.getItems();
            for (MarkResultTM tm : items) {
                if (!Pattern.matches("[0-9]{1,3}", tm.getMarks().getText())) {
                    tm.setMarks("");
                }
            }

        } else {


            ObservableList<MarkResultTM> items = tblMarkResult.getItems();
            ArrayList<ExamMarkDTO> results = new ArrayList<>();
            int marks;
            for (MarkResultTM tm : items) {
                marks = tm.getMarks().getText().contentEquals("") ? 0 : Integer.parseInt(tm.getMarks().getText());
                results.add(new ExamMarkDTO(
                        tm.getId(),
                        tm.getRegId(),
                        examId,
                        marks,
                        marks == 0 ? "abs" : marks >= MarkResultService.getPassMark(examId) ? "Pass" : "Fail"

                ));
            }
            try {
                Connection connection = DBConnection.getInstance().getConnection();
                connection.setAutoCommit(false);
                boolean marksSet = MarkResultService.setExamMarks(results);
                if (marksSet) {
                    connection.commit();
                    cmbExamOnAction(new ActionEvent());
                    alert = new Alert(Alert.AlertType.INFORMATION, "Results Set Successfully!");
                    alert.showAndWait();
                } else {
                    connection.rollback();
                }
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    private boolean setMarksNotAllowed(String examId) {
        boolean isAllowed = MarkResultService.getDateVerification(examId);
        return !isAllowed;
    }

    private boolean detailsNotEligible() {
        ObservableList<MarkResultTM> items = tblMarkResult.getItems();
        for (MarkResultTM tm : items) {
            if (
                    !tm.getMarks().getText().contentEquals("")
                            &&
                            !Pattern.matches("[0-9]{1,3}", tm.getMarks().getText())
            ) {
                return true;
            }
        }
        return false;
    }


    public void cmbExamOnAction(ActionEvent actionEvent) {
        if (cmbExam.getValue() != null) {
            boolean mandatory = MarkResultService.isMandatory(AddExamService.getCourseSubjectId(cmbCourse.getValue(), cmbSubject.getValue()),
                    cmbBatch.getValue(), cmbExam.getValue());

            String examId = MarkResultService.getExamId(AddExamService.getCourseSubjectId(cmbCourse.getValue(), cmbSubject.getValue()),
                    cmbBatch.getValue(), mandatory ? "(Mandatory)" + cmbExam.getValue() : cmbExam.getValue());

            String exam = mandatory ? "(Mandatory)" + cmbExam.getValue() : cmbExam.getValue();

            ArrayList<MarkResultTM> allStudents =
                    MarkResultService.getAllStudents
                            (cmbCourse.getValue(), cmbSubject.getValue(), cmbBatch.getValue(), exam);
            boolean marksAlreadySet;
            if (allStudents != null) {
                marksAlreadySet = MarkResultService.marksAlreadySet(examId);

                if (marksAlreadySet) {
                    int disableCount = 0;
                    for (MarkResultTM tm : allStudents) {
                        if (!tm.getMarks().getText().contentEquals("0")) {
                            tm.getMarks().setDisable(true);
                            disableCount++;
                        }
                    }
                    if (disableCount == allStudents.size()) {
                        btnSetOrUpdate.setText("Set");
                        btnSetOrUpdate.setStyle("-fx-background-color:  #17a2b8");
                    } else {
                        btnSetOrUpdate.setText("Update");
                        btnSetOrUpdate.setStyle("-fx-background-color:  #28a745");
                    }

                } else {
                    btnSetOrUpdate.setText("Set");
                    btnSetOrUpdate.setStyle("-fx-background-color:  #17a2b8");
                }
                tblMarkResult.getItems().clear();
                tblMarkResult.getItems().addAll(allStudents);
            }

        }

    }
}
