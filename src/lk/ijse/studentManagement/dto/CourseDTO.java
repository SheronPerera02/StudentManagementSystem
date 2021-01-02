package lk.ijse.studentManagement.dto;

public class CourseDTO {
    private String id;
    private String courseName;
    private String duration;
    private double courseFee;

    public CourseDTO(String id, String courseName, String duration, double courseFee) {
        this.id = id;
        this.courseName = courseName;
        this.duration = duration;
        this.courseFee = courseFee;
    }

    public CourseDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public double getCourseFee() {
        return courseFee;
    }

    public void setCourseFee(double courseFee) {
        this.courseFee = courseFee;
    }

    @Override
    public String toString() {
        return "CourseDTO{" +
                "id='" + id + '\'' +
                ", courseName='" + courseName + '\'' +
                ", duration='" + duration + '\'' +
                ", courseFee=" + courseFee +
                '}';
    }
}
