package nl.bss.carrentapi.api.models.entities;

import nl.bss.carrentapi.api.models.interfaces.CostCalculable;

import javax.persistence.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    protected Car() {
    }

    public Car(String brand, String model, String color, int kilometersCurrent, Double pricePerKilometer, User owner) {
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.kilometersCurrent = kilometersCurrent;
        this.pricePerKilometer = pricePerKilometer;
        this.owner = owner;
    }

    public String getBrand() {
        return brand;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}