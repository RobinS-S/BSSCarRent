package nl.bss.carrentapi.api.models.entities;

import nl.bss.carrentapi.api.enums.ElectricType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "electric_cars", schema = "PUBLIC")
@DiscriminatorValue("EC")
public class ElectricCar extends Car {
    private ElectricType EnergyType;

    public ElectricType getEnergyType() {
        return EnergyType;
    }

    protected ElectricCar() { }

    public ElectricCar(String brand, String model, String color, int kilometersCurrent, Double pricePerKilometer, ElectricType energyType, User owner) {
        super(brand, model, color, kilometersCurrent, pricePerKilometer, owner);
        EnergyType = energyType;
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
