package lk.ijse.studentManagement.model;

public class ViewAttendanceTM {
    private String regId;
    private String studentName;
    private String attendance;
    private String arrival;
    private String departure;

    public ViewAttendanceTM(String regId, String studentName, String attendance, String arrival, String departure) {
        this.regId = regId;
        this.studentName = studentName;
        this.attendance = attendance;
        this.arrival = arrival;
        this.departure = departure;
    }

    public ViewAttendanceTM() {
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

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    @Override
    public String toString() {
        return "ViewAttendanceTM{" +
                "regId='" + regId + '\'' +
                ", studentName='" + studentName + '\'' +
                ", attendance='" + attendance + '\'' +
                ", arrival='" + arrival + '\'' +
                ", departure='" + departure + '\'' +
                '}';
    }
}
