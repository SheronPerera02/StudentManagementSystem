package lk.ijse.studentManagement.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import lk.ijse.studentManagement.model.ViewAttendanceEventTM;
import lk.ijse.studentManagement.model.ViewAttendanceTM;
import lk.ijse.studentManagement.service.AddExamService;
import lk.ijse.studentManagement.service.BatchService;
import lk.ijse.studentManagement.service.StudentRegistrationService;
import lk.ijse.studentManagement.service.ViewAttendanceService;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ViewAttendanceController implements Initializable {
    public TableView<ViewAttendanceTM> tblViewAttendance;
    public TableView<ViewAttendanceEventTM> tblEvent;

    public TableColumn<ViewAttendanceEventTM, String> colEventName;
    public TableColumn<ViewAttendanceEventTM, String> colDate;

    public TableColumn<ViewAttendanceTM, String> colRegId;
    public TableColumn<ViewAttendanceTM, String> colStudentName;
    public TableColumn<ViewAttendanceTM, String> colAttendance;
    public TableColumn<ViewAttendanceTM, String> colDeparture;
    public TableColumn<ViewAttendanceTM, String> colArrival;

    public JFXComboBox<String> cmbSelectFieldEvent;
    public JFXComboBox<String> cmbSelectFieldAttendance;
    public JFXComboBox<String> cmbCourse;
    public JFXComboBox<String> cmbSubject;
    public JFXComboBox<String> cmbBatch;

    public TextField txtSearchTableEvent;
    public TextField txtSearchTableAttendance;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadAllCourses();
        loadCmbSelectFieldAttendance();
        loadCmbSelectFieldEvent();

        colEventName.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("eventDate"));

        colRegId.setCellValueFactory(new PropertyValueFactory<>("regId"));
        colStudentName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        colAttendance.setCellValueFactory(new PropertyValueFactory<>("attendance"));
        colArrival.setCellValueFactory(new PropertyValueFactory<>("arrival"));
        colDeparture.setCellValueFactory(new PropertyValueFactory<>("departure"));

        cmbBatch.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                tblEvent.getItems().clear();
                tblViewAttendance.getItems().clear();
                loadAllEvents();

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
                tblEvent.getItems().clear();
                tblViewAttendance.getItems().clear();
                loadAllEvents();
            }
        });
        tblEvent.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ViewAttendanceEventTM>() {
            @Override
            public void changed(ObservableValue<? extends ViewAttendanceEventTM> observable, ViewAttendanceEventTM oldValue, ViewAttendanceEventTM newValue) {
                if (tblEvent.getSelectionModel().getSelectedItem() != null) {
                    String eventName = tblEvent.getSelectionModel().getSelectedItem().getEventName();
                    String courseSubjectId = AddExamService.getCourseSubjectId(cmbCourse.getValue(), cmbSubject.getValue());
                    String batch = cmbBatch.getValue();
                    ArrayList<ViewAttendanceTM> attendance = ViewAttendanceService.getAttendance(eventName, courseSubjectId, batch);
                    tblViewAttendance.getItems().clear();
                    tblViewAttendance.getItems().addAll(attendance);
                }
            }
        });
    }

    private void loadAllEvents() {
        if (cmbBatch.getValue() != null && cmbSubject != null) {
            String courseSubjectId = AddExamService.getCourseSubjectId(cmbCourse.getValue(), cmbSubject.getValue());
            ArrayList<ViewAttendanceEventTM> events = ViewAttendanceService.getAllEvents(courseSubjectId, cmbBatch.getValue());
            tblEvent.getItems().clear();
            tblViewAttendance.getItems().clear();
            if (events != null) {
                tblEvent.getItems().addAll(events);
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

    private void loadCmbSelectFieldEvent() {
        ObservableList<TableColumn<ViewAttendanceEventTM, ?>> columns = tblEvent.getColumns();
        for (TableColumn<ViewAttendanceEventTM, ?> column : columns) {
            cmbSelectFieldEvent.getItems().add(column.getText());
        }
    }

    private void loadCmbSelectFieldAttendance() {
        ObservableList<TableColumn<ViewAttendanceTM, ?>> columns = tblViewAttendance.getColumns();
        for (TableColumn<ViewAttendanceTM, ?> column : columns) {
            cmbSelectFieldAttendance.getItems().add(column.getText());
        }
    }


    public void txtSearchEventOnKeyReleased(KeyEvent keyEvent) {
        tblViewAttendance.getItems().clear();
        tblEvent.getSelectionModel().clearSelection();
        if (cmbCourse.getValue() != null && cmbSubject.getValue() != null && cmbBatch.getValue() != null) {
            String courseSubjectId = AddExamService.getCourseSubjectId(cmbCourse.getValue(), cmbSubject.getValue());
            ArrayList<ViewAttendanceEventTM> events = ViewAttendanceService.getAllEvents(courseSubjectId, cmbBatch.getValue());
            ObservableList<ViewAttendanceEventTM> items = tblEvent.getItems();

            String text = txtSearchTableEvent.getText().toLowerCase();
            if (cmbSelectFieldEvent.getValue() != null) {
                if (text.contentEquals("")) {
                    items.clear();
                    loadAllEvents();
                } else if (cmbSelectFieldEvent.getValue().contentEquals("Event Name")) {
                    items.clear();
                    if (events != null) {
                        for (ViewAttendanceEventTM model : events) {
                            if (model.getEventName().toLowerCase().contains(text)) {
                                items.add(model);
                            }
                        }
                    }
                } else if (cmbSelectFieldEvent.getValue().contentEquals("Date")) {
                    items.clear();
                    if (events != null) {
                        for (ViewAttendanceEventTM model : events) {
                            if (model.getEventDate().toLowerCase().contains(text)) {
                                items.add(model);
                            }
                        }
                    }
                }
            }
        }
    }

    public void txtSearchAttendanceOnKeyReleased(KeyEvent keyEvent) {
        if (cmbCourse.getValue() != null && cmbSubject.getValue() != null && cmbBatch.getValue() != null) {
            String eventName = tblEvent.getSelectionModel().getSelectedItem().getEventName();
            String courseSubjectId = AddExamService.getCourseSubjectId(cmbCourse.getValue(), cmbSubject.getValue());
            String batch = cmbBatch.getValue();
            ArrayList<ViewAttendanceTM> attendance = ViewAttendanceService.getAttendance(eventName, courseSubjectId, batch);
            ObservableList<ViewAttendanceTM> items = tblViewAttendance.getItems();

            String text = txtSearchTableAttendance.getText().toLowerCase();
            if (cmbSelectFieldAttendance.getValue() != null) {
                if (text.contentEquals("")) {
                    items.clear();
                    tblViewAttendance.getItems().clear();
                    tblViewAttendance.getItems().addAll(attendance);
                } else if (cmbSelectFieldAttendance.getValue().contentEquals("Reg ID")) {
                    items.clear();
                    if (attendance != null) {
                        for (ViewAttendanceTM model : attendance) {
                            if (model.getRegId().toLowerCase().contains(text)) {
                                items.add(model);
                            }
                        }
                    }
                } else if (cmbSelectFieldAttendance.getValue().contentEquals("Student Name")) {
                    items.clear();
                    if (attendance != null) {
                        for (ViewAttendanceTM model : attendance) {
                            if (model.getStudentName().toLowerCase().contains(text)) {
                                items.add(model);
                            }
                        }
                    }
                } else if (cmbSelectFieldAttendance.getValue().contentEquals("Attendance")) {
                    items.clear();
                    if (attendance != null) {
                        for (ViewAttendanceTM model : attendance) {
                            if (model.getAttendance().toLowerCase().contains(text)) {
                                items.add(model);
                            }
                        }
                    }
                } else if (cmbSelectFieldAttendance.getValue().contentEquals("Arrival")) {
                    items.clear();
                    if (attendance != null) {
                        for (ViewAttendanceTM model : attendance) {
                            if (model.getArrival().contains(text)) {
                                items.add(model);
                            }
                        }
                    }
                } else if (cmbSelectFieldAttendance.getValue().contentEquals("Departure")) {
                    items.clear();
                    if (attendance != null) {
                        for (ViewAttendanceTM model : attendance) {
                            if (model.getDeparture().contains(text)) {
                                items.add(model);
                            }
                        }
                    }
                }
            }
        }
    }
}
