package lk.ijse.studentManagement.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTimePicker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.studentManagement.db.DBConnection;
import lk.ijse.studentManagement.dto.AttendanceDTO;
import lk.ijse.studentManagement.model.MarkAttendanceTM;
import lk.ijse.studentManagement.service.AddExamService;
import lk.ijse.studentManagement.service.BatchService;
import lk.ijse.studentManagement.service.MarkAttendanceService;
import lk.ijse.studentManagement.service.StudentRegistrationService;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class MarkAttendanceController implements Initializable {
    public TableView<MarkAttendanceTM> tblMarkAttendance;

    public TableColumn<MarkAttendanceTM, String> colId;
    public TableColumn<MarkAttendanceTM, String> colRegId;
    public TableColumn<MarkAttendanceTM, String> colStudentName;
    public TableColumn<MarkAttendanceTM, JFXCheckBox> colPresent;
    public TableColumn<MarkAttendanceTM, JFXCheckBox> colAbsent;
    public TableColumn<MarkAttendanceTM, JFXTimePicker> colArrival;
    public TableColumn<MarkAttendanceTM, JFXTimePicker> colDeparture;

    public JFXComboBox<String> cmbCourse;
    public JFXComboBox<String> cmbBatch;
    public JFXComboBox<String> cmbSubject;
    public JFXComboBox<String> cmbEvent;

    public JFXButton btnSetOrUpdate;
    public JFXButton btnNewOrSelect;

    public TextField txtEventName;

    public Label lblEventName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRegId.setCellValueFactory(new PropertyValueFactory<>("regId"));
        colStudentName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        colPresent.setCellValueFactory(new PropertyValueFactory<>("present"));
        colAbsent.setCellValueFactory(new PropertyValueFactory<>("absent"));
        colArrival.setCellValueFactory(new PropertyValueFactory<>("arrival"));
        colDeparture.setCellValueFactory(new PropertyValueFactory<>("departure"));
        loadAllCourses();

        cmbBatch.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                tblMarkAttendance.getItems().clear();
                loadAllEvents();
                loadAllStudents();

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
                txtEventName.setDisable(false);
                btnNewOrSelect.setDisable(false);
            }


        });

        cmbSubject.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                tblMarkAttendance.getItems().clear();
                loadAllEvents();
                loadAllStudents();
            }
        });

    }

    private void loadAllEvents() {
        tblMarkAttendance.getItems().clear();
        if (cmbSubject.getValue() != null && cmbBatch.getValue() != null) {
            cmbEvent.setDisable(false);
        }
        ObservableList<String> items = cmbEvent.getItems();
        items.clear();
        ArrayList<String> events = MarkAttendanceService.
                getAllEventNames(AddExamService.getCourseSubjectId(cmbCourse.getValue(), cmbSubject.getValue()), cmbBatch.getValue());
        if (events != null) {
            items.addAll(events);
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

        if (btnSetOrUpdate.getText().contentEquals("Set")) {
            setAttendance();
        } else {
            updateAttendance();
        }

    }

    private void updateAttendance() {
        Alert alert;
        try {
            if (cmbEvent.getValue() == null) {
                alert = new Alert(Alert.AlertType.INFORMATION, "Select Event!");
                alert.showAndWait();
            } else if (detailsMissing()) {
                alert = new Alert(Alert.AlertType.INFORMATION, "For Later Updating Leave Attendance,Time Fields\nEmpty " +
                        "Or, Complete Both Fields!");
                alert.showAndWait();
            } else if (absentSelectedWhileTimeSet()) {
                alert = new Alert(Alert.AlertType.INFORMATION, "Cannot Set Arrival Or Departure When Absent!");
                alert.showAndWait();
            } else if (timePickersAreNull()) {
                alert = new Alert(Alert.AlertType.INFORMATION, "Arrival Or Departure Time\nMissing From Certain Row(s)!");
                alert.showAndWait();
            } else {
                Connection connection = DBConnection.getInstance().getConnection();
                connection.setAutoCommit(false);

                ArrayList<AttendanceDTO> list = new ArrayList<>();

                ObservableList<MarkAttendanceTM> items = tblMarkAttendance.getItems();
                for (MarkAttendanceTM tm : items) {
                    list.add(new AttendanceDTO(
                            tm.getId(),
                            tm.getRegId(),
                            null,
                            AddExamService.getCourseSubjectId(cmbCourse.getValue(), cmbSubject.getValue()),
                            Integer.parseInt(cmbBatch.getValue()),
                            txtEventName.getText(),
                            tm.getPresent().isSelected() ? "Present" : tm.getAbsent().isSelected() ? "Absent" : "",
                            tm.getArrivalString() == null ? "" : tm.getArrivalString(),
                            tm.getDepartureString() == null ? "" : tm.getDepartureString()
                    ));
                }

                boolean isUpdated = MarkAttendanceService.updateAttendance(list);
                if (isUpdated) {
                    connection.commit();
                    alert = new Alert(Alert.AlertType.INFORMATION, "Attendance Updated Successfully!");
                    alert.showAndWait();
                    cmbEventOnAction(new ActionEvent());
                } else {
                    connection.rollback();
                }
                connection.setAutoCommit(true);

            }
        } catch (SQLException ex) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Try Again!");
            alert.showAndWait();
        }
    }

    private void setAttendance() {
        Alert alert;
        try {
            if (txtEventName.getText().contentEquals("")) {
                alert = new Alert(Alert.AlertType.INFORMATION, "Enter Event Name!\n(Lesson/Exam/Other)");
                alert.showAndWait();
            } else if (
                    !Pattern.matches("[a-zA-Z0-9-()]{4,50}", txtEventName.getText().replaceAll("\\s+", ""))
            ) {
                alert = new Alert(Alert.AlertType.INFORMATION, "Details Not Eligible!");
                alert.showAndWait();
            } else if (eventNameAlreadyExists()) {
                alert = new Alert(Alert.AlertType.INFORMATION, "Event Name Already Exists!");
                alert.showAndWait();
            } else if (detailsMissing()) {
                alert = new Alert(Alert.AlertType.INFORMATION, "For Later Updating Leave Attendance,Time Fields\nEmpty " +
                        "Or, Complete Both Fields!");
                alert.showAndWait();
            } else if (absentSelectedWhileTimeSet()) {
                alert = new Alert(Alert.AlertType.INFORMATION, "Cannot Set Arrival Or Departure When Absent!");
                alert.showAndWait();
            } else if (timePickersAreNull()) {
                alert = new Alert(Alert.AlertType.INFORMATION, "Arrival Or Departure Time\nMissing From Certain Row(s)!");
                alert.showAndWait();
            } else {

                Connection connection = DBConnection.getInstance().getConnection();
                connection.setAutoCommit(false);
                String currentDate = DateTimeFormatter.ofPattern("YYYY-MM-dd").format(LocalDate.now());
                ArrayList<AttendanceDTO> list = new ArrayList<>();
                ObservableList<MarkAttendanceTM> items = tblMarkAttendance.getItems();
                for (MarkAttendanceTM tm : items) {
                    list.add(new AttendanceDTO(
                            tm.getId(),
                            tm.getRegId(),
                            currentDate,
                            AddExamService.getCourseSubjectId(cmbCourse.getValue(), cmbSubject.getValue()),
                            Integer.parseInt(cmbBatch.getValue()),
                            txtEventName.getText(),
                            tm.getPresent().isSelected() ? "Present" : tm.getAbsent().isSelected() ? "Absent" : "",
                            tm.getArrivalString() == null ? "" : tm.getArrivalString(),
                            tm.getDepartureString() == null ? "" : tm.getDepartureString()
                    ));
                }
                boolean isSet = MarkAttendanceService.setAttendance(list);
                String event = null;
                if (isSet) {
                    connection.commit();
                    alert = new Alert(Alert.AlertType.INFORMATION, "Attendance Set Successfully!");
                    alert.showAndWait();
                    event = txtEventName.getText();
                    btnNewOrSelect.fire();
                    cmbEvent.getSelectionModel().select(event);
                    cmbEventOnAction(new ActionEvent());
                } else {
                    connection.rollback();
                }
                connection.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Try Again!");
            alert.showAndWait();
        }
    }

    private boolean eventNameAlreadyExists() {
        ArrayList<String> allEventNames = MarkAttendanceService.getAllEventNamesForTheBatch(cmbCourse.getValue(), cmbBatch.getValue());
        for (String eventName : allEventNames) {
            if (eventName.toLowerCase().contentEquals(txtEventName.getText().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private boolean absentSelectedWhileTimeSet() {
        ObservableList<MarkAttendanceTM> items = tblMarkAttendance.getItems();
        for (MarkAttendanceTM tm : items) {
            if (
                    timesSet(tm) && tm.getAbsent().isSelected()
            ) {
                return true;
            }
        }
        return false;
    }

    private boolean detailsMissing() {
        ObservableList<MarkAttendanceTM> items = tblMarkAttendance.getItems();
        for (MarkAttendanceTM tm : items) {
            if (
                    tm.getPresent().isSelected() && !timesSet(tm)
                            ||
                            !attendanceSet(tm) && timesSet(tm)
            ) {
                return true;
            }
        }
        return false;
    }

    private boolean attendanceSet(MarkAttendanceTM tm) {
        return tm.getPresent().isSelected() || tm.getAbsent().isSelected();
    }

    private boolean timesSet(MarkAttendanceTM tm) {
        return tm.getArrival().getValue() != null || tm.getDeparture().getValue() != null;
    }

    private boolean timePickersAreNull() {
        ObservableList<MarkAttendanceTM> items = tblMarkAttendance.getItems();
        for (MarkAttendanceTM tm : items) {
            if (
                    tm.getArrival().getValue() != null && tm.getDeparture().getValue() == null
                            ||
                            tm.getArrival().getValue() == null && tm.getDeparture().getValue() != null
            ) {
                return true;
            }
        }
        return false;
    }


    public void btnNewOrSelectOnAction(ActionEvent actionEvent) {
        if (btnNewOrSelect.getText().contentEquals("New")) {
            btnNewOrSelect.setText("Select");
            btnSetOrUpdate.setText("Set");
            btnSetOrUpdate.setStyle("-fx-background-color:  #17a2b8");
            cmbEvent.setDisable(true);
            cmbEvent.setVisible(false);
            lblEventName.setVisible(true);
            txtEventName.setVisible(true);
            txtEventName.setDisable(false);
            tblMarkAttendance.getItems().clear();
            cmbEvent.setValue(null);
            loadAllStudents();
        } else {
            btnNewOrSelect.setText("New");
            btnSetOrUpdate.setText("Update");
            btnSetOrUpdate.setStyle("-fx-background-color:  #28a745");
            cmbEvent.setDisable(false);
            cmbEvent.setVisible(true);
            lblEventName.setVisible(false);
            txtEventName.setVisible(false);
            txtEventName.setDisable(true);
            txtEventName.clear();
            loadAllEvents();
        }
    }

    public void loadAllStudents() {
        if (cmbBatch.getValue() != null && cmbSubject.getValue() != null && btnNewOrSelect.getText().contentEquals("Select")) {
            tblMarkAttendance.getItems().clear();
            String course = cmbCourse.getValue();
            String subject = cmbSubject.getValue();
            String batch = cmbBatch.getValue();
            ArrayList<MarkAttendanceTM> allStudents =
                    MarkAttendanceService.getAllStudents
                            (course, subject, batch);
            tblMarkAttendance.getItems().addAll(allStudents);
        }
    }

    public void cmbEventOnAction(ActionEvent actionEvent) {
        tblMarkAttendance.getItems().clear();
        if (cmbEvent.getValue() != null && btnNewOrSelect.getText().contentEquals("New")) {
            String course = cmbCourse.getValue();
            String subject = cmbSubject.getValue();
            String batch = cmbBatch.getValue();
            String event = cmbEvent.getValue();
            ArrayList<MarkAttendanceTM> allStudents =
                    MarkAttendanceService.getAllStudents
                            (course, subject, batch, event);
            boolean attendanceAlreadySet;
            if (allStudents != null) {
                attendanceAlreadySet = MarkAttendanceService.
                        attendanceAlreadySet(AddExamService.getCourseSubjectId(course, subject), batch, event);

                if (attendanceAlreadySet) {
                    for (MarkAttendanceTM tm : allStudents) {
                        if (tm.getPresent().isSelected() || tm.getAbsent().isSelected()) {
                            tm.getPresent().setDisable(true);
                            tm.getAbsent().setDisable(true);
                            tm.getArrival().setDisable(true);
                            tm.getDeparture().setDisable(true);
                        }
                    }
                } else {
                    btnSetOrUpdate.setText("Set");
                    btnSetOrUpdate.setStyle("-fx-background-color:  #17a2b8");
                }
                tblMarkAttendance.getItems().addAll(allStudents);
            }

        }

    }
}
