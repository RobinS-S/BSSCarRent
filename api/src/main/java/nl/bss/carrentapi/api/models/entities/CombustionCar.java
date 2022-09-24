package nl.bss.carrentapi.api.models.entities;

import nl.bss.carrentapi.api.enums.CombustionFuelType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "combustion_cars", schema = "PUBLIC")
@DiscriminatorValue("CC")
public class CombustionCar extends Car {
    private CombustionFuelType FuelType;

    protected CombustionCar() { }

    public CombustionCar(String brand, String model, String color, int kilometersCurrent, Double pricePerKilometer, CombustionFuelType fuelType, User owner) {
        super(brand, model, color, kilometersCurrent, pricePerKilometer, owner);
        FuelType = fuelType;
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
