package lk.ijse.studentManagement.dto;

public class StudentDTO {
    private String id;
    private String name;
    private String email;
    private String address;
    private String contact;
    private String dateOfBirth;
    private String parentName;
    private String parentContact;

    public StudentDTO(String id, String name, String email, String address, String contact, String dateOfBirth, String parentName, String parentContact) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.contact = contact;
        this.dateOfBirth = dateOfBirth;
        this.parentName = parentName;
        this.parentContact = parentContact;
    }

    public StudentDTO() {
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentContact() {
        return parentContact;
    }

    public void setParentContact(String parentContact) {
        this.parentContact = parentContact;
    }

    @Override
    public String toString() {
        return "StudentDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", contact='" + contact + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", parentName='" + parentName + '\'' +
                ", parentContact='" + parentContact + '\'' +
                '}';
    }
}
