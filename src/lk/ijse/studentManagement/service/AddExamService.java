package lk.ijse.studentManagement.service;

import lk.ijse.studentManagement.db.DBConnection;
import lk.ijse.studentManagement.dto.ExamDTO;
import lk.ijse.studentManagement.model.AddExamTM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AddExamService {
    public static ArrayList<String> getSubjects(String course) {
        try {
            PreparedStatement stm = DBConnection.getInstance().
                    getConnection().prepareStatement("SELECT SubName FROM Course_Subject,Subject,Course WHERE " +
                    "Course_Subject.SubID=Subject.SubID AND Course_Subject.CID=Course.CID AND CourseName=?");
            stm.setObject(1, course);
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

    public static String getNextId() {
        try {
            PreparedStatement stm = DBConnection.getInstance().
                    getConnection().prepareStatement("SELECT ExID FROM Exam ORDER BY ExID DESC LIMIT 1");
            ResultSet resultSet = stm.executeQuery();

            String string = null;
            if (resultSet.next()) {
                string = resultSet.getString(1);
            }
            if (string != null) {
                String[] split = string.split("Ex");
                int val = Integer.parseInt(split[1]) + 1;
                if (val < 10) {
                    return "Ex0000" + val;
                } else if (val < 100) {
                    return "Ex000" + val;
                } else if (val < 1000) {
                    return "Ex00" + val;
                } else if (val < 10000) {
                    return "Ex0" + val;
                } else {
                    return "Ex" + val;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCourseSubjectId(String course, String subject) {
        try {
            PreparedStatement stm = DBConnection.getInstance().
                    getConnection().prepareStatement("SELECT CSID FROM Course_Subject,Subject,Course " +
                    "WHERE Course.CID=Course_Subject.CID AND Subject.SubID=Course_Subject.SubID AND CourseName=? AND SubName=?");
            stm.setObject(1, course);
            stm.setObject(2, subject);
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addExam(ExamDTO dto) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("INSERT INTO Exam VALUES(?,?,?,?,?,?,?,?)");
            stm.setObject(1, dto.getId());
            stm.setObject(2, dto.getDate());
            stm.setObject(3, dto.getCourseSubjectId());
            stm.setObject(4, dto.getStart());
            stm.setObject(5, dto.getEnd());
            stm.setObject(6, dto.getBatchNo());
            stm.setObject(7, dto.getExamName());
            stm.setObject(8, dto.getPassMark());
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<AddExamTM> getAllExams() {
        try {
            PreparedStatement stm = DBConnection.getInstance().
                    getConnection().prepareStatement("SELECT ExID,Exam_Date,CourseName,SubName,Batch,ExamName,Start,End,PassMark FROM " +
                    "Exam,Course,Subject,Course_Subject WHERE Exam.CSID=Course_Subject.CSID AND " +
                    "Course.CID=Course_Subject.CID AND Subject.SubID=Course_Subject.SubID ORDER BY ExID DESC");
            ResultSet resultSet = stm.executeQuery();
            ArrayList<AddExamTM> list = new ArrayList<>();
            String examName = "";
            while (resultSet.next()) {
                if (resultSet.getString(6).toLowerCase().contains("mandatory")) {
                    String name = "";
                    for (int i = 0; i < resultSet.getString(6).length(); i++) {
                        if (i > 10) name += resultSet.getString(6).charAt(i);
                    }
                    examName = name;
                } else {
                    examName = resultSet.getString(6);
                }
                list.add(new AddExamTM(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getInt(5),
                        examName,
                        resultSet.getString(7),
                        resultSet.getString(8),
                        resultSet.getInt(9)
                ));
            }
            return list;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean updateExam(ExamDTO dto) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("UPDATE Exam SET " +
                            "Exam_Date=?,Start=?,End=?,PassMark=?,ExamName=? WHERE ExID=?");
            stm.setObject(1, dto.getDate());
            stm.setObject(2, dto.getStart());
            stm.setObject(3, dto.getEnd());
            stm.setObject(4, dto.getPassMark());
            stm.setObject(5, dto.getExamName());
            stm.setObject(6, dto.getId());
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isMandatory(String examId) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT ExamName FROM Exam WHERE ExID=?");
            stm.setObject(1, examId);
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1).toLowerCase().contains("mandatory");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<String> getAllExamNames(String course, String batch) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT ExamName FROM Exam,Course_Subject,Course WHERE " +
                            "Course.CID=Course_Subject.CID AND Course_Subject.CSID=Exam.CSID AND CourseName=? AND Batch=?");
            stm.setObject(1, course);
            stm.setObject(2, batch);
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
}
