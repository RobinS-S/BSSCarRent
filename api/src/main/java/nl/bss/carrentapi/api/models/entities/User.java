package nl.bss.carrentapi.api.models.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(length = 128, unique = true)
    private String email;

    @Column(length = 128)
    private String password;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private Set<Car> ownedCars;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tenant")
    private Set<Rental> rentals;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "carOwner")
    private Set<Rental> currentContracts;

    @Column
    private long score;

    protected User() {
    }

    public User(String email, String password, String firstName, String infix, String lastName, String phoneInternationalCode, String phoneNumber, LocalDate birthDate) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.infix = infix;
        this.lastName = lastName;
        this.phoneInternationalCode = phoneInternationalCode;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.ownedCars = new HashSet<>();
        this.rentals = new HashSet<>();
    }

    public long getId() { return id; }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Set<Car> getOwnedCars() {
        return ownedCars;
    }

    public void setOwnedCars(Set<Car> ownedCars) {
        this.ownedCars = ownedCars;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(long id) {
        this.id = id;
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

    public Set<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(Set<Rental> rentals) {
        this.rentals = rentals;
    }

    public Set<Rental> getCurrentContracts() { return currentContracts; }

    public void setCurrentContracts(Set<Rental> currentContracts) { this.currentContracts = currentContracts; }

    public long getScore() {
        return score;
    }

    public void setScore(long score) { this.score = score; }

    public String getPhoneInternationalCode() { return phoneInternationalCode; }

    public void setPhoneInternationalCode(String phoneInternationalCode) { this.phoneInternationalCode = phoneInternationalCode; }

    public String getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    @Override
    public String toString() {
        return String.format("%s, cars: %d, rentals: %d", String.join(" ", this.firstName, this.infix, this.lastName), this.ownedCars.size(), this.rentals.size());
    }
}
