package lk.ijse.studentManagement.dto;

public class LectureScheduleDTO {
    private String id;
    private String dayName;
    private String lecturerId;
    private String courseSubjectId;
    private int batch;
    private String description;
    private String start;
    private String end;

    public LectureScheduleDTO(String id, String dayName, String lecturerId, String courseSubjectId, int batch, String description, String start, String end) {
        this.id = id;
        this.dayName = dayName;
        this.lecturerId = lecturerId;
        this.courseSubjectId = courseSubjectId;
        this.batch = batch;
        this.description = description;
        this.start = start;
        this.end = end;
    }

    public LectureScheduleDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(String lecturerId) {
        this.lecturerId = lecturerId;
    }

    public String getCourseSubjectId() {
        return courseSubjectId;
    }

    public void setCourseSubjectId(String courseSubjectId) {
        this.courseSubjectId = courseSubjectId;
    }

    public int getBatch() {
        return batch;
    }

    public void setBatch(int batch) {
        this.batch = batch;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public String toString() {
        return "LectureScheduleDTO{" +
                "id='" + id + '\'' +
                ", dayName='" + dayName + '\'' +
                ", lecturerId='" + lecturerId + '\'' +
                ", courseSubjectId='" + courseSubjectId + '\'' +
                ", batch=" + batch +
                ", description='" + description + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                '}';
    }
}
