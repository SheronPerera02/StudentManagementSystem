package lk.ijse.studentManagement.service;

import lk.ijse.studentManagement.db.DBConnection;
import lk.ijse.studentManagement.model.ViewResultTM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ViewResultService {
    public static ArrayList<ViewResultTM> getAllStudents(String examId) {
        try {
            PreparedStatement stm = DBConnection.
                    getInstance().
                    getConnection().prepareStatement("SELECT Exam_Mark.RID,Name,Marks,ExamState FROM Student,Registration,Exam_Mark " +
                    "WHERE Student.SID=Registration.SID AND Registration.RID=Exam_Mark.RID AND ExID=?");
            stm.setObject(1, examId);
            ResultSet resultSet = stm.executeQuery();
            ArrayList<ViewResultTM> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new ViewResultTM(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        Integer.parseInt(resultSet.getString(3)),
                        resultSet.getString(4)
                ));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
