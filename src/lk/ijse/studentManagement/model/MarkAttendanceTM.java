package lk.ijse.studentManagement.model;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTimePicker;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MarkAttendanceTM {
    private String id;
    private String regId;
    private String studentName;
    private JFXCheckBox present;
    private JFXCheckBox absent;
    private JFXTimePicker arrival;
    private JFXTimePicker departure;

    public MarkAttendanceTM(String id, String regId, String studentName, JFXCheckBox present, JFXCheckBox absent, String arrival, String departure) {
        this.id = id;
        this.regId = regId;
        this.studentName = studentName;
        this.present = present;
        this.absent = absent;
        if (arrival != null) {
            JFXTimePicker tp1 = new JFXTimePicker();
            tp1.setValue(LocalTime.of(getHour(arrival), getMinute(arrival)));
            this.arrival = tp1;
        } else {
            this.arrival = new JFXTimePicker();
        }
        if (departure != null) {
            JFXTimePicker tp2 = new JFXTimePicker();
            tp2.setValue(LocalTime.of(getHour(departure), getMinute(departure)));
            this.departure = tp2;
        } else {
            this.departure = new JFXTimePicker();
        }
    }

    public MarkAttendanceTM() {
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

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public JFXCheckBox getPresent() {
        return present;
    }

    public void setPresent(JFXCheckBox present) {
        this.present = present;
    }

    public JFXCheckBox getAbsent() {
        return absent;
    }

    public void setAbsent(JFXCheckBox absent) {
        this.absent = absent;
    }

    public JFXTimePicker getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        JFXTimePicker tp = new JFXTimePicker();
        tp.setValue(LocalTime.of(getHour(arrival), getMinute(arrival)));
        this.arrival = tp;
    }

    public JFXTimePicker getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        JFXTimePicker tp = new JFXTimePicker();
        tp.setValue(LocalTime.of(getHour(departure), getMinute(departure)));
        this.departure = tp;
    }

    private int getHour(String time) {
        String hour = "";
        hour += time.charAt(0);
        hour += time.charAt(1);
        return Integer.parseInt(hour);
    }

    private int getMinute(String time) {
        String minute = "";
        minute += time.charAt(3);
        minute += time.charAt(4);
        return Integer.parseInt(minute);
    }

    public String getArrivalString() {
        if(this.arrival.getValue()!=null){
            return DateTimeFormatter.ofPattern("HH:mm:ss").format(this.arrival.getValue());
        }
        return null;
    }

    public String getDepartureString() {
        if(this.departure.getValue()!=null){
            return DateTimeFormatter.ofPattern("HH:mm:ss").format(this.departure.getValue());
        }
        return null;
    }


    @Override
    public String toString() {
        return "MarkAttendanceTM{" +
                "id='" + id + '\'' +
                ", regId='" + regId + '\'' +
                ", studentName='" + studentName + '\'' +
                ", present=" + present +
                ", absent=" + absent +
                ", arrival=" + arrival +
                ", departure=" + departure +
                '}';
    }
}
