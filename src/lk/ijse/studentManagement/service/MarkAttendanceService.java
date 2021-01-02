package lk.ijse.studentManagement.service;

import com.jfoenix.controls.JFXCheckBox;
import lk.ijse.studentManagement.db.DBConnection;
import lk.ijse.studentManagement.dto.AttendanceDTO;
import lk.ijse.studentManagement.model.MarkAttendanceTM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MarkAttendanceService {

    public static ArrayList<MarkAttendanceTM> getAllStudents(String course, String subject, String batch, String event) {
        try {
            PreparedStatement stm = DBConnection.getInstance().
                    getConnection().
                    prepareStatement("SELECT AID,Attendance.RID,Name,Attendance,Arrival,Departure FROM " +
                            "Student,Registration,Attendance WHERE Student.SID=Registration.SID " +
                            "AND Registration.RID=Attendance.RID AND CSID=? AND Batch=? AND EventName=?");

            stm.setObject(1, AddExamService.getCourseSubjectId(course, subject));
            stm.setObject(2, batch);
            stm.setObject(3, event);

            ArrayList<MarkAttendanceTM> list = new ArrayList<>();

            ResultSet students = stm.executeQuery();
            JFXCheckBox box1;
            JFXCheckBox box2;
            while (students.next()) {
                box1 = new JFXCheckBox();
                box2 = new JFXCheckBox();
                if (students.getString(4).contentEquals("Present")) {
                    box1.setSelected(true);
                } else if (students.getString(4).contentEquals("Absent")) {
                    box2.setSelected(true);
                }
                list.add(new MarkAttendanceTM(
                        students.getString(1),
                        students.getString(2),
                        students.getString(3),
                        box1,
                        box2,
                        students.getString(5).contentEquals("") ? null : students.getString(5),
                        students.getString(6).contentEquals("") ? null : students.getString(6)
                ));
            }

            return list;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean setAttendance(ArrayList<AttendanceDTO> list) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("INSERT INTO Attendance VALUES(?,?,?,?,?,?,?,?,?)");
            boolean isSet = false;
            for (AttendanceDTO dto : list) {
                stm.setObject(1, dto.getId());
                stm.setObject(2, dto.getRegId());
                stm.setObject(3, dto.getDate());
                stm.setObject(4, dto.getCourseSubjectId());
                stm.setObject(5, dto.getBatch());
                stm.setObject(6, dto.getEventName());
                stm.setObject(7, dto.getAttendance());
                stm.setObject(8, dto.getArrival());
                stm.setObject(9, dto.getDeparture());
                isSet = stm.executeUpdate() > 0;
            }
            return isSet;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<MarkAttendanceTM> getAllStudents(String course, String subject, String batch) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().
                            prepareStatement("SELECT RID,Name FROM Student,Registration,Course,Batch WHERE " +
                                    "Student.SID=Registration.SID AND Registration.BID=Batch.BID AND " +
                                    "Course.CID=Batch.CID AND CourseName=? AND BatchID=? ORDER BY RID ASC");
            stm.setObject(1, course);
            stm.setObject(2, batch);

            ArrayList<MarkAttendanceTM> list = new ArrayList<>();

            String id = getNextIdFromDatabase();

            ResultSet resultSet = stm.executeQuery();

            while (resultSet.next()) {
                id = getNextId(id);
                list.add(new MarkAttendanceTM(
                        id,
                        resultSet.getString(1),
                        resultSet.getString(2),
                        new JFXCheckBox(),
                        new JFXCheckBox(),
                        null,
                        null
                ));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getNextIdFromDatabase() {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT AID FROM Attendance ORDER BY AID DESC LIMIT 1");
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                String[] split = resultSet.getString(1).split("A");
                int val = Integer.parseInt(split[1]) + 1;
                if (val < 10) {
                    return "A000000" + val;
                } else if (val < 100) {
                    return "A00000" + val;
                } else if (val < 1000) {
                    return "A0000" + val;
                } else if (val < 10000) {
                    return "A000" + val;
                } else if (val < 100000) {
                    return "A00" + val;
                } else if (val < 1000000) {
                    return "A0" + val;
                } else {
                    return "A" + val;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getNextId(String id) {
        if (id == null) {
            return "A0000001";
        } else {
            String[] split = id.split("A");
            int val = Integer.parseInt(split[1]) + 1;
            if (val < 10) {
                return "A000000" + val;
            } else if (val < 100) {
                return "A00000" + val;
            } else if (val < 1000) {
                return "A0000" + val;
            } else if (val < 10000) {
                return "A000" + val;
            } else if (val < 100000) {
                return "A00" + val;
            } else if (val < 1000000) {
                return "A0" + val;
            } else {
                return "A" + val;
            }
        }
    }

    public static ArrayList<String> getAllEventNames(String courseSubjectId, String batch) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT DISTINCT EventName FROM Attendance WHERE " +
                            "CSID=? AND Batch=?");
            stm.setObject(1, courseSubjectId);
            stm.setObject(2, batch);
            ResultSet resultSet = stm.executeQuery();
            ArrayList<String> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(resultSet.getString(1));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean attendanceAlreadySet(String courseSubjectId, String batch, String event) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT AID FROM Attendance WHERE " +
                            "CSID=? AND Batch=? AND EventName=?");
            stm.setObject(1, courseSubjectId);
            stm.setObject(2, batch);
            stm.setObject(3, event);
            ResultSet resultSet = stm.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateAttendance(ArrayList<AttendanceDTO> list) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("UPDATE Attendance SET Attendance=?,Arrival=?,Departure=? " +
                            "WHERE AID=?");
            boolean isUpdated = false;
            for (AttendanceDTO dto : list) {
                stm.setObject(1, dto.getAttendance());
                stm.setObject(2, dto.getArrival());
                stm.setObject(3, dto.getDeparture());
                stm.setObject(4, dto.getId());
                isUpdated = stm.executeUpdate() > 0;
            }
            return isUpdated;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<String> getAllEventNamesForTheBatch(String course, String batch) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT DISTINCT EventName FROM Course,Batch,Registration,Attendance " +
                            "WHERE Course.CID=Batch.CID AND Batch.BID=Registration.BID AND Registration.RID=Attendance.RID AND " +
                            "CourseName=? AND Batch=?");
            stm.setObject(1, course);
            stm.setObject(2, batch);
            ResultSet resultSet = stm.executeQuery();
            ArrayList<String> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(resultSet.getString(1));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
