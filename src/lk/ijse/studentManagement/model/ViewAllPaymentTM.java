package lk.ijse.studentManagement.model;

public class ViewAllPaymentTM {
    private String id;
    private String registrationId;
    private String studentName;
    private String date;
    private double amount;

    public ViewAllPaymentTM(String id, String registrationId, String studentName, String date, double amount) {
        this.id = id;
        this.registrationId = registrationId;
        this.studentName = studentName;
        this.date = date;
        this.amount = amount;
    }

    public ViewAllPaymentTM() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "ViewAllPaymentTM{" +
                "id='" + id + '\'' +
                ", registrationId='" + registrationId + '\'' +
                ", studentName='" + studentName + '\'' +
                ", date='" + date + '\'' +
                ", amount=" + amount +
                '}';
    }
}
