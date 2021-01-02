package lk.ijse.studentManagement.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import lk.ijse.studentManagement.model.ViewResultTM;
import lk.ijse.studentManagement.service.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ViewResultController implements Initializable {
    public TableView<ViewResultTM> tblViewResult;

    public TableColumn<ViewResultTM, String> colRegId;
    public TableColumn<ViewResultTM, String> colStudentName;
    public TableColumn<ViewResultTM, Integer> colMarks;
    public TableColumn<ViewResultTM, String> colExamState;

    public JFXComboBox<String> cmbCourse;
    public JFXComboBox<String> cmbBatch;
    public JFXComboBox<String> cmbSubject;
    public JFXComboBox<String> cmbExam;
    public JFXComboBox<String> cmbSelectField;

    public TextField txtSearch;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadAllCourses();
        loadCmbSelectField();

        colRegId.setCellValueFactory(new PropertyValueFactory<>("regId"));
        colStudentName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        colMarks.setCellValueFactory(new PropertyValueFactory<>("marks"));
        colExamState.setCellValueFactory(new PropertyValueFactory<>("examState"));

        cmbBatch.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                tblViewResult.getItems().clear();
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
                tblViewResult.getItems().clear();
                loadAllExams();
            }
        });

    }

    private void loadCmbSelectField() {
        ObservableList<TableColumn<ViewResultTM, ?>> columns = tblViewResult.getColumns();
        for (TableColumn<ViewResultTM, ?> column : columns) {
            cmbSelectField.getItems().add(column.getText());
        }
    }

    private void loadAllCourses() {
        ArrayList<String> allCourses = BatchService.getAllCourses();
        ObservableList<String> items = cmbCourse.getItems();
        if (allCourses != null) {
            items.addAll(allCourses);
        }
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

    public void cmbExamOnAction(ActionEvent actionEvent) {
        if (cmbExam.getValue() != null) {
            boolean mandatory = MarkResultService.isMandatory(AddExamService.getCourseSubjectId(cmbCourse.getValue(), cmbSubject.getValue()),
                    cmbBatch.getValue(), cmbExam.getValue());

            String examId = MarkResultService.getExamId(AddExamService.getCourseSubjectId(cmbCourse.getValue(), cmbSubject.getValue()),
                    cmbBatch.getValue(), mandatory ? "(Mandatory)" + cmbExam.getValue() : cmbExam.getValue());

            ArrayList<ViewResultTM> allStudents = ViewResultService.getAllStudents(examId);
            if (allStudents != null) {
                tblViewResult.getItems().clear();
                tblViewResult.getItems().addAll(allStudents);
            }
        }
    }

    public void txtSearchOnKeyReleased(KeyEvent keyEvent) {
        if (cmbExam.getValue() != null) {
            boolean mandatory = MarkResultService.isMandatory(AddExamService.getCourseSubjectId(cmbCourse.getValue(), cmbSubject.getValue()),
                    cmbBatch.getValue(), cmbExam.getValue());

            String examId = MarkResultService.getExamId(AddExamService.getCourseSubjectId(cmbCourse.getValue(), cmbSubject.getValue()),
                    cmbBatch.getValue(), mandatory ? "(Mandatory)" + cmbExam.getValue() : cmbExam.getValue());

            ArrayList<ViewResultTM> allStudents = ViewResultService.getAllStudents(examId);
            ObservableList<ViewResultTM> items = tblViewResult.getItems();

            String text = txtSearch.getText().toLowerCase();
            if (cmbSelectField.getValue() != null) {
                if (text.contentEquals("")) {
                    items.clear();
                    cmbExamOnAction(new ActionEvent());
                } else if (cmbSelectField.getValue().contentEquals("Reg ID")) {
                    items.clear();
                    if (allStudents != null) {
                        for (ViewResultTM model : allStudents) {
                            if (model.getRegId().toLowerCase().contains(text)) {
                                items.add(model);
                            }
                        }
                    }
                } else if (cmbSelectField.getValue().contentEquals("Student Name")) {
                    items.clear();
                    if (allStudents != null) {
                        for (ViewResultTM model : allStudents) {
                            if (model.getStudentName().toLowerCase().contains(text)) {
                                items.add(model);
                            }
                        }
                    }
                } else if (cmbSelectField.getValue().contentEquals("Marks")) {
                    items.clear();
                    if (allStudents != null) {
                        for (ViewResultTM model : allStudents) {
                            if (String.valueOf(model.getMarks()).contains(text)) {
                                items.add(model);
                            }
                        }
                    }
                } else if (cmbSelectField.getValue().contentEquals("Exam State")) {
                    items.clear();
                    if (allStudents != null) {
                        for (ViewResultTM model : allStudents) {
                            if (model.getExamState().toLowerCase().contains(text)) {
                                items.add(model);
                            }
                        }
                    }
                }
            }


        }
    }
}
