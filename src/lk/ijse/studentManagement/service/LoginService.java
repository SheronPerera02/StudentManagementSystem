package lk.ijse.studentManagement.service;

import lk.ijse.studentManagement.db.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginService {
    public static boolean userNameDoesNotExist(String name){
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().
                            prepareStatement("SELECT UserName FROM Login");
            ResultSet resultSet = stm.executeQuery();
            while(resultSet.next()){
                if(resultSet.getString(1).contentEquals(name)) return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
    public static boolean accessGranted(String userName, String password){
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().
                            prepareStatement("SELECT PassWord FROM Login WHERE UserName=?");
            stm.setObject(1,userName);
            ResultSet resultSet = stm.executeQuery();
            if(resultSet.next()){
                return resultSet.getString(1).contentEquals(password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
