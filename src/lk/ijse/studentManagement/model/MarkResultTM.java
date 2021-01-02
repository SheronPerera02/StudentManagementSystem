package lk.ijse.studentManagement.model;

import javafx.scene.control.TextField;

public class MarkResultTM {
    private String id;
    private String regId;
    private String studentName;
    private TextField marks;

    public MarkResultTM(String id, String regId, String studentName, String marks) {
        this.id = id;
        this.regId = regId;
        this.studentName = studentName;
        this.marks = new TextField(marks);
    }

    public MarkResultTM() {
    }

    public String getId() {
        return id;

    }

    public void setId(String id){
        this.id=id;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public TextField getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks.setText(marks);
    }

    @Override
    public String toString() {
        return "MarkResultTM{" +
                "id='" + id + '\'' +
                ", regId='" + regId + '\'' +
                ", studentName='" + studentName + '\'' +
                ", marks=" + marks +
                '}';
    }
}
