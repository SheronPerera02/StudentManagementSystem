package lk.ijse.studentManagement.model;

public class GuestMainFormTM {
    private String regId;
    private String studentName;

    public GuestMainFormTM(String regId, String studentName) {
        this.regId = regId;
        this.studentName = studentName;
    }

    public GuestMainFormTM() {
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

    @Override
    public String toString() {
        return "GuestMainFormTM{" +
                "regId='" + regId + '\'' +
                ", studentName='" + studentName + '\'' +
                '}';
    }
}
