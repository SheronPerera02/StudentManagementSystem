package lk.ijse.studentManagement.model;

public class ViewResultTM {
    private String regId;
    private String studentName;
    private int marks;
    private String examState;

    public ViewResultTM(String regId, String studentName, int marks, String examState) {
        this.regId = regId;
        this.studentName = studentName;
        this.marks = marks;
        this.examState = examState;
    }

    public ViewResultTM() {
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
        return "ViewExamTM{" +
                "regId='" + regId + '\'' +
                ", studentName='" + studentName + '\'' +
                ", marks='" + marks + '\'' +
                ", examState='" + examState + '\'' +
                '}';
    }
}
