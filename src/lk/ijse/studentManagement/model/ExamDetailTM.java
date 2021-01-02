package lk.ijse.studentManagement.model;

public class ExamDetailTM {
    private String subjectName;
    private String examName;
    private String examDate;
    private int marks;
    private String examState;

    public ExamDetailTM(String subjectName, String examName, String examDate, int marks, String examState) {
        this.subjectName = subjectName;
        this.examName = examName;
        this.examDate = examDate;
        this.marks = marks;
        this.examState = examState;
    }

    public ExamDetailTM() {
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getExamDate() {
        return examDate;
    }

    public void setExamDate(String examDate) {
        this.examDate = examDate;
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
        return "ExamDetailTM{" +
                "subjectName='" + subjectName + '\'' +
                ", examName='" + examName + '\'' +
                ", examDate='" + examDate + '\'' +
                ", marks=" + marks +
                ", examState='" + examState + '\'' +
                '}';
    }
}
