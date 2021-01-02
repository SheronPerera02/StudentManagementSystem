package lk.ijse.studentManagement.service;

import lk.ijse.studentManagement.db.DBConnection;
import lk.ijse.studentManagement.dto.PaymentDTO;
import lk.ijse.studentManagement.dto.RegistrationDTO;
import lk.ijse.studentManagement.dto.StudentDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StudentRegistrationService {
    public static String getNextRegisterId() {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().
                            prepareStatement("SELECT RID FROM Registration ORDER BY RID DESC LIMIT 1");
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                String[] split = resultSet.getString(1).split("R");
                int val = Integer.parseInt(split[1]) + 1;
                if (val < 10) {
                    return "R0000" + val;
                } else if (val < 100) {
                    return "R000" + val;
                } else if (val < 1000) {
                    return "R00" + val;
                } else if (val < 10000) {
                    return "R0" + val;
                } else {
                    return "R" + val;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getNextStudentId() {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().
                            prepareStatement("SELECT SID FROM Student ORDER BY SID DESC LIMIT 1");
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                String[] split = resultSet.getString(1).split("S");
                int val = Integer.parseInt(split[1]) + 1;
                if (val < 10) {
                    return "S0000" + val;
                } else if (val < 100) {
                    return "S000" + val;
                } else if (val < 1000) {
                    return "S00" + val;
                } else if (val < 10000) {
                    return "S0" + val;
                } else {
                    return "S" + val;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getNextPaymentId() {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().
                            prepareStatement("SELECT PID FROM Payment ORDER BY PID DESC LIMIT 1");
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                String[] split = resultSet.getString(1).split("P");
                int val = Integer.parseInt(split[1]) + 1;
                if (val < 10) {
                    return "P0000" + val;
                } else if (val < 100) {
                    return "P000" + val;
                } else if (val < 1000) {
                    return "P00" + val;
                } else if (val < 10000) {
                    return "P0" + val;
                } else {
                    return "P" + val;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addStudent(StudentDTO dto) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("INSERT INTO Student VALUES(?,?,?,?,?,?,?,?)");
            stm.setObject(1, dto.getId());
            stm.setObject(2, dto.getName());
            stm.setObject(3, dto.getEmail());
            stm.setObject(4, dto.getAddress());
            stm.setObject(5, dto.getContact());
            stm.setObject(6, dto.getDateOfBirth());
            stm.setObject(7, dto.getParentName());
            stm.setObject(8, dto.getParentContact());
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    public static boolean addPayment(PaymentDTO dto) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("INSERT INTO Payment VALUES(?,?,?,?)");
            stm.setObject(1, dto.getId());
            stm.setObject(2, dto.getRegistrationId());
            stm.setObject(3, dto.getDate());
            stm.setObject(4, dto.getAmount());
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean registerStudent(RegistrationDTO dto) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("INSERT INTO Registration VALUES(?,?,?,?,?)");
            stm.setObject(1, dto.getId());
            stm.setObject(2, dto.getStudentId());
            stm.setObject(3, dto.getBatchId());
            stm.setObject(4, dto.getRegisterDate());
            stm.setObject(5, dto.getFee());
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean studentAlreadyExists(String uniqueContact) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT Contact FROM Student");
            ResultSet resultSet = stm.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString(1).contentEquals(uniqueContact)) return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getStudentId(String uniqueContact) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT SID,Contact FROM Student");
            ResultSet resultSet = stm.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString(2).contentEquals(uniqueContact)) return resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<String> getAllBatches(String courseName) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().
                            prepareStatement("SELECT BatchID FROM Batch,Course WHERE" +
                                    " Course.CID=Batch.CID AND CourseName=?");
            stm.setObject(1, courseName);
            ResultSet resultSet = stm.executeQuery();
            ArrayList<String> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(resultSet.getString(1));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getOccupantNumber(String courseName, String batchNo) {
        try {
            PreparedStatement stm1 = DBConnection.getInstance().getConnection().prepareStatement("SELECT BID FROM" +
                    " Batch,Course WHERE Batch.CID=Course.CID AND CourseName=? AND BatchID=?");
            stm1.setObject(1, courseName);
            stm1.setObject(2, batchNo);
            ResultSet resultSet1 = stm1.executeQuery();
            String bid = null;
            if (resultSet1.next()) {
                bid = resultSet1.getString(1);
            }
            PreparedStatement stm2 = DBConnection.getInstance().getConnection().prepareStatement("SELECT COUNT(BID) FROM Registration WHERE BID=?");

            stm2.setObject(1, bid);
            ResultSet resultSet2 = stm2.executeQuery();

            if (resultSet2.next()) {
                return Integer.parseInt(resultSet2.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static boolean getRegistrationVerification(String contact, String courseName) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().getConnection().prepareStatement("SELECT RID FROM Registration,Student,Batch,Course " +
                            "WHERE Student.SID=Registration.SID AND " +
                            "Registration.BID=Batch.BID AND Batch.CID=Course.CID AND CourseName=? AND Contact=?");
            stm.setObject(1, courseName);
            stm.setObject(2, contact);
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                String rid = resultSet.getString(1);
                if (rid != null) return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static double getCourseFee(String courseName, String batch) {
        try {
            PreparedStatement stm = DBConnection.getInstance().
                    getConnection().prepareStatement("SELECT Batch.CourseFee FROM Batch,Course " +
                    "WHERE Course.CID=Batch.CID AND CourseName=? AND BatchID=?");
            stm.setObject(1, courseName);
            stm.setObject(2, batch);
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getBatchStartDate(String courseName, String batch) {
        try {
            PreparedStatement stm = DBConnection.getInstance().
                    getConnection().prepareStatement("SELECT StartDate FROM Batch,Course WHERE " +
                    "Course.CID=Batch.CID AND CourseName=? AND BatchID=?");
            stm.setObject(1, courseName);
            stm.setObject(2, batch);
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static double getRegistrationFee(String courseName, String batch) {
        try {
            PreparedStatement stm = DBConnection.getInstance().
                    getConnection().prepareStatement("SELECT RegFee FROM Batch,Course WHERE " +
                    "Course.CID=Batch.CID AND CourseName=? AND BatchID=?");
            stm.setObject(1, courseName);
            stm.setObject(2, batch);
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
