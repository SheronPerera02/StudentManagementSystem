package lk.ijse.studentManagement.model;

public class AddExamTM {
    private String examId;
    private String date;
    private String course;
    private String subject;
    private int batchNo;
    private String examName;
    private String startTime;
    private String endTime;
    private int passMark;

    public AddExamTM(String examId, String date, String course, String subject, int batchNo, String examName, String startTime, String endTime, int passMark) {
        this.examId = examId;
        this.date = date;
        this.course = course;
        this.subject = subject;
        this.batchNo = batchNo;
        this.examName = examName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.passMark = passMark;
    }

    public AddExamTM() {
    }


    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(int batchNo) {
        this.batchNo = batchNo;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getPassMark() {
        return passMark;
    }

    public void setPassMark(int passMark) {
        this.passMark = passMark;
    }

    @Override
    public String toString() {
        return "AddExamTM{" +
                "examId='" + examId + '\'' +
                ", date='" + date + '\'' +
                ", course='" + course + '\'' +
                ", subject='" + subject + '\'' +
                ", batchNo=" + batchNo +
                ", examName='" + examName + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", passMark=" + passMark +
                '}';
    }
}
