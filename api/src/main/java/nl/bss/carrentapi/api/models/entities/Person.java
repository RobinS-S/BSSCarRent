package nl.bss.carrentapi.api.models.entities;

import javax.persistence.Column;
import java.time.LocalDate;

public class Person {
    @Column(length = 128)
    private String firstName;

    @Column(length = 128)
    private String infix;

    @Column(length = 128)
    private String lastName;

    @Column(length = 8)
    private String phoneInternationalCode;

    @Column(length = 16)
    private String phoneNumber;

    @Column
    private LocalDate birthDate;

    public Person(String firstName, String infix, String lastName, String phoneInternationalCode, String phoneNumber, LocalDate birthDate) {
        this.firstName = firstName;
        this.infix = infix;
        this.lastName = lastName;
        this.phoneInternationalCode = phoneInternationalCode;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
    }

    protected Person() {
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

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

    public String getPhoneInternationalCode() {
        return phoneInternationalCode;
    }

    public void setPhoneInternationalCode(String phoneInternationalCode) {
        this.phoneInternationalCode = phoneInternationalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return String.join(" ", this.firstName, this.infix, this.lastName);
    }
}
