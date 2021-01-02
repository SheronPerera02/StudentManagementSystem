package lk.ijse.studentManagement.service;

import javafx.scene.control.Alert;
import lk.ijse.studentManagement.db.DBConnection;
import lk.ijse.studentManagement.dto.RegistrationDTO;
import lk.ijse.studentManagement.dto.StudentDTO;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StudentUpdateService {

    public static boolean updateStudent(StudentDTO dto) {
        Alert alert;
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().getConnection().prepareStatement("UPDATE Student SET " +
                    "Name=?,Email=?,Address=?,Contact=?,DateOfBirth=?,ParentName=?,ParentContact=? WHERE SID=?");
            stm.setObject(1,dto.getName());
            stm.setObject(2,dto.getEmail());
            stm.setObject(3,dto.getAddress());
            stm.setObject(4,dto.getContact());
            stm.setObject(5,dto.getDateOfBirth());
            stm.setObject(6,dto.getParentName());
            stm.setObject(7,dto.getParentContact());
            stm.setObject(8,dto.getId());
            return stm.executeUpdate()>0;
        } catch (SQLException e) {
            e.printStackTrace();
            alert = new Alert(Alert.AlertType.INFORMATION,"Duplicate Entry\nFor Contact Number!");
            alert.showAndWait();
        }
        return false;
    }
    public static boolean updateRegistration(RegistrationDTO dto){
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().getConnection().prepareStatement("UPDATE Registration SET " +
                            "SID=?,BID=?,RegDate=?,Fee=? WHERE RID=?");
            stm.setObject(1,dto.getStudentId());
            stm.setObject(2,dto.getBatchId());
            stm.setObject(3,dto.getRegisterDate());
            stm.setObject(4,dto.getFee());
            stm.setObject(5,dto.getId());
            return stm.executeUpdate()>0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
