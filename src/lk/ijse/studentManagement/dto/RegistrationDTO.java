package lk.ijse.studentManagement.dto;

public class RegistrationDTO {
    private String id;
    private String studentId;
    private String  batchId;
    private String registerDate;
    private double fee;

    public RegistrationDTO(String id, String studentId, String batchId, String registerDate, double fee) {
        this.id = id;
        this.studentId = studentId;
        this.batchId = batchId;
        this.registerDate = registerDate;
        this.fee = fee;

    }

    public RegistrationDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    @Override
    public String toString() {
        return "RegistrationDTO{" +
                "id='" + id + '\'' +
                ", studentId='" + studentId + '\'' +
                ", batchId='" + batchId + '\'' +
                ", registerDate='" + registerDate + '\'' +
                ", fee=" + fee +
                '}';
    }
}
