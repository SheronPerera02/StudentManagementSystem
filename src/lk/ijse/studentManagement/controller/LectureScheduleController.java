package lk.ijse.studentManagement.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTimePicker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.studentManagement.dto.LectureScheduleDTO;
import lk.ijse.studentManagement.model.LectureScheduleTM;
import lk.ijse.studentManagement.model.LecturerTM;
import lk.ijse.studentManagement.service.AddExamService;
import lk.ijse.studentManagement.service.BatchService;
import lk.ijse.studentManagement.service.LectureScheduleService;
import lk.ijse.studentManagement.service.StudentRegistrationService;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LectureScheduleController implements Initializable {
    public TableView<LectureScheduleTM> tblLectureSchedule;

    public TableColumn<LecturerTM, String> colId;
    public TableColumn<LecturerTM, String> colDayName;
    public TableColumn<LecturerTM, String> colLecturer;
    public TableColumn<LecturerTM, String> colCourse;
    public TableColumn<LecturerTM, Integer> colBatch;
    public TableColumn<LecturerTM, String> colSubject;
    public TableColumn<LecturerTM, String> colDescription;
    public TableColumn<LecturerTM, String> colStartTime;
    public TableColumn<LecturerTM, String> colEndTime;

    public JFXComboBox<String> cmbDay;
    public JFXComboBox<String> cmbLecturer;
    public JFXComboBox<String> cmbCourse;
    public JFXComboBox<String> cmbBatch;
    public JFXComboBox<String> cmbSubject;

    public JFXTextArea txtDescription;

    public JFXTimePicker txtStartTime;
    public JFXTimePicker txtEndTime;

    public JFXButton btnAddOrUpdate;
    public JFXButton btnCancel;
    public JFXButton btnDelete;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDayName.setCellValueFactory(new PropertyValueFactory<>("dayName"));
        colLecturer.setCellValueFactory(new PropertyValueFactory<>("lecturer"));
        colCourse.setCellValueFactory(new PropertyValueFactory<>("course"));
        colBatch.setCellValueFactory(new PropertyValueFactory<>("batch"));
        colSubject.setCellValueFactory(new PropertyValueFactory<>("subject"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colStartTime.setCellValueFactory(new PropertyValueFactory<>("start"));
        colEndTime.setCellValueFactory(new PropertyValueFactory<>("end"));
        loadAllSchedules();
        loadAllCourses();
        loadOtherComboBoxes();
        tblLectureSchedule.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<LectureScheduleTM>() {
            @Override
            public void changed(ObservableValue<? extends LectureScheduleTM> observable, LectureScheduleTM oldValue, LectureScheduleTM newValue) {
                LectureScheduleTM selectedItem = tblLectureSchedule.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    btnDelete.setVisible(true);
                    btnDelete.setDisable(false);
                    btnAddOrUpdate.setText("Update");
                    btnAddOrUpdate.setStyle("-fx-background-color: #28a745");
                    cmbDay.setValue(selectedItem.getDayName());
                    cmbLecturer.setValue(selectedItem.getLecturer());
                    cmbCourse.setValue(selectedItem.getCourse());
                    cmbBatch.setValue(String.valueOf(selectedItem.getBatch()));
                    cmbSubject.setValue(selectedItem.getSubject());
                    txtDescription.setText(selectedItem.getDescription());
                    txtStartTime.setValue(LocalTime.of(getHour(selectedItem.getStart()),
                            getMinute(selectedItem.getStart())));
                    txtEndTime.setValue(LocalTime.of(getHour(selectedItem.getEnd()),
                            getMinute(selectedItem.getEnd())));
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

    private void loadAllSchedules() {
        ArrayList<LectureScheduleTM> list = LectureScheduleService.getAllSchedules();
        if (list != null) {
            tblLectureSchedule.getItems().addAll(list);
        }
    }

    private void loadOtherComboBoxes() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Monday");
        list.add("Tuesday");
        list.add("Wednesday");
        list.add("Thursday");
        list.add("Friday");
        list.add("Saturday");
        list.add("Sunday");
        cmbDay.getItems().addAll(list);
        ArrayList<String> allLecturers = LectureScheduleService.getAllLecturers();
        cmbLecturer.getItems().addAll(allLecturers);
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
                addSchedule();
            } else {
                updateSchedule();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Try Again!");
            alert.showAndWait();
        }
    }

    private void updateSchedule() {
        Alert alert = null;
        if (detailsMissing()) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Details Missing!");
            alert.showAndWait();
        } else {
            String id = tblLectureSchedule.getSelectionModel().getSelectedItem().getId();
            String lecturerId = LectureScheduleService.getLecturerId(cmbLecturer.getValue());
            String courseSubjectId = AddExamService.getCourseSubjectId(cmbCourse.getValue(), cmbSubject.getValue());
            String start = DateTimeFormatter.ofPattern("HH:mm").format(txtStartTime.getValue());
            String end = DateTimeFormatter.ofPattern("HH:mm").format(txtEndTime.getValue());

            LectureScheduleDTO dto = new LectureScheduleDTO(
                    id,
                    cmbDay.getValue(),
                    lecturerId,
                    courseSubjectId,
                    Integer.parseInt(cmbBatch.getValue()),
                    txtDescription.getText(),
                    start,
                    end
            );
            boolean isUpdated = false;
            isUpdated = LectureScheduleService.updateSchedule(dto);
            if (isUpdated) {
                tblLectureSchedule.getItems().set(tblLectureSchedule.getSelectionModel().getSelectedIndex(), new LectureScheduleTM(
                                id,
                                cmbDay.getValue(),
                                cmbLecturer.getValue(),
                                cmbCourse.getValue(),
                                Integer.parseInt(cmbBatch.getValue()),
                                cmbSubject.getValue(),
                                txtDescription.getText(),
                                start,
                                end
                        )
                );
                btnCancel.fire();
                alert = new Alert(Alert.AlertType.INFORMATION, "Lecture Schedule Updated Successfully!");
                alert.showAndWait();
            }
        }
    }

    private void addSchedule() {
        Alert alert = null;
        if (detailsMissing()) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Details Missing!");
        } else {
            String nextId = LectureScheduleService.getNextId() == null ? "L001" : LectureScheduleService.getNextId();
            String lecturerId = LectureScheduleService.getLecturerId(cmbLecturer.getValue());
            String courseSubjectId = AddExamService.getCourseSubjectId(cmbCourse.getValue(), cmbSubject.getValue());
            String start = DateTimeFormatter.ofPattern("HH:mm").format(txtStartTime.getValue());
            String end = DateTimeFormatter.ofPattern("HH:mm").format(txtEndTime.getValue());

            LectureScheduleDTO dto = new LectureScheduleDTO(
                    nextId,
                    cmbDay.getValue(),
                    lecturerId,
                    courseSubjectId,
                    Integer.parseInt(cmbBatch.getValue()),
                    txtDescription.getText(),
                    start,
                    end
            );
            boolean isAdded = false;
            isAdded = LectureScheduleService.addSchedule(dto);
            if (isAdded) {
                tblLectureSchedule.getItems().add(new LectureScheduleTM(
                        nextId,
                        cmbDay.getValue(),
                        cmbLecturer.getValue(),
                        cmbCourse.getValue(),
                        Integer.parseInt(cmbBatch.getValue()),
                        cmbSubject.getValue(),
                        txtDescription.getText(),
                        start,
                        end
                ));
                btnCancel.fire();
                alert = new Alert(Alert.AlertType.INFORMATION, "Lecture Schedule Added Successfully!");
                alert.showAndWait();
            }

        }
    }


    private boolean detailsMissing() {
        return cmbDay.getValue() == null
                ||
                cmbLecturer.getValue() == null
                ||
                cmbCourse.getValue() == null
                ||
                cmbBatch.getValue() == null
                ||
                cmbSubject.getValue() == null
                ||
                txtStartTime.getValue() == null
                ||
                txtEndTime.getValue() == null;
    }

    public void btnCancelOnAction(ActionEvent actionEvent) {
        tblLectureSchedule.getSelectionModel().clearSelection();
        cmbDay.setValue(null);
        cmbLecturer.setValue(null);
        cmbCourse.setValue(null);
        cmbBatch.setValue(null);
        cmbSubject.setValue(null);
        txtDescription.clear();
        txtStartTime.setValue(null);
        txtEndTime.setValue(null);
        btnAddOrUpdate.setText("Add");
        btnAddOrUpdate.setStyle("-fx-background-color: #17a2b8");
        btnDelete.setVisible(false);
        btnDelete.setDisable(true);
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        Alert alert = null;
        if (tblLectureSchedule.getSelectionModel().getSelectedItem() != null) {
            boolean isDeleted = LectureScheduleService.deleteSchedule(tblLectureSchedule.getSelectionModel().getSelectedItem().getId());
            if (isDeleted) {
                tblLectureSchedule.getItems().clear();
                tblLectureSchedule.getItems().addAll(LectureScheduleService.getAllSchedules());
                btnCancel.fire();
                alert = new Alert(Alert.AlertType.INFORMATION, "Lecture Schedule Deleted Successfully!");
                alert.showAndWait();
            }
        }
    }
}
