package lk.ijse.studentManagement.service;

import javafx.scene.control.Alert;
import lk.ijse.studentManagement.db.DBConnection;
import lk.ijse.studentManagement.dto.BatchDTO;
import lk.ijse.studentManagement.model.BatchTM;

import java.sql.*;
import java.util.ArrayList;

public class BatchService {

    public static ArrayList<String> getAllCourses(){
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                    getConnection().
                            prepareStatement("SELECT CourseName From Course");
            ResultSet resultSet = stm.executeQuery();
            ArrayList<String> list = new ArrayList<>();
            while(resultSet.next()){
                list.add(resultSet.getString(1));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }
    public static String getNextBatch(String courseName){
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                    getConnection().
                    prepareStatement("SELECT BatchID FROM Batch,Course WHERE Batch.CID=Course.CID AND" +
                            " CourseName=? ORDER BY BatchID DESC LIMIT 1");
            stm.setObject(1,courseName);
            ResultSet resultSet = stm.executeQuery();
            if(resultSet.next()){
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getNextId(){
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement stm =
                    connection.prepareStatement("SELECT BID FROM Batch ORDER BY BID DESC LIMIT 1");
            ResultSet resultSet = stm.executeQuery();
            String string = null;
            if(resultSet.next()){
                string = resultSet.getString(1);
            }
            if(string!=null){
                String[] split = string.split("B");
                int val = Integer.parseInt(split[1])+1;
                if(val<10){
                    return "B0000"+val;
                } else if(val<100){
                    return "B000"+val;
                } else if(val<1000){
                    return "B00"+val;
                } else if(val<10000){
                    return "B0"+val;
                } else{
                    return "B"+val;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Boolean addBatch(BatchDTO batchDTO) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().
                            prepareStatement("INSERT INTO Batch VALUES(?,?,?,?,?,?,?)");
            stm.setObject(1,batchDTO.getId());
            stm.setObject(2,batchDTO.getBatch());
            stm.setObject(3,batchDTO.getCourseId());
            stm.setObject(4,batchDTO.getStartDate());
            stm.setObject(5,batchDTO.getEndDate());
            stm.setObject(6,batchDTO.getRegFee());
            stm.setObject(7,batchDTO.getCourseFee());

            return stm.executeUpdate()>0;
        } catch(SQLIntegrityConstraintViolationException ex){
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Duplicate Entry!");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



    public static String getCourseId(String selectedItem) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                    getConnection().
                    prepareStatement("SELECT CID FROM Course WHERE CourseName=?");
            stm.setObject(1,selectedItem);
            ResultSet resultSet = stm.executeQuery();
            if(resultSet.next()){
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static ArrayList<BatchTM> getAllBatches(){
        try {
            PreparedStatement preparedStatement =
                    DBConnection.getInstance().
                            getConnection().
                            prepareStatement("SELECT * FROM Batch,Course WHERE Course.CID=Batch.CID ORDER BY BID DESC");
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<BatchTM> list = new ArrayList<>();

            while(resultSet.next()){
                list.add(new BatchTM(
                        resultSet.getString(1),
                        Integer.parseInt(resultSet.getString(2)),
                        getCourseName(resultSet.getString(3)),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getDouble(6)
                ));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCourseName(String id) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().
                            prepareStatement("SELECT CourseName FROM Course WHERE CID=?");
            stm.setObject(1,id);
            ResultSet resultSet = stm.executeQuery();
            if(resultSet.next()){
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean updateBatch(BatchDTO dto) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().
                            prepareStatement("UPDATE Batch SET StartDate=?,EndDate=? WHERE BID=?");
            stm.setObject(1,dto.getStartDate());
            stm.setObject(2,dto.getEndDate());
            stm.setObject(3,dto.getId());
            return stm.executeUpdate()>0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getBatchId(String courseName, String batchNo) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT BID FROM Batch,Course WHERE" +
                    " Course.CID=Batch.CID AND CourseName=? AND BatchID=?");
            stm.setObject(1,courseName);
            stm.setObject(2,batchNo);
            ResultSet resultSet = stm.executeQuery();
            if(resultSet.next()){
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static double getCourseFee(String courseName) {
        try {
            PreparedStatement stm = DBConnection.getInstance().
                    getConnection().prepareStatement("SELECT CourseFee FROM Course " +
                    "WHERE CourseName=?");
            stm.setObject(1,courseName);
            ResultSet resultSet = stm.executeQuery();
            if(resultSet.next()){
                return resultSet.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
