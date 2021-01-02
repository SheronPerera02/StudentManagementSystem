package lk.ijse.studentManagement.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
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
import lk.ijse.studentManagement.dto.CourseDTO;
import lk.ijse.studentManagement.model.CourseTM;
import lk.ijse.studentManagement.service.CourseService;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class CourseController implements Initializable {
    public JFXTextField txtCourseName;
    public TextField txtYears;
    public TextField txtMonths;
    public TextField txtCourseFee;

    public JFXButton btnAddOrUpdate;
    public JFXButton btnCancel;

    public TableView<CourseTM> tblAddCourse;

    public TableColumn<CourseTM, String> colId;
    public TableColumn<CourseTM, String> colCourseName;
    public TableColumn<CourseTM, String> colDuration;
    public TableColumn<CourseTM, Double> colCourseFee;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colCourseFee.setCellValueFactory(new PropertyValueFactory<>("courseFee"));


        loadAllCourses();

        tblAddCourse.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CourseTM>() {
            @Override
            public void changed(ObservableValue<? extends CourseTM> observable, CourseTM oldValue, CourseTM newValue) {
                btnAddOrUpdate.setText("Update");
                CourseTM courseTM = tblAddCourse.getSelectionModel().getSelectedItem();
                if (courseTM != null) {
                    txtCourseName.setDisable(true);
                    txtCourseName.setText(courseTM.getCourseName());
                    int value = Integer.parseInt(courseTM.getDuration());
                    txtYears.setText(String.valueOf(value / 12));
                    txtMonths.setText(String.valueOf((value % 12)));
                    txtCourseFee.setText(String.valueOf((int) courseTM.getCourseFee()));
                    btnAddOrUpdate.setStyle("-fx-background-color: #28a745");
                }
            }
        });
    }

    private void loadAllCourses() {
        ArrayList<CourseTM> allCourses = CourseService.getAllCourses();
        ObservableList<CourseTM> items = tblAddCourse.getItems();

        if (allCourses != null) {
            for (CourseTM course : allCourses) {
                items.add(new CourseTM(
                        course.getId(),
                        course.getCourseName(),
                        course.getDuration(),
                        course.getCourseFee()
                ));
            }
        }
    }


    public void btnAddOrUpdateOnAction(ActionEvent actionEvent) {
        try {
            if (btnAddOrUpdate.getText().contentEquals("Add")) {
                addCourse();
            } else {
                updateCourse();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Try Again!");
            alert.showAndWait();
        }
    }

    private void updateCourse() {
        Alert alert;
        if (detailsMissing()) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Complete The Form!");
            alert.showAndWait();
        } else if (detailsNotEligible()) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Details Not Eligible!");
            alert.showAndWait();
        } else {
            alert = new Alert(Alert.AlertType.CONFIRMATION, "The Course Fee For The Upcoming Batches\n" +
                    "Will Be A Sum Of "+txtCourseFee.getText()+"/= Rupees");
            alert.showAndWait();
            if (alert.getResult().getText().contentEquals("OK")) {
                CourseDTO dto = new CourseDTO(
                        tblAddCourse.getSelectionModel().getSelectedItem().getId(),
                        txtCourseName.getText(),
                        getDuration(),
                        Double.parseDouble(txtCourseFee.getText())
                );
                boolean isUpdated = CourseService.updateCourse(dto);
                if (isUpdated) {

                    ObservableList<CourseTM> items = tblAddCourse.getItems();
                    items.set(tblAddCourse.getSelectionModel().getSelectedIndex(), new CourseTM(
                            dto.getId(),
                            dto.getCourseName(),
                            dto.getDuration(),
                            dto.getCourseFee()
                    ));
                    btnCancel.fire();
                    alert = new Alert(Alert.AlertType.INFORMATION, "Course Updated\nSuccessfully!");
                    alert.showAndWait();
                }
            }
        }
    }

    private void addCourse() {
        Alert alert;
        if (detailsMissing()) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Complete The Form!");
            alert.showAndWait();
        } else if (detailsNotEligible()) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Details Not Eligible!");
            alert.showAndWait();
        } else {

            boolean added;
            String nextId = CourseService.getNextId();
            if (nextId == null) {
                nextId = "C01";
            }
            added = CourseService.addCourse(new CourseDTO(
                    nextId,
                    txtCourseName.getText(),
                    getDuration(),
                    Double.parseDouble(txtCourseFee.getText())
            ));
            ObservableList<CourseTM> items = tblAddCourse.getItems();
            if (added) {

                items.add(new CourseTM(
                        nextId,
                        txtCourseName.getText(),
                        getDuration(),
                        Double.parseDouble(txtCourseFee.getText())
                ));
                btnCancel.fire();
                alert = new Alert(Alert.AlertType.INFORMATION, "Course Added\nSuccessfully!");
                alert.showAndWait();
            }
        }
    }

    private String getDuration() {
        int duration = (Integer.parseInt(txtYears.getText())) * 12;
        duration += Integer.parseInt(txtMonths.getText());
        return String.valueOf(duration);
    }


    public void btnCancelOnAction(ActionEvent actionEvent) {
        tblAddCourse.getSelectionModel().clearSelection();
        btnAddOrUpdate.setText("Add");
        txtCourseName.setDisable(false);
        txtCourseName.clear();
        txtYears.clear();
        txtMonths.clear();
        txtCourseFee.clear();
        txtCourseName.requestFocus();
        btnAddOrUpdate.setStyle("-fx-background-color:  #17a2b8");
    }

    public void txtCourseNameOnAction(ActionEvent actionEvent) {
        txtYears.requestFocus();
    }

    public void txtYearsOnAction(ActionEvent actionEvent) {
        txtMonths.requestFocus();
    }

    private boolean detailsNotEligible() {
        return !Pattern.matches("[a-zA-Z]{3,50}", txtCourseName.getText().replaceAll("\\s+", ""))
                ||
                !Pattern.matches("[0-9]{1,2}", txtYears.getText())
                ||
                !Pattern.matches("[0-9]{1,2}", txtMonths.getText())
                ||
                !Pattern.matches("[0-9]{1,10}", txtCourseFee.getText())
                ||
                txtCourseFee.getText().contentEquals("0")
                ||
                txtYears.getText().contentEquals("0")&&txtMonths.getText().contentEquals("0");
    }

    private boolean detailsMissing() {
        return txtCourseName.getText().contentEquals("")
                ||
                txtYears.getText().contentEquals("")
                ||
                txtMonths.getText().contentEquals("")
                ||
                txtCourseFee.getText().contentEquals("");
    }


    public void txtMonthsOnAction(ActionEvent actionEvent) {
        txtCourseFee.requestFocus();
    }
}
