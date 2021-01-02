package lk.ijse.studentManagement.service;

import javafx.scene.control.Alert;
import lk.ijse.studentManagement.db.DBConnection;
import lk.ijse.studentManagement.dto.CourseDTO;
import lk.ijse.studentManagement.model.CourseTM;

import java.sql.*;
import java.util.ArrayList;

public class CourseService {
    public static String getNextId() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement stm =
                    connection.prepareStatement("SELECT CID FROM Course ORDER BY CID DESC LIMIT 1");
            ResultSet resultSet = stm.executeQuery();
            String string = null;
            if (resultSet.next()) {
                string = resultSet.getString(1);
            }
            if (string != null) {
                String[] split = string.split("C");
                int val = Integer.parseInt(split[1]) + 1;
                return val < 10 ? "C0" + val :
                        "C" + val;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addCourse(CourseDTO course) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().
                            prepareStatement("INSERT INTO Course VALUES(?,?,?,?)");
            stm.setObject(1, course.getId());
            stm.setObject(2, course.getCourseName());
            stm.setObject(3, course.getDuration());
            stm.setObject(4, course.getCourseFee());
            return stm.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException ex) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Duplicate Entry!");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateCourse(CourseDTO course) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().
                            prepareStatement("UPDATE Course SET CourseName=?,Duration=?,CourseFee=? WHERE CID=?");
            stm.setObject(1, course.getCourseName());
            stm.setObject(2, course.getDuration());
            stm.setObject(3, course.getCourseFee());
            stm.setObject(4, course.getId());

            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<CourseTM> getAllCourses() {
        try {
            PreparedStatement preparedStatement =
                    DBConnection.getInstance().
                            getConnection().
                            prepareStatement("SELECT * FROM Course");
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<CourseTM> list = new ArrayList<>();

            while (resultSet.next()) {
                list.add(new CourseTM(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDouble(4)
                ));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }
}
