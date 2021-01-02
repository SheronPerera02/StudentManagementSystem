package lk.ijse.studentManagement.dto;

public class PaymentDTO {
    private String id;
    private String registrationId;
    private String date;
    private double amount;

    public PaymentDTO(String id, String registrationId, String date, double amount) {
        this.id = id;
        this.registrationId = registrationId;
        this.date = date;
        this.amount = amount;
    }

    public PaymentDTO() {
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
        return "PaymentDTO{" +
                "id='" + id + '\'' +
                ", registrationId='" + registrationId + '\'' +
                ", date='" + date + '\'' +
                ", amount=" + amount +
                '}';
    }
}
