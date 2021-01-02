package lk.ijse.studentManagement.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection dbConnection;
    private Connection connection;

    private DBConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/StudentManagement",
                "root","mysql");
    }
    public static DBConnection getInstance() throws SQLException {
        return dbConnection==null?(dbConnection=new DBConnection()) : dbConnection;
    }
    public Connection getConnection(){
        return connection;
    }
}
