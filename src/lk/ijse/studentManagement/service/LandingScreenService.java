package lk.ijse.studentManagement.service;

import lk.ijse.studentManagement.db.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LandingScreenService {
    public static String getVerification() throws SQLException {

        PreparedStatement preparedStatement = DBConnection.getInstance().
                getConnection().
                prepareStatement("SELECT PassWord FROM Login WHERE LogID='Log1'");

        ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()){
            return resultSet.getString(1);
        }
        return null;
    }
}
