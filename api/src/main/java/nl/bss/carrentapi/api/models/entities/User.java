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

    @Column
    private LocalDate birthDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private Set<Car> ownedCars;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tenant")
    private Set<Rental> rentals;

    @Column
    private Long score;

    protected User() {
    }

    public User(String email, String password, String firstName, String infix, String lastName, LocalDate birthDate) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.infix = infix;
        this.lastName = lastName;
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

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public void addCar(Car car) {
        this.ownedCars.add(car);
        car.setOwner(this);
    }

    @Override
    public String toString() {
        return String.format("%s, cars: %d, rentals: %d", String.join(" ", this.firstName, this.infix, this.lastName), this.ownedCars.size(), this.rentals.size());
    }
}