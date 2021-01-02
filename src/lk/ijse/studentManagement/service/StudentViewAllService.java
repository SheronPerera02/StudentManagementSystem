package lk.ijse.studentManagement.service;

import lk.ijse.studentManagement.db.DBConnection;
import lk.ijse.studentManagement.model.ViewAllPaymentTM;
import lk.ijse.studentManagement.model.ViewAllRegistrationTM;
import lk.ijse.studentManagement.model.ViewAllStudentTM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StudentViewAllService {
    public static ArrayList<ViewAllRegistrationTM> getAllRegistration() {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().
                            prepareStatement("SELECT RID,Registration.SID,Name,CourseName,BatchID,RegDate,Fee FROM " +
                                    "Student,Registration,Batch,Course WHERE " +
                                    "Student.SID=Registration.SID AND Registration.BID=Batch.BID AND " +
                                    "Batch.CID=Course.CID ORDER BY RID DESC");
            ResultSet resultSet = stm.executeQuery();
            ArrayList<ViewAllRegistrationTM> allRegistrations = new ArrayList<>();
            while (resultSet.next()) {
                allRegistrations.add(new ViewAllRegistrationTM(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getDouble(7),
                        getDueFee(resultSet.getString(1), resultSet.getDouble(7))
                ));
            }
            return allRegistrations;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static double getDueFee(String regId, double fee) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT Amount FROM Payment WHERE RID=?");
            stm.setObject(1, regId);
            ResultSet resultSet = stm.executeQuery();
            double totalPayed = 0;
            while (resultSet.next()) {
                totalPayed += resultSet.getDouble(1);
            }
            return fee - totalPayed;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getStudentName(String rid) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().getConnection().
                            prepareStatement("SELECT Name FROM Student,Registration WHERE Student.SID=" +
                                    "Registration.SID AND RID=?");
            stm.setObject(1, rid);
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<ViewAllStudentTM> getAllStudent() {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT * FROM Student ORDER BY SID DESC");
            ResultSet resultSet = stm.executeQuery();
            ArrayList<ViewAllStudentTM> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new ViewAllStudentTM(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7),
                        resultSet.getString(8)
                ));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<ViewAllPaymentTM> getAllPayment() {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT PID,Payment.RID,Name,Date,Amount " +
                            "FROM Payment,Student,Registration WHERE " +
                            "Student.SID=Registration.SID AND Registration.RID=Payment.RID ORDER BY PID DESC");
            ResultSet resultSet = stm.executeQuery();
            ArrayList<ViewAllPaymentTM> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new ViewAllPaymentTM(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getDouble(5)
                ));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }
}
