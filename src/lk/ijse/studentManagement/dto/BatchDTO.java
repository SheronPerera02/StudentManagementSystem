package lk.ijse.studentManagement.dto;

public class BatchDTO {
    private String id;
    private int batch;
    private String courseId;
    private String startDate;
    private String endDate;
    private double regFee;
    private double courseFee;

    public BatchDTO(String id, int batch, String courseId, String startDate, String endDate, double regFee, double courseFee) {
        this.id = id;
        this.batch = batch;
        this.courseId = courseId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.regFee = regFee;
        this.courseFee = courseFee;
    }

    public BatchDTO() {
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

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
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

    public double getCourseFee() {
        return courseFee;
    }

    public void setCourseFee(double courseFee) {
        this.courseFee = courseFee;
    }

    @Override
    public String toString() {
        return "BatchDTO{" +
                "id='" + id + '\'' +
                ", batch=" + batch +
                ", courseId='" + courseId + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", regFee=" + regFee +
                ", courseFee=" + courseFee +
                '}';
    }
}
