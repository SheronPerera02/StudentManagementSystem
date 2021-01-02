package lk.ijse.studentManagement.service;

import lk.ijse.studentManagement.db.DBConnection;
import lk.ijse.studentManagement.dto.LectureScheduleDTO;
import lk.ijse.studentManagement.model.LectureScheduleTM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LectureScheduleService {
    public static ArrayList<String> getAllLecturers() {
        ArrayList<String> list = new ArrayList<>();
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT Name FROM Lecturer");
            ResultSet resultSet = stm.executeQuery();
            while (resultSet.next()) {
                list.add(resultSet.getString(1));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getNextId() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement stm =
                    connection.prepareStatement("SELECT LID FROM Lecture_Schedule ORDER BY LID DESC LIMIT 1");
            ResultSet resultSet = stm.executeQuery();
            String string = null;
            if (resultSet.next()) {
                string = resultSet.getString(1);
            }
            if (string != null) {
                String[] split = string.split("L");
                int val = Integer.parseInt(split[1]) + 1;
                if (val < 10) {
                    return "L00" + val;
                } else if (val < 100) {
                    return "L0" + val;
                } else {
                    return "L" + val;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getLecturerId(String name) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT LecID FROM Lecturer WHERE Name=?");
            stm.setObject(1, name);
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addSchedule(LectureScheduleDTO dto) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("INSERT INTO Lecture_Schedule VALUES(?,?,?,?,?,?,?,?)");
            stm.setObject(1, dto.getId());
            stm.setObject(2, dto.getDayName());
            stm.setObject(3, dto.getLecturerId());
            stm.setObject(4, dto.getCourseSubjectId());
            stm.setObject(5, dto.getBatch());
            stm.setObject(6, dto.getDescription());
            stm.setObject(7, dto.getStart());
            stm.setObject(8, dto.getEnd());
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<LectureScheduleTM> getAllSchedules() {
        ArrayList<LectureScheduleTM> list = new ArrayList<>();
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT LID,Day_Name,Name,CourseName,Batch,SubName,Description,Start,End " +
                            "FROM Lecturer,Lecture_Schedule,Course_Subject,Course,Subject WHERE " +
                            "Lecturer.LecID=Lecture_Schedule.LecID AND Course_Subject.CSID=Lecture_Schedule.CSID AND " +
                            "Subject.SubID=Course_Subject.SubID AND Course.CID=Course_Subject.CID");
            ResultSet resultSet = stm.executeQuery();

            while (resultSet.next()) {
                list.add(new LectureScheduleTM(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getInt(5),
                        resultSet.getString(6),
                        resultSet.getString(7),
                        resultSet.getString(8),
                        resultSet.getString(9)
                ));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean updateSchedule(LectureScheduleDTO dto) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("UPDATE Lecture_Schedule SET Day_Name=?,LecID=?,CSID=?,Batch=?," +
                            "Description=?,Start=?,End=? WHERE LID=?");
            stm.setObject(1,dto.getDayName());
            stm.setObject(2,dto.getLecturerId());
            stm.setObject(3,dto.getCourseSubjectId());
            stm.setObject(4, dto.getBatch());
            stm.setObject(5,dto.getDescription());
            stm.setObject(6,dto.getStart());
            stm.setObject(7,dto.getEnd());
            stm.setObject(8,dto.getId());
            return stm.executeUpdate()>0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteSchedule(String id) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("DELETE FROM Lecture_Schedule WHERE LID=?");
            stm.setObject(1,id);
            return stm.executeUpdate()>0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
