package nl.bss.carrentapi.api.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "renter")
    private Set<Invoice> rentalInvoices;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private Set<Invoice> rentOutInvoices;

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
    }

    @Override
    public String toString() {
        return String.format("%s, cars: %d, rentals: %d", String.join(" ", this.firstName, this.infix, this.lastName), this.ownedCars.size(), this.rentals.size());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
