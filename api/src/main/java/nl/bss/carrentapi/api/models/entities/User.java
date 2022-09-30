package nl.bss.carrentapi.api.models.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(length = 128, unique = true)
    private String email;

    @Column(length = 128)
    private String password;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private Set<Car> ownedCars;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tenant")
    private Set<Rental> rentals;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "carOwner")
    private Set<Rental> currentContracts;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "renter")
    private Set<Invoice> rentalInvoices;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private Set<Invoice> rentOutInvoices;

    @Column
    private long score;

    protected User() {
    }

    public User(String email, String password, String firstName, String infix, String lastName, String phoneInternationalCode, String phoneNumber, LocalDate birthDate) {
        super(firstName, infix, lastName, phoneInternationalCode, phoneNumber, birthDate);
        this.email = email;
        this.password = password;
        this.ownedCars = new HashSet<>();
        this.rentals = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Set<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(Set<Rental> rentals) {
        this.rentals = rentals;
    }

    public Set<Rental> getCurrentContracts() {
        return currentContracts;
    }

    public void setCurrentContracts(Set<Rental> currentContracts) {
        this.currentContracts = currentContracts;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return String.format("%s, cars: %d, rentals: %d", super.toString(), this.ownedCars.size(), this.rentals.size());
    }
}
