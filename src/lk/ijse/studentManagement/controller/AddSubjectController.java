package lk.ijse.studentManagement.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.studentManagement.db.DBConnection;
import lk.ijse.studentManagement.dto.CourseSubjectDTO;
import lk.ijse.studentManagement.dto.SubjectDTO;
import lk.ijse.studentManagement.model.AddSubjectTM;
import lk.ijse.studentManagement.service.BatchService;
import lk.ijse.studentManagement.service.SubjectService;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AddSubjectController implements Initializable {
    public JFXComboBox<String> cmbCourse;
    public JFXComboBox<String> cmbSubject;

    public JFXTextField txtSubject;
    public TextField txtSemester;

    public JFXButton btnAddOrUpdate;
    public JFXButton btnCancel;
    public JFXButton btnNewOrSelect;

    public TableView<AddSubjectTM> tblCourseSubject;

    public TableColumn<AddSubjectTM, String> colId;
    public TableColumn<AddSubjectTM, String> colCourse;
    public TableColumn<AddSubjectTM, String> colSubject;
    public TableColumn<AddSubjectTM, String> colSemester;

    public Label lblOptionality;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCourse.setCellValueFactory(new PropertyValueFactory<>("course"));
        colSubject.setCellValueFactory(new PropertyValueFactory<>("subject"));
        colSemester.setCellValueFactory(new PropertyValueFactory<>("semester"));
        loadAllCourses();
        loadTblCourseSubject();
        loadCmbSubject();
        tblCourseSubject.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AddSubjectTM>() {
            @Override
            public void changed(ObservableValue<? extends AddSubjectTM> observable, AddSubjectTM oldValue, AddSubjectTM newValue) {
                AddSubjectTM addSubjectTM = tblCourseSubject.getSelectionModel().getSelectedItem();
                if (addSubjectTM != null) {
                    cmbCourse.setValue(addSubjectTM.getCourse());
                    txtSubject.setText(addSubjectTM.getSubject());
                    txtSemester.setText(addSubjectTM.getSemester().contentEquals("-")?"":addSubjectTM.getSemester());
                    cmbCourse.setDisable(true);
                    txtSubject.setDisable(true);
                    btnAddOrUpdate.setText("Update");
                    btnAddOrUpdate.setStyle("-fx-background-color: #28a745");
                    newSubjectField();
                    btnNewOrSelect.setDisable(true);
                    btnNewOrSelect.setVisible(false);
                    txtSubject.setDisable(true);
                }
            }
        });

    }

    private void loadCmbSubject() {
        ArrayList<String> allSubjectName = SubjectService.getAllSubjectName();
        if (allSubjectName != null) {
            cmbSubject.getItems().addAll(allSubjectName);
        }
    }

    private void loadTblCourseSubject() {
        ArrayList<AddSubjectTM> allCourseSubjects = SubjectService.getAllCourseSubjects();
        ObservableList<AddSubjectTM> items = tblCourseSubject.getItems();
        if (allCourseSubjects != null) {
            for (AddSubjectTM allCourseSubject : allCourseSubjects) {
                items.add(new AddSubjectTM(
                        allCourseSubject.getId(),
                        allCourseSubject.getCourse(),
                        allCourseSubject.getSubject(),
                        allCourseSubject.getSemester()
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

    public void txtSubjectOnAction(ActionEvent actionEvent) {
        txtSemester.requestFocus();
    }

    public void btnCancelOnAction(ActionEvent actionEvent) {
        tblCourseSubject.getSelectionModel().clearSelection();
        cmbCourse.setValue(null);
        btnNewOrSelect.setDisable(false);
        btnNewOrSelect.setVisible(true);
        cmbSubject.setValue(null);
        txtSubject.clear();
        txtSemester.clear();
        btnAddOrUpdate.setText("Add");
        cmbCourse.setDisable(false);
        txtSubject.setDisable(false);
        btnAddOrUpdate.setStyle("-fx-background-color:  #17a2b8");
        selectSubjectField();
        txtSubject.setDisable(false);
    }

    public void btnAddOrUpdateOnAction(ActionEvent actionEvent) {

        if (btnAddOrUpdate.getText().contentEquals("Add")) {
            addCourseSubject();
        } else {
            updateCourseSubject();
        }
    }

    private void updateCourseSubject() {
        Alert alert;
        if (detailsNotEligible()) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Details Not Eligible!");
            alert.showAndWait();
        } else {
            String semester = txtSemester.getText().contentEquals("") ? "-" : txtSemester.getText();
            CourseSubjectDTO dto = new CourseSubjectDTO(
                    tblCourseSubject.getSelectionModel().getSelectedItem().getId(),
                    BatchService.getCourseId(cmbCourse.getValue()),
                    SubjectService.getSubjectId(txtSubject.getText()),
                    semester
            );
            boolean isUpdated = SubjectService.updateCourseSubject(dto);
            if (isUpdated) {

                ObservableList<AddSubjectTM> items = tblCourseSubject.getItems();
                items.set(tblCourseSubject.getSelectionModel().getSelectedIndex(), new AddSubjectTM(
                        dto.getId(),
                        BatchService.getCourseName(dto.getCourseId()),
                        txtSubject.getText(),
                        semester
                ));
                btnCancel.fire();
                alert = new Alert(Alert.AlertType.INFORMATION, "Course_Subject Record Updated\nSuccessfully!");
                alert.showAndWait();
            }

        }
    }

    private void addCourseSubject() {
        Alert alert;
        if (detailsMissing()) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Complete The Form!");
            alert.showAndWait();
        } else if (detailsNotEligible()) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Details Not Eligible!");
            alert.showAndWait();
        } else {

            Connection connection;
            try {
                connection = DBConnection.getInstance().getConnection();
                connection.setAutoCommit(false);

                String semester = txtSemester.getText().contentEquals("") ? "-" : txtSemester.getText();
                boolean courseSubjectAdded;
                boolean subjectAdded;
                String nextCSID = SubjectService.getNextCSID();
                String nextSubID = SubjectService.getNextSubID();
                if (nextCSID == null) {
                    nextCSID = "CS0001";
                    nextSubID = "Sub001";
                    subjectAdded = SubjectService.addSubject(new SubjectDTO(
                            nextSubID,
                            btnNewOrSelect.getText().contentEquals("New")?cmbSubject.getValue() :txtSubject.getText()
                    ));
                    courseSubjectAdded = SubjectService.addCourseSubject(new CourseSubjectDTO(
                            nextCSID,
                            BatchService.getCourseId(cmbCourse.getValue()),
                            nextSubID,
                            semester
                    ));
                } else {


                    if (SubjectService.subjectAlreadyExists(
                            btnNewOrSelect.getText().contentEquals("New")?cmbSubject.getValue() :txtSubject.getText()
                    )) {
                        subjectAdded = true;
                        courseSubjectAdded = SubjectService.addCourseSubject(new CourseSubjectDTO(
                                nextCSID,
                                BatchService.getCourseId(cmbCourse.getValue()),
                                SubjectService.getSubjectId(btnNewOrSelect.getText().contentEquals("New")?cmbSubject.getValue()
                                        :txtSubject.getText()),
                                semester
                        ));
                    } else {
                        subjectAdded = SubjectService.addSubject(new SubjectDTO(
                                nextSubID,
                                btnNewOrSelect.getText().contentEquals("New")?cmbSubject.getValue() :txtSubject.getText()
                        ));
                        courseSubjectAdded = SubjectService.addCourseSubject(new CourseSubjectDTO(
                                nextCSID,
                                BatchService.getCourseId(cmbCourse.getValue()),
                                nextSubID,
                                semester
                        ));
                    }
                }
                ObservableList<AddSubjectTM> items = tblCourseSubject.getItems();
                if (courseSubjectAdded && subjectAdded) {
                    connection.commit();
                    connection.setAutoCommit(true);
                    items.add(new AddSubjectTM(
                            nextCSID,
                            cmbCourse.getValue(),
                            btnNewOrSelect.getText().contentEquals("New")?cmbSubject.getValue() :txtSubject.getText(),
                            semester
                    ));
                    cmbSubject.getItems().clear();
                    loadCmbSubject();
                    btnCancel.fire();
                    alert = new Alert(Alert.AlertType.INFORMATION, "Subject Added\nSuccessfully!");
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

    private boolean detailsNotEligible() {
        boolean subjectIsValid = Pattern.matches("[a-zA-Z]{2,30}", txtSubject.getText().replaceAll("\\s+", ""));
        return !(subjectIsValid
                &&
                txtSemester.getText().contentEquals("") || Pattern.matches("[0-9]{1,3}", txtSemester.getText()));
    }

    private boolean detailsMissing() {
        return cmbCourse.getValue() == null
                ||
                btnNewOrSelect.getText().contentEquals("Select")&&txtSubject.getText().contentEquals("")
                ||
                btnNewOrSelect.getText().contentEquals("New")&&cmbSubject.getValue()==null;
    }


    public void btnNewOrSelectOnAction(ActionEvent actionEvent) {
        if(txtSubject.isVisible()){
            selectSubjectField();
        } else{
            newSubjectField();
        }
    }
    private void selectSubjectField(){
        cmbSubject.setDisable(false);
        cmbSubject.setVisible(true);
        txtSubject.setDisable(true);
        txtSubject.setVisible(false);
        btnNewOrSelect.setText("New");
    }
    private void newSubjectField(){
        cmbSubject.setDisable(true);
        cmbSubject.setVisible(false);
        txtSubject.setDisable(false);
        txtSubject.setVisible(true);
        btnNewOrSelect.setText("Select");
    }
}
