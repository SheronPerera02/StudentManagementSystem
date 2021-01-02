package lk.ijse.studentManagement.model;

public class BatchTM {
    private String id;
    private int batch;
    private String course;
    private String startDate;
    private String endDate;
    private double regFee;

    public BatchTM(String id, int batch, String course, String startDate, String endDate, double regFee) {
        this.id = id;
        this.batch = batch;
        this.course = course;
        this.startDate = startDate;
        this.endDate = endDate;
        this.regFee = regFee;
    }

    public BatchTM() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getBatch() {
        return batch;
    }

    public void setBatch(int batch) {
        this.batch = batch;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public double getRegFee() {
        return regFee;
    }

    public void setRegFee(double regFee) {
        this.regFee = regFee;
    }

    @Override
    public String toString() {
        return "BatchTM{" +
                "id='" + id + '\'' +
                ", batch=" + batch +
                ", course='" + course + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", regFee=" + regFee +
                '}';
    }
}
