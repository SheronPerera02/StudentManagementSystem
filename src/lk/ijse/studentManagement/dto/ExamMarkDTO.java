package lk.ijse.studentManagement.dto;

public class ExamMarkDTO {
    private String id;
    private String regId;
    private String examId;
    private int marks;
    private String examState;

    public ExamMarkDTO(String id, String regId, String examId, int marks, String examState) {
        this.id = id;
        this.regId = regId;
        this.examId = examId;
        this.marks = marks;
        this.examState = examState;
    }

    public ExamMarkDTO() {
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

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public String getExamState() {
        return examState;
    }

    public void setExamState(String examState) {
        this.examState = examState;
    }

    @Override
    public String toString() {
        return "ExamMarkDTO{" +
                "id='" + id + '\'' +
                ", regId='" + regId + '\'' +
                ", examId='" + examId + '\'' +
                ", marks=" + marks +
                ", examState='" + examState + '\'' +
                '}';
    }
}
