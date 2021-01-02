package lk.ijse.studentManagement.model;

public class ViewSubjectTM {
    private String id;
    private String subjectName;

    public ViewSubjectTM(String id, String subjectName) {
        this.id = id;
        this.subjectName = subjectName;
    }

    public ViewSubjectTM() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    @Override
    public String toString() {
        return "ViewSubjectTM{" +
                "id='" + id + '\'' +
                ", subjectName='" + subjectName + '\'' +
                '}';
    }
}
