package nl.bss.carrentapi.api.classes;

public class Car {
    private String brand;
    private String model;
    private String color;
    private int kilometersCurrent;
    private int pricePerKilometer;

    private Category carCategory;
    private Owner owner;
    private Tenant tenant;

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

    public int getPricePerKilometer() {
        return pricePerKilometer;
    }

    public void setPricePerKilometer(int pricePerKilometer) {
        this.pricePerKilometer = pricePerKilometer;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Car(String brand, String model, String color, int kilometersCurrent, int pricePerKilometer, Owner owner) {
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.kilometersCurrent = kilometersCurrent;
        this.pricePerKilometer = pricePerKilometer;
        this.owner = owner;
    }

    public Car(String brand, String model, String color, int kilometersCurrent, int pricePerKilometer) {
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.kilometersCurrent = kilometersCurrent;
        this.pricePerKilometer = pricePerKilometer;
    }
}