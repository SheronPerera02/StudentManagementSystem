package lk.ijse.studentManagement.service;

import lk.ijse.studentManagement.db.DBConnection;
import lk.ijse.studentManagement.model.ExamDetailTM;
import lk.ijse.studentManagement.model.GuestMainFormTM;
import lk.ijse.studentManagement.model.PendingExamTM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GuestMainFormService {
    public static ArrayList<GuestMainFormTM> getAllStudents(String course, String batch) {
        ArrayList<GuestMainFormTM> list = new ArrayList<>();
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT RID,Name FROM Student,Registration,Batch,Course WHERE " +
                            "Student.SID=Registration.SID AND Registration.BID=Batch.BID AND Batch.CID=Course.CID AND " +
                            "CourseName=? AND BatchID=?");
            stm.setObject(1, course);
            stm.setObject(2, batch);
            ResultSet resultSet = stm.executeQuery();
            while (resultSet.next()) {
                list.add(new GuestMainFormTM(
                        resultSet.getString(1),
                        resultSet.getString(2)
                ));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<PendingExamTM> getAllPendingExams(String course, String batch) {
        ArrayList<PendingExamTM> list = new ArrayList<>();
        try {
            PreparedStatement pendingExams = DBConnection.
                    getInstance().
                    getConnection().prepareStatement("SELECT ExID FROM Exam,Course_Subject,Course WHERE " +
                    "Exam.CSID=Course_Subject.CSID AND Course_Subject.CID=Course.CID AND CourseName=? AND Batch=? AND ExID NOT IN" +
                    "(SELECT DISTINCT Exam_Mark.ExID FROM Exam_Mark,Exam,Course_Subject,Course WHERE " +
                    "Exam_Mark.ExID=Exam.ExID AND Exam.CSID=Course_Subject.CSID AND Course_Subject.CID=Course.CID AND " +
                    "CourseName=? AND Batch=?)");
            pendingExams.setObject(1, course);
            pendingExams.setObject(2, batch);
            pendingExams.setObject(3, course);
            pendingExams.setObject(4, batch);
            ResultSet exams = pendingExams.executeQuery();

            PreparedStatement getData = DBConnection.getInstance().
                    getConnection().prepareStatement("SELECT SubName,ExamName,Exam_Date,Start,End FROM Exam,Course_Subject,Subject " +
                    "WHERE Exam.CSID=Course_Subject.CSID AND Course_Subject.SubID=Subject.SubID AND ExID=? ORDER BY Exam_Date ASC");
            ResultSet resultSet = null;
            String name = null;
            while (exams.next()) {
                getData.setObject(1, exams.getString(1));
                resultSet = getData.executeQuery();
                if (resultSet.next()) {
                    if (resultSet.getString(2).toLowerCase().contains("mandatory")) {
                        name = "";
                        for (int i = 0; i < resultSet.getString(2).length(); i++) {
                            if (i > 10) name += resultSet.getString(2).charAt(i);
                        }
                    } else {
                        name = resultSet.getString(2);
                    }
                    list.add(new PendingExamTM(
                            resultSet.getString(1),
                            name,
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getString(5)
                    ));
                }
            }
            return list;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static ArrayList<String> getCourseAndBatch(String regId) {
        ArrayList<String> list = new ArrayList<>();
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT CourseName,BatchID FROM Registration,Batch,Course " +
                            "WHERE Registration.BID=Batch.BID AND Batch.CID=Course.CID AND RID=?");
            stm.setObject(1, regId);
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                list.add(resultSet.getString(1));
                list.add(resultSet.getString(2));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<ExamDetailTM> getAllExamDetails(String regId) {
        ArrayList<ExamDetailTM> list = new ArrayList<>();
        try {
            PreparedStatement stm = DBConnection.getInstance().
                    getConnection().prepareStatement("SELECT SubName,ExamName,Exam_Date,Marks,ExamState FROM Exam_Mark,Exam," +
                    "Course_Subject,Subject WHERE Exam_Mark.ExID=Exam.ExID AND Exam.CSID=Course_Subject.CSID AND " +
                    "Course_Subject.SubID=Subject.SubID AND RID=? ORDER BY Exam_Date DESC");
            stm.setObject(1, regId);
            ResultSet resultSet = stm.executeQuery();
            String name = null;
            while (resultSet.next()) {
                if (resultSet.getString(2).toLowerCase().contains("mandatory")) {
                    name = "";
                    for (int i = 0; i < resultSet.getString(2).length(); i++) {
                        if (i > 10) name += resultSet.getString(2).charAt(i);
                    }
                } else {
                    name = resultSet.getString(2);
                }
                list.add(new ExamDetailTM(
                        resultSet.getString(1),
                        name,
                        resultSet.getString(3),
                        resultSet.getInt(4),
                        resultSet.getString(5)
                ));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
