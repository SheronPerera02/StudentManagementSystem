package lk.ijse.studentManagement.dto;

public class ExamDTO {
    private String id;
    private String date;
    private String courseSubjectId;
    private String start;
    private String end;
    private int batchNo;
    private String examName;
    private int passMark;

    public ExamDTO(String id, String date, String courseSubjectId, String start, String end, int batchNo, String examName, int passMark) {
        this.id = id;
        this.date = date;
        this.courseSubjectId = courseSubjectId;
        this.start = start;
        this.end = end;
        this.batchNo = batchNo;
        this.examName = examName;
        this.passMark = passMark;
    }

    public ExamDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCourseSubjectId() {
        return courseSubjectId;
    }

    public void setCourseSubjectId(String courseSubjectId) {
        this.courseSubjectId = courseSubjectId;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
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

    public int getPassMark() {
        return passMark;
    }

    public void setPassMark(int passMark) {
        this.passMark = passMark;
    }

    @Override
    public String toString() {
        return "ExamDTO{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", courseSubjectId='" + courseSubjectId + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", batchNo=" + batchNo +
                ", examName='" + examName + '\'' +
                ", passMark=" + passMark +
                '}';
    }
}
