package lk.ijse.studentManagement.dto;

public class LecturerDTO {
    private String id;
    private String name;
    private String email;
    private String address;
    private String dob;
    private String contact;

    public LecturerDTO(String id, String name, String email, String address, String dob, String contact) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.dob = dob;
        this.contact = contact;
    }

    public LecturerDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "LecturerDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", eMail='" + email + '\'' +
                ", address='" + address + '\'' +
                ", dob='" + dob + '\'' +
                ", contact=" + contact +
                '}';
    }
}
