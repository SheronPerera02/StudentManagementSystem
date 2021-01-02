package lk.ijse.studentManagement.dto;

public class AttendanceDTO {
    private String id;
    private String regId;
    private String date;
    private String courseSubjectId;
    private int batch;
    private String eventName;
    private String attendance;
    private String arrival;
    private String departure;

    public AttendanceDTO(String id, String regId, String date, String courseSubjectId, int batch, String eventName, String attendance, String arrival, String departure) {
        this.id = id;
        this.regId = regId;
        this.date = date;
        this.courseSubjectId = courseSubjectId;
        this.batch = batch;
        this.eventName = eventName;
        this.attendance = attendance;
        this.arrival = arrival;
        this.departure = departure;
    }

    public AttendanceDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
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
        return "AttendanceDTO{" +
                "id='" + id + '\'' +
                ", regId='" + regId + '\'' +
                ", date='" + date + '\'' +
                ", courseSubjectId='" + courseSubjectId + '\'' +
                ", batch=" + batch +
                ", eventName='" + eventName + '\'' +
                ", attendance='" + attendance + '\'' +
                ", arrival='" + arrival + '\'' +
                ", departure='" + departure + '\'' +
                '}';
    }
}
