package lk.ijse.studentManagement.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import lk.ijse.studentManagement.service.AdminDashboardService;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {

    public Label lblStudentRegistration;
    public Label lblTotalBatch;
    public Label lblTotalLecturer;
    public Label lblUserRegistration;
    public LineChart<String, Number> lineChart;
    public PieChart pieChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadAllLabels();
        loadLineChart();
        loadPieChart();
    }

    private void loadPieChart() {
        ArrayList<PieChart.Data> pieChartData = AdminDashboardService.getPieChartData();
        ObservableList<PieChart.Data> list = FXCollections.observableArrayList();
        list.addAll(pieChartData);
        pieChart.setData(list);
    }

    private void loadAllLabels() {
        loadStudentRegistrationCount();
        loadTotalBatchCount();
        loadTotalLecturerCount();
        loadUserRegistrationCount();
        loadLineChart();
    }

    private void loadLineChart() {
        lineChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
        series.getData().addAll(AdminDashboardService.getLineChartData());
        series.setName("Average Exam-Marks");
        lineChart.getData().add(series);

    }

    private void loadUserRegistrationCount() {
        lblUserRegistration.setText(String.valueOf(AdminDashboardService.getUserRegistrationCount()));
    }

    private void loadTotalLecturerCount() {
        lblTotalLecturer.setText(String.valueOf(AdminDashboardService.getTotalLecturerCount()));
    }

    private void loadTotalBatchCount() {
        lblTotalBatch.setText(String.valueOf(AdminDashboardService.getTotalBatchCount()));
    }

    private void loadStudentRegistrationCount() {
        lblStudentRegistration.setText(String.valueOf(AdminDashboardService.getStudentRegistrationCount()));
    }
}
