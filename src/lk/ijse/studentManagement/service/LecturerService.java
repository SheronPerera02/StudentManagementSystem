package lk.ijse.studentManagement.service;

import javafx.scene.control.Alert;
import lk.ijse.studentManagement.db.DBConnection;
import lk.ijse.studentManagement.dto.LecturerDTO;
import lk.ijse.studentManagement.model.LecturerTM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class
LecturerService {
    public static boolean addLecturer(LecturerDTO lecturer) {
        Alert alert;
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().
                            prepareStatement("INSERT INTO Lecturer VALUES(?,?,?,?,?,?)");
            stm.setObject(1, lecturer.getId());
            stm.setObject(2, lecturer.getName());
            stm.setObject(3, lecturer.getEmail());
            stm.setObject(4, lecturer.getAddress());
            stm.setObject(5, lecturer.getDob());
            stm.setObject(6, lecturer.getContact());
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            alert = new Alert(Alert.AlertType.INFORMATION, "Duplicate Entry\nFor Contact Number!");
            alert.showAndWait();
        }
        return false;
    }

    public static String getNextId() {

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement stm =
                    connection.prepareStatement("SELECT LecID FROM Lecturer ORDER BY LecID DESC LIMIT 1");
            ResultSet resultSet = stm.executeQuery();
            String string = null;
            if (resultSet.next()) {
                string = resultSet.getString(1);
            }
            if (string != null) {
                String[] split = string.split("Lec");
                int val = Integer.parseInt(split[1]) + 1;
                if (val < 10) {
                    return "Lec00" + val;
                } else if (val < 100) {
                    return "Lec0" + val;
                } else {
                    return "Lec" + val;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<LecturerTM> getAllLecturers() {
        try {
            PreparedStatement preparedStatement =
                    DBConnection.getInstance().
                            getConnection().
                            prepareStatement("SELECT * FROM Lecturer");
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<LecturerTM> list = new ArrayList<>();

            while (resultSet.next()) {
                list.add(new LecturerTM(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(6),
                        resultSet.getString(5)
                ));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean updateLecturer(LecturerDTO dto) {
        Alert alert;
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().
                            prepareStatement("UPDATE Lecturer SET Name=?,Email=?,Address=?,DateOfBirth=?,Contact=? WHERE LecID=?");
            stm.setObject(1, dto.getName());
            stm.setObject(2, dto.getEmail());
            stm.setObject(3, dto.getAddress());
            stm.setObject(4, dto.getDob());
            stm.setObject(5, dto.getContact());
            stm.setObject(6, dto.getId());
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            alert = new Alert(Alert.AlertType.INFORMATION, "Duplicate Entry\nFor Contact Number!");
            alert.showAndWait();
        }
        return false;
    }
}
