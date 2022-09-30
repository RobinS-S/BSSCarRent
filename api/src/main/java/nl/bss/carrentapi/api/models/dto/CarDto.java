package nl.bss.carrentapi.api.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import nl.bss.carrentapi.api.enums.CarType;
import nl.bss.carrentapi.api.enums.CombustionFuelType;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

public class CarDto {
    public long id;
    public long ownerId;
    @NotEmpty
    private String brand;
    @NotEmpty
    private String model;
    @NotEmpty
    private String color;
    @NotEmpty
    private int kilometersCurrent;
    @NotEmpty
    private Double pricePerKilometer;
    @NotEmpty
    private String licensePlate;
    @NotEmpty
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate constructed;
    @NotEmpty
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate apkUntil;
    @NotEmpty
    private CarType carType;
    private CombustionFuelType fuelType;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
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

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public long getId() {
        return id;
    }

    protected void setId(long id) {
        this.id = id;
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

    public CarType getCarType() {
        return carType;
    }

    public void setCarType(CarType carType) {
        this.carType = carType;
    }

    public CombustionFuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(CombustionFuelType fuelType) {
        this.fuelType = fuelType;
    }
}
