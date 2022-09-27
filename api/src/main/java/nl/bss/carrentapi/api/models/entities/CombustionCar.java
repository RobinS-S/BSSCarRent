package nl.bss.carrentapi.api.models.entities;

import nl.bss.carrentapi.api.enums.CombustionFuelType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "combustion_cars", schema = "PUBLIC")
@DiscriminatorValue("CC")
public class CombustionCar extends Car {
    private CombustionFuelType FuelType;

    protected CombustionCar() { }

    public CombustionCar(String brand, String model, String color, String licensePlate, int kilometersCurrent, Double pricePerKilometer, Double initialCost, LocalDate constructed, LocalDate apkUntil, CombustionFuelType combustionFuelType, User owner) {
        super(brand, model, color, licensePlate, kilometersCurrent, pricePerKilometer, initialCost, constructed, apkUntil, owner);
        FuelType = combustionFuelType;
    }

    public CombustionFuelType getFuelType() {
        return FuelType;
    }

    @Override
    public Double calculateMonthlyCost() {
        return null;
    }

    @Override
    public Double calculateCostForKms(Double kms) {
        return null;
    }
}
