package lk.ijse.studentManagement.model;

public class ViewAttendanceEventTM {
    private String eventName;
    private String eventDate;

    public ViewAttendanceEventTM(String eventName, String eventDate) {
        this.eventName = eventName;
        this.eventDate = eventDate;
    }

    public ViewAttendanceEventTM() {
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    @Override
    public String toString() {
        return "ViewAttendanceEventTM{" +
                "eventName='" + eventName + '\'' +
                ", eventDate='" + eventDate + '\'' +
                '}';
    }
}
