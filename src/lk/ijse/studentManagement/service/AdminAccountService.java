package lk.ijse.studentManagement.service;

import lk.ijse.studentManagement.db.DBConnection;
import lk.ijse.studentManagement.dto.AdminAccountDTO;
import lk.ijse.studentManagement.model.AdminAccountTM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class AdminAccountService {
    public static boolean userNameExists(String userName) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().
                            prepareStatement("SELECT UserName FROM Login");
            ResultSet resultSet = stm.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString(1).contentEquals(userName)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean passwordsDoesNotMatch(String passWord, String repeatPassWord) {
        return !passWord.contentEquals(repeatPassWord);
    }

    public static boolean addAccount(AdminAccountDTO dto) {
        try {
            PreparedStatement preparedStatement =
                    DBConnection.getInstance().
                            getConnection().
                            prepareStatement("INSERT INTO Login VALUES(?,?,?)");
            preparedStatement.setObject(1, dto.getId());
            preparedStatement.setObject(2, dto.getUsername());
            preparedStatement.setObject(3, dto.getPassword());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getNextID() {
        String id = null;
        try {
            PreparedStatement preparedStatement =
                    DBConnection.getInstance().
                            getConnection().
                            prepareStatement("SELECT LogID FROM Login ORDER BY LogID DESC LIMIT 1");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getString(1);
            }
            if (id != null) {
                String[] split = id.split("Log");
                int val = Integer.parseInt(split[1]) + 1;
                return "Log" + val;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean userNameNotEligible(String username) {
        return !Pattern.matches("[a-zA-Z0-9]{6,25}", username.replaceAll("\\s+", ""));
    }

    public static ArrayList<AdminAccountTM> getAllAdminAccounts() {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT * FROM Login");
            ResultSet resultSet = stm.executeQuery();
            ArrayList<AdminAccountTM> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new AdminAccountTM(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3)
                ));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean updateAccount(AdminAccountDTO dto) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().
                            prepareStatement("UPDATE Login SET UserName=?,PassWord=? WHERE LogID=?");
            stm.setObject(1, dto.getUsername());
            stm.setObject(2, dto.getPassword());
            stm.setObject(3, dto.getId());
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteAccount(String id) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("DELETE FROM Login WHERE LogID=?");
            stm.setObject(1,id);
            return stm.executeUpdate()>0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
