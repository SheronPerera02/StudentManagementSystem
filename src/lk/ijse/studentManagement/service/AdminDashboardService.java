package lk.ijse.studentManagement.service;

import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import lk.ijse.studentManagement.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminDashboardService {
    public static int getStudentRegistrationCount() {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT COUNT(RID) FROM Registration");
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getUserRegistrationCount() {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT COUNT(LogID) FROM Login");
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getTotalBatchCount() {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT COUNT(BID) FROM Batch");
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getTotalLecturerCount() {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT COUNT(LecID) FROM Lecturer");
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static ArrayList<XYChart.Data<String, Number>> getLineChartData() {
        Connection connection = null;
        ArrayList<XYChart.Data<String, Number>> list = new ArrayList<>();
        try {
            connection = DBConnection.getInstance().getConnection();
            PreparedStatement getTotalMarksForMonth = connection.
                    prepareStatement("SELECT SUM(Marks) FROM Exam_Mark,Exam WHERE " +
                            "Exam_Mark.ExID=Exam.ExID AND YEAR(Exam_Date)=YEAR(NOW()) AND MONTH(Exam_Date)=?");

            PreparedStatement totalParticipantsForMonth = connection.
                    prepareStatement("SELECT COUNT(EMID) FROM Exam_Mark,Exam WHERE " +
                            "Exam_Mark.ExID=Exam.ExID AND YEAR(Exam_Date)=YEAR(NOW()) AND MONTH(Exam_Date)=? AND ExamState NOT IN('abs')");

            PreparedStatement getMonthName = connection.prepareStatement("SELECT MONTHNAME(?)");

            PreparedStatement getRecordCountForMonth = connection.
                    prepareStatement("SELECT COUNT(EMID) FROM Exam_Mark,Exam WHERE " +
                            "Exam_Mark.ExID=Exam.ExID AND YEAR(Exam_Date)=YEAR(NOW()) AND MONTH(Exam_Date)=?");
            ResultSet getRecordCount = null;
            ResultSet getTotalMarks = null;
            ResultSet getTotalParticipants = null;
            ResultSet getMonth = null;

            int recordCount;
            double marks;
            double participants;
            double average;
            String monthName;

            for (int i = 1; i <= 12; i++) {
                recordCount = 0;
                marks = 0;
                participants = 0;
                monthName = null;
                getRecordCountForMonth.setObject(1, i);
                getRecordCount = getRecordCountForMonth.executeQuery();
                if (getRecordCount.next()) recordCount = getRecordCount.getInt(1);
                if (recordCount > 0) {
                    getTotalMarksForMonth.setObject(1, i);
                    getTotalMarks = getTotalMarksForMonth.executeQuery();
                    if (getTotalMarks.next()) marks = getTotalMarks.getDouble(1);
                    totalParticipantsForMonth.setObject(1, i);
                    getTotalParticipants = totalParticipantsForMonth.executeQuery();
                    if (getTotalParticipants.next()) participants = getTotalParticipants.getDouble(1);
                    getMonthName.setObject(1, "2000-" + i + "-01");
                    getMonth = getMonthName.executeQuery();
                    if (getMonth.next()) monthName = getMonth.getString(1);
                    average = marks / participants;
                    list.add(new XYChart.Data<>(monthName, average));
                }
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<PieChart.Data> getPieChartData() {
        ArrayList<PieChart.Data> list = new ArrayList<>();
        try {
            PreparedStatement getRegistrationCount =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT COUNT(RID) FROM Registration,Batch,Course WHERE " +
                            "Registration.BID=Batch.BID AND Batch.CID=Course.CID AND CourseName=?");
            PreparedStatement getCourses =
                    DBConnection.getInstance().getConnection().prepareStatement("SELECT CourseName FROM Course");
            ResultSet courses = getCourses.executeQuery();
            while(courses.next()){
                getRegistrationCount.setObject(1,courses.getString(1));
                ResultSet regCount = getRegistrationCount.executeQuery();
                if(regCount.next()){
                    list.add(new PieChart.Data(courses.getString(1),regCount.getInt(1)));
                }
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
