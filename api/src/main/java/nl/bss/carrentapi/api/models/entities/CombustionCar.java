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
    protected CombustionCar() {
    }

    public CombustionCar(String brand, String model, String color, String licensePlate, int kilometersCurrent, Double pricePerHour, Double pricePerKilometer, Double initialCost, LocalDate constructed, LocalDate apkUntil, CombustionFuelType combustionFuelType, User owner) {
        super(brand, model, color, licensePlate, kilometersCurrent, pricePerHour, pricePerKilometer, initialCost, constructed, apkUntil, owner);
        super.setFuelType(combustionFuelType);
    }

    @Override
    public Double calculateMonthlyCost() {
        return null;
    }

    @Override
    public Double calculateCostForKms(long kms) {
        Double pricePerKm = 0.0;
        switch(this.getFuelType()) {
            case GAS:
                pricePerKm = 0.07;
                break;
            case GASOLINE:
                pricePerKm = 0.14;
                break;
            case DIESEL:
                pricePerKm = 0.08;
                break;
        }
        return kms * (pricePerKm + this.getPricePerKilometer());
    }
}
