package nl.bss.carrentapi.api.models;

import lombok.Getter;
import lombok.Setter;
import nl.bss.carrentapi.api.enums.CombustionFuelType;
import nl.bss.carrentapi.api.interfaces.CostCalculable;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "cars", schema = "PUBLIC")
@DiscriminatorColumn(name = "DISCRIMINATOR", discriminatorType = DiscriminatorType.STRING)
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public abstract class Car implements CostCalculable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(length = 128)
    private String brand;

    @Column(length = 128)
    private String model;

    @Column(length = 128)
    private String color;

    @Column
    private int kilometersCurrent;

    @Column
    private Double pricePerHour;

    @Column
    private Double pricePerKilometer;

    @Column
    private Double initialCost;

    @Column(length = 64)
    private String licensePlate;

    @Column
    private LocalDate constructed;

    @Column
    private LocalDate apkUntil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column
    private CombustionFuelType fuelType;

    protected Car() {
    }

    public Car(String brand, String model, String color, String licensePlate, int kilometersCurrent, Double pricePerHour, Double pricePerKilometer, Double initialCost, LocalDate constructed, LocalDate apkUntil, User owner) {
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.licensePlate = licensePlate;
        this.kilometersCurrent = kilometersCurrent;
        this.pricePerHour = pricePerHour;
        this.pricePerKilometer = pricePerKilometer;
        this.initialCost = initialCost;
        this.constructed = constructed;
        this.apkUntil = apkUntil;
        this.owner = owner;
    }
}