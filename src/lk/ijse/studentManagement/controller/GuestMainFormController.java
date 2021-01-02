package lk.ijse.studentManagement.controller;

import com.jfoenix.controls.JFXComboBox;
import de.jensd.fx.glyphs.octicons.OctIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lk.ijse.studentManagement.model.ExamDetailTM;
import lk.ijse.studentManagement.model.GuestMainFormTM;
import lk.ijse.studentManagement.model.PendingExamTM;
import lk.ijse.studentManagement.service.BatchService;
import lk.ijse.studentManagement.service.GuestMainFormService;
import lk.ijse.studentManagement.service.StudentRegistrationService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GuestMainFormController implements Initializable {

    public TableView<GuestMainFormTM> tblGuestMainForm;

    public TableColumn<GuestMainFormTM, String> colRegId;
    public TableColumn<GuestMainFormTM, String> colStudentName;

    public ComboBox<String> cmbCourse;
    public ComboBox<String> cmbBatch;

    public TableView<PendingExamTM> tblPendingExam;

    public TableColumn<PendingExamTM, String> colSubject;
    public TableColumn<PendingExamTM, String> colExam;
    public TableColumn<PendingExamTM, String> colDate;
    public TableColumn<PendingExamTM, String> colStart;
    public TableColumn<PendingExamTM, String> colEnd;

    public TableView<ExamDetailTM> tblExamDetail;

    public TableColumn<ExamDetailTM, String> colSubjectName;
    public TableColumn<ExamDetailTM, String> colExamName;
    public TableColumn<ExamDetailTM, String> colExamDate;
    public TableColumn<ExamDetailTM, Integer> colMarks;
    public TableColumn<ExamDetailTM, String> colExamState;

    public TextField txtSearch;

    public JFXComboBox<String> cmbSelectField;

    public OctIconView iconExit;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadAllCourses();
        loadCmbSelectField();

        colRegId.setCellValueFactory(new PropertyValueFactory<>("regId"));
        colStudentName.setCellValueFactory(new PropertyValueFactory<>("studentName"));

        colSubject.setCellValueFactory(new PropertyValueFactory<>("subject"));
        colExam.setCellValueFactory(new PropertyValueFactory<>("exam"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        colEnd.setCellValueFactory(new PropertyValueFactory<>("end"));

        colSubjectName.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        colExamName.setCellValueFactory(new PropertyValueFactory<>("examName"));
        colExamDate.setCellValueFactory(new PropertyValueFactory<>("examDate"));
        colMarks.setCellValueFactory(new PropertyValueFactory<>("marks"));
        colExamState.setCellValueFactory(new PropertyValueFactory<>("examState"));

        cmbCourse.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                clearTables();
                cmbBatch.getItems().clear();
                ArrayList<String> allBatches = StudentRegistrationService.getAllBatches(cmbCourse.getValue());
                if (allBatches != null) {
                    cmbBatch.getItems().addAll(allBatches);
                }
                cmbBatch.setDisable(false);
            }

        });
        cmbBatch.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                clearTables();
                tblGuestMainForm.getItems().clear();
                loadAllStudents();

            }
        });
        tblGuestMainForm.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<GuestMainFormTM>() {
            @Override
            public void changed(ObservableValue<? extends GuestMainFormTM> observable, GuestMainFormTM oldValue, GuestMainFormTM newValue) {
                if (tblGuestMainForm.getSelectionModel().getSelectedItem() != null) {
                    clearTables();
                    loadTables(tblGuestMainForm.getSelectionModel().getSelectedItem().getRegId());
                }
            }
        });
    }

    private void clearTables() {
        tblExamDetail.getItems().clear();
        tblPendingExam.getItems().clear();
    }

    private void loadAllStudents() {
        ArrayList<GuestMainFormTM> allStudents = GuestMainFormService.getAllStudents(cmbCourse.getValue(), cmbBatch.getValue());
        if (allStudents != null) {
            tblGuestMainForm.getItems().addAll(allStudents);
        }
    }

    private void loadAllCourses() {
        ArrayList<String> allCourses = BatchService.getAllCourses();
        ObservableList<String> items = cmbCourse.getItems();
        if (allCourses != null) {
            items.addAll(allCourses);
        }
    }

    private void loadTables(String regId) {
        ArrayList<String> courseAndBatch = GuestMainFormService.getCourseAndBatch(regId);
        loadPendingExamTable(courseAndBatch.get(0), courseAndBatch.get(1));
        loadExamDetailTable(regId);
    }

    private void loadPendingExamTable(String course, String batch) {
        ArrayList<PendingExamTM> pendingExams = GuestMainFormService.getAllPendingExams(course, batch);
        if (pendingExams != null) {
            tblPendingExam.getItems().addAll(pendingExams);
        }
    }

    private void loadExamDetailTable(String regId) {
        ArrayList<ExamDetailTM> examDetails = GuestMainFormService.getAllExamDetails(regId);
        if (examDetails != null) {
            tblExamDetail.getItems().addAll(examDetails);
        }
    }

    private void loadCmbSelectField() {
        ObservableList<TableColumn<ExamDetailTM, ?>> columns = tblExamDetail.getColumns();
        for (TableColumn<ExamDetailTM, ?> column : columns) {
            cmbSelectField.getItems().add(column.getText());
        }
    }


    public void txtSearchEventOnKeyReleased(KeyEvent keyEvent) {
        ArrayList<ExamDetailTM> examDetails = GuestMainFormService.
                getAllExamDetails(tblGuestMainForm.getSelectionModel().getSelectedItem().getRegId());
        ObservableList<ExamDetailTM> items = tblExamDetail.getItems();

        String text = txtSearch.getText().toLowerCase();
        if (examDetails != null) {
            if (cmbSelectField.getValue() != null) {
                if (text.contentEquals("")) {
                    items.clear();
                    items.addAll(examDetails);
                } else if (cmbSelectField.getValue().contentEquals("Subject")) {
                    items.clear();
                    if (examDetails != null) {
                        for (ExamDetailTM model : examDetails) {
                            if (model.getSubjectName().toLowerCase().contains(text)) {
                                items.add(model);
                            }
                        }
                    }
                } else if (cmbSelectField.getValue().contentEquals("Exam Name")) {
                    items.clear();
                    if (examDetails != null) {
                        for (ExamDetailTM model : examDetails) {
                            if (model.getExamName().toLowerCase().contains(text)) {
                                items.add(model);
                            }
                        }
                    }
                } else if (cmbSelectField.getValue().contentEquals("Exam Date")) {
                    items.clear();
                    if (examDetails != null) {
                        for (ExamDetailTM model : examDetails) {
                            if (model.getExamDate().contains(text)) {
                                items.add(model);
                            }
                        }
                    }
                } else if (cmbSelectField.getValue().contentEquals("Marks")) {
                    items.clear();
                    if (examDetails != null) {
                        for (ExamDetailTM model : examDetails) {
                            if (String.valueOf(model.getMarks()).contains(text)) {
                                items.add(model);
                            }
                        }
                    }
                } else if (cmbSelectField.getValue().contentEquals("Exam State")) {
                    items.clear();
                    if (examDetails != null) {
                        for (ExamDetailTM model : examDetails) {
                            if (model.getExamState().toLowerCase().contains(text)) {
                                items.add(model);
                            }
                        }
                    }
                }
            }
        }
    }


    public void iconExitOnMouseClicked(MouseEvent mouseEvent) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/lk/ijse/studentManagement/views/LandingScreen.fxml"));
            Stage newStage = new Stage();
            newStage.setScene(new Scene(parent));
            newStage.setTitle("Student Management");
            newStage.show();

            Stage window = (Stage) this.iconExit.getScene().getWindow();
            window.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
