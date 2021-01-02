package lk.ijse.studentManagement.service;

import javafx.scene.control.Alert;
import lk.ijse.studentManagement.db.DBConnection;
import lk.ijse.studentManagement.dto.CourseSubjectDTO;
import lk.ijse.studentManagement.dto.SubjectDTO;
import lk.ijse.studentManagement.model.AddSubjectTM;
import lk.ijse.studentManagement.model.ViewSubjectTM;

import java.sql.*;
import java.util.ArrayList;

public class SubjectService {
    public static String getNextCSID() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement stm =
                    connection.prepareStatement("SELECT CSID FROM Course_Subject ORDER BY CSID DESC LIMIT 1");
            ResultSet resultSet = stm.executeQuery();
            String string = null;
            if (resultSet.next()) {
                string = resultSet.getString(1);
            }
            if (string != null) {
                String[] split = string.split("CS");
                int val = Integer.parseInt(split[1]) + 1;
                if (val < 10) {
                    return "CS000" + val;
                } else if (val < 100) {
                    return "CS00" + val;
                } else if (val < 1000) {
                    return "CS0" + val;
                } else {
                    return "CS" + val;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addCourseSubject(CourseSubjectDTO dto) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().
                            prepareStatement("INSERT INTO Course_Subject VALUES(?,?,?,?)");
            stm.setObject(1, dto.getId());
            stm.setObject(2, dto.getCourseId());
            stm.setObject(3, dto.getSubjectId());
            stm.setObject(4, dto.getSemester());

            return stm.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Duplicate Entry!");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    public static boolean addSubject(SubjectDTO dto) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().
                            prepareStatement("INSERT INTO Subject VALUES(?,?)");
            stm.setObject(1, dto.getId());
            stm.setObject(2, dto.getSubjectName());

            return stm.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Duplicate Entry!");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getNextSubID() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement stm =
                    connection.prepareStatement("SELECT SubID FROM Subject ORDER BY SubID DESC LIMIT 1");
            ResultSet resultSet = stm.executeQuery();
            String string = null;
            if (resultSet.next()) {
                string = resultSet.getString(1);
            }
            if (string != null) {
                String[] split = string.split("Sub");
                int val = Integer.parseInt(split[1]) + 1;
                if (val < 10) {
                    return "Sub00" + val;
                } else if (val < 100) {
                    return "Sub0" + val;
                } else {
                    return "Sub" + val;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<AddSubjectTM> getAllCourseSubjects() {
        try {
            PreparedStatement preparedStatement =
                    DBConnection.getInstance().
                            getConnection().
                            prepareStatement("SELECT CSID,CourseName,SubName,Semester FROM Course_Subject,Subject,Course WHERE" +
                                    " Subject.SubID=Course_Subject.SubID AND Course.CID=Course_Subject.CID" +
                                    " ORDER BY CourseName ASC, SubName ASC");
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<AddSubjectTM> list = new ArrayList<>();

            while (resultSet.next()) {
                list.add(new AddSubjectTM(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4)
                ));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean subjectAlreadyExists(String subject) {
        try {
            PreparedStatement stm = DBConnection.getInstance().
                    getConnection().
                    prepareStatement("SELECT SubName FROM Subject");
            ResultSet resultSet = stm.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString(1).contentEquals(subject)) return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getSubjectId(String subject) {
        try {
            PreparedStatement stm = DBConnection.getInstance().
                    getConnection().
                    prepareStatement("SELECT SubID FROM Subject WHERE SubName=?");
            stm.setObject(1, subject);
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<ViewSubjectTM> getAllSubjects() {
        try {
            PreparedStatement preparedStatement =
                    DBConnection.getInstance().
                            getConnection().
                            prepareStatement("SELECT * FROM Subject");
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<ViewSubjectTM> list = new ArrayList<>();

            while (resultSet.next()) {
                list.add(new ViewSubjectTM(
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

    public static boolean updateSubject(SubjectDTO dto) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance()
                            .getConnection().
                            prepareStatement("UPDATE Subject SET SubName=? WHERE SubID=?");
            stm.setObject(1, dto.getSubjectName());
            stm.setObject(2, dto.getId());
            return stm.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateCourseSubject(CourseSubjectDTO dto) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance()
                            .getConnection().
                            prepareStatement("UPDATE Course_Subject SET Semester=? WHERE CSID=?");
            stm.setObject(1, dto.getSemester());
            stm.setObject(2, dto.getId());
            return stm.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<String> getAllSubjectName() {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().
                            prepareStatement("SELECT SubName FROM Subject");
            ArrayList<String> list = new ArrayList<>();
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
}
