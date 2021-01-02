package lk.ijse.studentManagement.service;

import lk.ijse.studentManagement.db.DBConnection;
import lk.ijse.studentManagement.model.ViewAttendanceEventTM;
import lk.ijse.studentManagement.model.ViewAttendanceTM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ViewAttendanceService {
    public static ArrayList<ViewAttendanceEventTM> getAllEvents(String courseSubjectId, String batch) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT DISTINCT EventName,Attend_Date FROM Attendance " +
                            "WHERE CSID=? AND Batch=?");
            stm.setObject(1, courseSubjectId);
            stm.setObject(2, batch);
            ResultSet resultSet = stm.executeQuery();
            ArrayList<ViewAttendanceEventTM> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new ViewAttendanceEventTM(
                        resultSet.getString(1),
                        resultSet.getString(2)
                ));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<ViewAttendanceTM> getAttendance(String eventName, String courseSubjectId, String batch) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT Attendance.RID,Name,Attendance,Arrival,Departure " +
                            "FROM Student,Registration,Attendance WHERE Student.SID=Registration.SID AND Registration.RID=" +
                            "Attendance.RID AND CSID=? AND Batch=? AND EventName=?");
            stm.setObject(1, courseSubjectId);
            stm.setObject(2, batch);
            stm.setObject(3, eventName);
            ResultSet resultSet = stm.executeQuery();
            ArrayList<ViewAttendanceTM> list = new ArrayList<>();
            boolean isAbsent = false;
            while (resultSet.next()) {
                isAbsent = resultSet.getString(3).contentEquals("Absent");
                list.add(new ViewAttendanceTM(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        isAbsent ? "-" : resultSet.getString(4),
                        isAbsent ? "-" : resultSet.getString(5)
                ));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
