package nl.bss.carrentapi.api.models.dto.car;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class CarUpdateDto {
    @NotEmpty
    private String brand;
    @NotEmpty
    private String model;
    @NotEmpty
    private String color;
    @Min(value = 1)
    private int kilometersCurrent;
    @Min(value = 0)
    private Double pricePerKilometer;
    @NotEmpty
    private String licensePlate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate apkUntil;
    @NotNull
    private Double initialCost;

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

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public LocalDate getApkUntil() {
        return apkUntil;
    }

    public void setApkUntil(LocalDate apkUntil) {
        this.apkUntil = apkUntil;
    }

    public Double getInitialCost() {
        return initialCost;
    }

    public void setInitialCost(Double initialCost) {
        this.initialCost = initialCost;
    }
}
