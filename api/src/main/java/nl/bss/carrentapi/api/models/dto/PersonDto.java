package nl.bss.carrentapi.api.models.dto;

import javax.validation.constraints.NotEmpty;

public class PersonDto {
    public long id;
    @NotEmpty
    private String firstName;
    private String infix;
    @NotEmpty
    private String lastName;
    private String phoneNumber;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getInfix() {
        return infix;
    }

    public void setInfix(String infix) {
        this.infix = infix;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getId() {
        return id;
    }

    protected void setId(long id) {
        this.id = id;
    }
}
