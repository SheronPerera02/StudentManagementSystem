package lk.ijse.studentManagement.service;

import lk.ijse.studentManagement.db.DBConnection;
import lk.ijse.studentManagement.dto.ExamFailParticipantDTO;
import lk.ijse.studentManagement.dto.ExamMarkDTO;
import lk.ijse.studentManagement.model.MarkResultTM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MarkResultService {

    public static ArrayList<MarkResultTM> getAllStudents(String course, String subject, String batch, String examName) {

        try {
            PreparedStatement stm1 =
                    DBConnection.getInstance().
                            getConnection().
                            prepareStatement("SELECT EMID,Exam_Mark.RID,Name,Marks FROM " +
                                    "Student,Registration,Exam_Mark,Exam WHERE Student.SID=Registration.SID " +
                                    "AND Registration.RID=Exam_Mark.RID AND Exam.ExId=Exam_Mark.ExId AND CSID=? AND Batch=? AND ExamName=?");
            stm1.setObject(1, AddExamService.getCourseSubjectId(course, subject));
            stm1.setObject(2, batch);
            stm1.setObject(3, examName);

            PreparedStatement stm2 =
                    DBConnection.getInstance().
                            getConnection().
                            prepareStatement("SELECT EMID,Exam_Mark.RID,Name,Marks FROM " +
                                    "Student,Registration,Exam_Mark,Exam WHERE Student.SID=Registration.SID " +
                                    "AND Registration.RID=Exam_Mark.RID AND Exam.ExId=Exam_Mark.ExId AND CSID=? AND Batch=? AND ExamName=?");
            stm2.setObject(1, AddExamService.getCourseSubjectId(course, subject));
            stm2.setObject(2, batch);
            stm2.setObject(3, examName);

            PreparedStatement stm3 =
                    DBConnection.getInstance().
                            getConnection().
                            prepareStatement("SELECT RID,Name FROM Student,Registration,Course,Batch WHERE " +
                                    "Student.SID=Registration.SID AND Registration.BID=Batch.BID AND " +
                                    "Course.CID=Batch.CID AND CourseName=? AND BatchID=? ORDER BY RID ASC");

            stm3.setObject(1, course);
            stm3.setObject(2, batch);

            ArrayList<MarkResultTM> list = new ArrayList<>();


            String id = getNextIdFromDatabase();

            ResultSet temp = stm1.executeQuery();

            boolean next = temp.next();

            if (!next) {
                ResultSet resultSet = stm3.executeQuery();
                while (resultSet.next()) {
                    id = getNextId(id);
                    list.add(new MarkResultTM(
                            id,
                            resultSet.getString(1),
                            resultSet.getString(2),
                            ""
                    ));
                }
                cleanUpStudentsOutOfAttempts(getCourseSubjectId(course, subject), examName);
                PreparedStatement failedStudents =
                        DBConnection.getInstance().getConnection().
                                prepareStatement("SELECT RID,StudentName FROM " +
                                        "Exam_Fail_Participant WHERE CSID=? AND ExamName=?");
                failedStudents.setObject(1, AddExamService.getCourseSubjectId(course, subject));
                failedStudents.setObject(2, examName);
                ResultSet resultSet1 = failedStudents.executeQuery();
                while (resultSet1.next()) {
                    id = getNextId(id);
                    list.add(new MarkResultTM(
                            id,
                            resultSet1.getString(1),
                            resultSet1.getString(2),
                            ""
                    ));
                }
            } else {
                ResultSet students = stm2.executeQuery();
                while (students.next()) {
                    list.add(new MarkResultTM(
                            students.getString(1),
                            students.getString(2),
                            students.getString(3),
                            students.getString(4)
                    ));
                }
            }
            return list;


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getCourseSubjectId(String course, String subject) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT CSID FROM Course_Subject,Course,Subject WHERE " +
                            "Course.CID=Course_Subject.CID AND Subject.SubID=Course_Subject.SubID AND CourseName=? AND SubName=?");
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

    private static void cleanUpStudentsOutOfAttempts(String courseSubjectId, String examName) {
        try {
            PreparedStatement stm1 = DBConnection.
                    getInstance().
                    getConnection().prepareStatement("SELECT ParID,RID FROM " +
                    "Exam_Fail_Participant WHERE CSID=? AND ExamName=?");
            stm1.setObject(1, courseSubjectId);
            stm1.setObject(2, examName);
            ResultSet resultSet1 = stm1.executeQuery();
            PreparedStatement stm2 = DBConnection.
                    getInstance().getConnection().prepareStatement("SELECT COUNT(EMID) FROM Exam_Mark,Exam WHERE " +
                    "Exam_Mark.ExID=Exam.ExID AND RID=? AND CSID=? AND ExamName=?");
            while (resultSet1.next()) {
                stm2.setObject(1, resultSet1.getString(2));
                stm2.setObject(2, courseSubjectId);
                stm2.setObject(3, examName);
                ResultSet resultSet2 = stm2.executeQuery();
                if (resultSet2.next()) {
                    if (resultSet2.getString(1).contentEquals("3")) {
                        deleteFromFailParticipant(resultSet1.getString(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String getNextIdFromDatabase() {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT EMID FROM Exam_Mark ORDER BY EMID DESC LIMIT 1");
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                String[] split = resultSet.getString(1).split("EM");
                int val = Integer.parseInt(split[1]) + 1;
                if (val < 10) {
                    return "EM00000" + val;
                } else if (val < 100) {
                    return "EM0000" + val;
                } else if (val < 1000) {
                    return "EM000" + val;
                } else if (val < 10000) {
                    return "EM00" + val;
                } else if (val < 100000) {
                    return "EM0" + val;
                } else {
                    return "EM" + val;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getNextId(String id) {
        if (id == null) {
            return "EM000001";
        } else {
            String[] split = id.split("EM");
            int val = Integer.parseInt(split[1]) + 1;
            if (val < 10) {
                return "EM00000" + val;
            } else if (val < 100) {
                return "EM0000" + val;
            } else if (val < 1000) {
                return "EM000" + val;
            } else if (val < 10000) {
                return "EM00" + val;
            } else if (val < 100000) {
                return "EM0" + val;
            } else {
                return "EM" + val;
            }
        }
    }

    public static ArrayList<String> getAllExamNames(String courseSubjectId, String batch) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT ExamName FROM Exam WHERE " +
                            "CSID=? AND Batch=?");
            stm.setObject(1, courseSubjectId);
            stm.setObject(2, batch);
            ResultSet resultSet = stm.executeQuery();
            ArrayList<String> list = new ArrayList<>();
            String name;
            while (resultSet.next()) {
                if (resultSet.getString(1).toLowerCase().contains("mandatory")) {
                    name = "";
                    for (int i = 0; i < resultSet.getString(1).length(); i++) {
                        if (i > 10) name += resultSet.getString(1).charAt(i);
                    }
                    list.add(name);
                } else {
                    list.add(resultSet.getString(1));
                }
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getExamId(String courseSubjectId, String batch, String examName) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT ExID FROM Exam WHERE " +
                            "CSID=? AND Batch=? AND ExamName=?");
            stm.setObject(1, courseSubjectId);
            stm.setObject(2, batch);
            stm.setObject(3, examName);
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getPassMark(String examId) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT PassMark FROM Exam WHERE " +
                            "ExID=?");
            stm.setObject(1, examId);
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static boolean setExamMarks(ArrayList<ExamMarkDTO> results) {
        try {
            PreparedStatement stm1 =
                    DBConnection.
                            getInstance().getConnection().prepareStatement("INSERT INTO Exam_Mark VALUES(?,?,?,?,?)");
            boolean isSet = false;
            for (ExamMarkDTO dto : results) {
                stm1.setObject(1, dto.getId());
                stm1.setObject(2, dto.getRegId());
                stm1.setObject(3, dto.getExamId());
                stm1.setObject(4, dto.getMarks());
                stm1.setObject(5, dto.getExamState());
                isSet = stm1.executeUpdate() > 0;
            }
            boolean mandatory = AddExamService.isMandatory(results.get(0).getExamId());
            if (mandatory) {
                String id = null;
                for (ExamMarkDTO dto : results) {
                    if (dto.getExamState().contentEquals("Fail")) {
                        id = getNextFailParticipantId();
                        isSet = addToFailParticipant(new ExamFailParticipantDTO(
                                id == null ? "Par0001" : id,
                                dto.getRegId(),
                                getStudentName(dto.getRegId()),
                                getCourseSubjectId(dto.getExamId()),
                                getExamName(dto.getExamId())
                        ));
                    }
                }
                PreparedStatement stm2 =
                        DBConnection.
                                getInstance().getConnection().prepareStatement("SELECT ParID FROM " +
                                "Exam_Fail_Participant WHERE RID=? AND CSID=? AND ExamName=?");
                ResultSet resultSet = null;
                for (ExamMarkDTO dto : results) {
                    if (dto.getExamState().contentEquals("Pass")) {
                        stm2.setObject(1, dto.getRegId());
                        stm2.setObject(2, getCourseSubjectId(dto.getExamId()));
                        stm2.setObject(3, getExamName(dto.getExamId()));
                        resultSet = stm2.executeQuery();
                        if (resultSet.next()) {
                            deleteFromFailParticipant(resultSet.getString(1));
                        }
                    }
                }
            }
            return isSet;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void deleteFromFailParticipant(String parId) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("DELETE FROM Exam_Fail_Participant WHERE ParID=?");
            stm.setObject(1, parId);
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String getExamName(String exId) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT ExamName FROM Exam WHERE ExID=?");
            stm.setObject(1, exId);
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getStudentName(String regId) {
        try {
            PreparedStatement stm = DBConnection.getInstance().
                    getConnection().
                    prepareStatement("SELECT Name FROM Student,Registration WHERE " +
                            "Student.SID=Registration.SID AND RID=?");
            stm.setObject(1, regId);
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean addToFailParticipant(ExamFailParticipantDTO dto) {

        try {
            boolean alreadyExists = failParticipantAlreadyExists(dto);
            System.out.println(alreadyExists);
            if(!alreadyExists){
                PreparedStatement stm =
                        DBConnection.getInstance().
                                getConnection().prepareStatement("INSERT INTO Exam_Fail_Participant VALUES(?,?,?,?,?)");
                stm.setObject(1, dto.getId());
                stm.setObject(2, dto.getRegId());
                stm.setObject(3, dto.getStudentName());
                stm.setObject(4, dto.getCourseSubjectId());
                stm.setObject(5, dto.getExamName());
                return stm.executeUpdate() > 0;
            } else{
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean failParticipantAlreadyExists(ExamFailParticipantDTO dto) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT ParID FROM Exam_Fail_Participant WHERE " +
                            "RID=? AND CSID=? AND ExamName=?");
            stm.setObject(1,dto.getRegId());
            stm.setObject(2,dto.getCourseSubjectId());
            stm.setObject(3,dto.getExamName());
            ResultSet resultSet = stm.executeQuery();
            if(resultSet.next()){
                return resultSet.getString(1)!=null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String getNextFailParticipantId() {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT ParID FROM Exam_Fail_Participant " +
                            "ORDER BY ParID DESC LIMIT 1");
            ResultSet resultSet = stm.executeQuery();

            if (resultSet.next()) {
                String id = resultSet.getString(1);
                String[] split = id.split("Par");
                int val = Integer.parseInt(split[1]) + 1;
                if (val < 10) {
                    return "Par000" + val;
                } else if (val < 100) {
                    return "Par00" + val;
                } else if (val < 1000) {
                    return "Par0" + val;
                } else {
                    return "Par" + val;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getCourseSubjectId(String examId) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT CSID FROM Exam WHERE ExID=?");
            stm.setObject(1, examId);
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isMandatory(String courseSubjectId, String batch, String examName) {

        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT ExamName FROM Exam WHERE CSID=? AND Batch=?");
            stm.setObject(1, courseSubjectId);
            stm.setObject(2, batch);
            ResultSet resultSet = stm.executeQuery();
            String exam = null;
            while (resultSet.next()) {
                if (resultSet.getString(1).toLowerCase().contains(examName.toLowerCase())) {
                    exam = resultSet.getString(1);
                }
            }
            if (exam != null) {
                return exam.toLowerCase().contains("mandatory");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    public static boolean marksAlreadySet(String examId) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("SELECT EMID FROM Exam_Mark WHERE ExID=? LIMIT 1");
            stm.setObject(1, examId);
            ResultSet resultSet = stm.executeQuery();
            String string = null;
            if (resultSet.next()) {
                string = resultSet.getString(1);
            }
            return string != null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateExamMarks(ArrayList<ExamMarkDTO> results) {
        try {
            PreparedStatement stm =
                    DBConnection.getInstance().
                            getConnection().prepareStatement("UPDATE Exam_Mark SET Marks=?,ExamState=? WHERE EMID=?");
            boolean isUpdated = false;
            for (ExamMarkDTO dto : results) {
                stm.setObject(1, dto.getMarks());
                stm.setObject(2, dto.getExamState());
                stm.setObject(3, dto.getId());
                isUpdated = stm.executeUpdate() > 0;
            }
            return isUpdated;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean getDateVerification(String examId) {
        try {
            PreparedStatement stm = DBConnection.getInstance().
                    getConnection().prepareStatement("SELECT DATEDIFF(CURDATE(),(SELECT Exam_Date FROM Exam WHERE ExID=?))");
            stm.setObject(1,examId);
            ResultSet resultSet = stm.executeQuery();
            if(resultSet.next()){
                return resultSet.getInt(1)>0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
