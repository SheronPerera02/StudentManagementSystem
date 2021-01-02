package lk.ijse.studentManagement.model;

public class ViewAllRegistrationTM {
    private String id;
    private String studentId;
    private String studentName;
    private String course;
    private String batch;
    private String regDate;
    private double fee;
    private double dueFee;

    public ViewAllRegistrationTM(String id, String studentId, String studentName, String course, String batch, String regDate, double fee, double dueFee) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.course = course;
        this.batch = batch;
        this.regDate = regDate;
        this.fee = fee;
        this.dueFee = dueFee;
    }

    public ViewAllRegistrationTM() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getDueFee() {
        return dueFee;
    }

    public void setDueFee(double dueFee) {
        this.dueFee = dueFee;
    }

    @Override
    public String toString() {
        return "ViewAllRegistrationTM{" +
                "id='" + id + '\'' +
                ", studentId='" + studentId + '\'' +
                ", studentName='" + studentName + '\'' +
                ", course='" + course + '\'' +
                ", batch='" + batch + '\'' +
                ", regDate='" + regDate + '\'' +
                ", fee=" + fee +
                ", dueFee=" + dueFee +
                '}';
    }
}
