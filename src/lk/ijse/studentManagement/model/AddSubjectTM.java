package lk.ijse.studentManagement.model;

public class AddSubjectTM {
    private String id;
    private String course;
    private String subject;
    private String semester;

    public AddSubjectTM(String id, String course, String subject, String semester) {
        this.id = id;
        this.course = course;
        this.subject = subject;
        this.semester = semester;
    }

    public AddSubjectTM() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return "CourseSubjectTM{" +
                "id='" + id + '\'' +
                ", course='" + course + '\'' +
                ", subject='" + subject + '\'' +
                ", semester='" + semester + '\'' +
                '}';
    }
}
