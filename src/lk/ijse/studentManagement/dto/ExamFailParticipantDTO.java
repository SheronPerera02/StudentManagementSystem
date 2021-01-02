package lk.ijse.studentManagement.dto;

public class ExamFailParticipantDTO {
    private String id;
    private String regId;
    private String studentName;
    private String courseSubjectId;
    private String examName;

    public ExamFailParticipantDTO(String id, String regId, String studentName, String courseSubjectId, String examName) {
        this.id = id;
        this.regId = regId;
        this.studentName = studentName;
        this.courseSubjectId = courseSubjectId;
        this.examName = examName;
    }

    public ExamFailParticipantDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseSubjectId() {
        return courseSubjectId;
    }

    public void setCourseSubjectId(String courseSubjectId) {
        this.courseSubjectId = courseSubjectId;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    @Override
    public String toString() {
        return "ExamFailParticipantDTO{" +
                "id='" + id + '\'' +
                ", regId='" + regId + '\'' +
                ", studentName='" + studentName + '\'' +
                ", courseSubjectId='" + courseSubjectId + '\'' +
                ", examName='" + examName + '\'' +
                '}';
    }
}
