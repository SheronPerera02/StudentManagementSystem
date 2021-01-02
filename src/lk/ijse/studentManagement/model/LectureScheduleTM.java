package lk.ijse.studentManagement.model;

public class LectureScheduleTM {
    private String id;
    private String dayName;
    private String lecturer;
    private String course;
    private int batch;
    private String subject;
    private String description;
    private String start;
    private String end;

    public LectureScheduleTM(String id, String dayName, String lecturer, String course, int batch, String subject, String description, String start, String end) {
        this.id = id;
        this.dayName = dayName;
        this.lecturer = lecturer;
        this.course = course;
        this.batch = batch;
        this.subject = subject;
        this.description = description;
        this.start = start;
        this.end = end;
    }

    public LectureScheduleTM() {
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

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getBatch() {
        return batch;
    }

    public void setBatch(int batch) {
        this.batch = batch;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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
        return "LectureScheduleTM{" +
                "id='" + id + '\'' +
                ", dayName='" + dayName + '\'' +
                ", lecturer='" + lecturer + '\'' +
                ", course='" + course + '\'' +
                ", batch=" + batch +
                ", subject='" + subject + '\'' +
                ", description='" + description + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                '}';
    }
}
