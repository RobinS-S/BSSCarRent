package nl.bss.carrentapi.api.models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "fuel_cell_cars", schema = "PUBLIC")
@DiscriminatorValue("FCEV")
public class FuelCellCar extends Car {
    protected FuelCellCar() {
    }

    public FuelCellCar(String brand, String model, String color, String licensePlate, int kilometersCurrent, Double pricePerHour, Double pricePerKilometer, Double initialCost, LocalDate constructed, LocalDate apkUntil, User owner) {
        super(brand, model, color, licensePlate, kilometersCurrent, pricePerHour, pricePerKilometer, initialCost, constructed, apkUntil, owner);
    }

    @Override
    public Double calculateMonthlyCost() {
        return null;
    }

    @Override
    public Double calculateCostForKms(long kms) {
        return kms * (this.getPricePerKilometer() + 0.10);
    }
}
