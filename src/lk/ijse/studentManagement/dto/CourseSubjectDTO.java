package lk.ijse.studentManagement.dto;

public class CourseSubjectDTO {
    private String id;
    private String courseId;
    private String subjectId;
    private String semester;

    public CourseSubjectDTO(String id, String courseId, String subjectId, String semester) {
        this.id = id;
        this.courseId = courseId;
        this.subjectId = subjectId;
        this.semester = semester;
    }

    public CourseSubjectDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return "CourseSubjectDTO{" +
                "id='" + id + '\'' +
                ", courseId='" + courseId + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", semester='" + semester + '\'' +
                '}';
    }
}
