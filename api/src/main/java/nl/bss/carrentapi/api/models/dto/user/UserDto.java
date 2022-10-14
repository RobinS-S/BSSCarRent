package nl.bss.carrentapi.api.models.dto.user;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserDto {
    @NotNull
    private long id;
    @NotEmpty
    private String firstName;
    private String infix;
    @NotEmpty
    private String lastName;
    @NotEmpty
    private String phoneInternationalCode;
    @NotEmpty
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

    public String getPhoneInternationalCode() {
        return phoneInternationalCode;
    }

    public void setPhoneInternationalCode(String phoneInternationalCode) {
        this.phoneInternationalCode = phoneInternationalCode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
