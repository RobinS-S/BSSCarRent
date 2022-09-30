package nl.bss.carrentapi.api.models.entities;

import nl.bss.carrentapi.api.enums.CombustionFuelType;
import nl.bss.carrentapi.api.models.interfaces.CostCalculable;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "cars", schema = "PUBLIC")
@DiscriminatorColumn(name = "DISCRIMINATOR", discriminatorType = DiscriminatorType.STRING)
@Inheritance(strategy = InheritanceType.JOINED)
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

    public Car(String brand, String model, String color, String licensePlate, int kilometersCurrent, Double pricePerKilometer, Double initialCost, LocalDate constructed, LocalDate apkUntil, User owner) {
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.licensePlate = licensePlate;
        this.kilometersCurrent = kilometersCurrent;
        this.pricePerKilometer = pricePerKilometer;
        this.initialCost = initialCost;
        this.constructed = constructed;
        this.apkUntil = apkUntil;
        this.owner = owner;
    }

    public long getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getKilometersCurrent() {
        return kilometersCurrent;
    }

    public void setKilometersCurrent(int kilometersCurrent) {
        this.kilometersCurrent = kilometersCurrent;
    }

    public Double getPricePerKilometer() {
        return pricePerKilometer;
    }

    public void setPricePerKilometer(Double pricePerKilometer) {
        this.pricePerKilometer = pricePerKilometer;
    }

    public Double getInitialCost() {
        return initialCost;
    }

    public void setInitialCost(Double initialCost) {
        this.initialCost = initialCost;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public LocalDate getConstructed() {
        return constructed;
    }

    public void setConstructed(LocalDate constructed) {
        this.constructed = constructed;
    }

    public LocalDate getApkUntil() {
        return apkUntil;
    }

    public void setApkUntil(LocalDate apkUntil) {
        this.apkUntil = apkUntil;
    }

    public CombustionFuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(CombustionFuelType fuelType) {
        this.fuelType = fuelType;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}