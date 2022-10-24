package nl.bss.carrentapi.api.models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "battery_electric_cars", schema = "PUBLIC")
@DiscriminatorValue("BEV")
public class BatteryElectricCar extends Car {
    protected BatteryElectricCar() {
    }

    public BatteryElectricCar(String brand, String model, String color, String licensePlate, long kilometersCurrent, Double pricePerHour, Double pricePerKilometer, Double initialCost, LocalDate constructed, LocalDate apkUntil, Double lat, Double lng, User owner) {
        super(brand, model, color, licensePlate, kilometersCurrent, pricePerHour, pricePerKilometer, initialCost, constructed, apkUntil, lat, lng, owner);
    }

    @Override
    public Double calculateAnnualCost() {
        double defaultKm = 15200.0; // Default kilometers per year. Source: https://www.thinkinsure.ca/insurance-help-centre/average-km-per-year-canada.html#:~:text=According%20to%20the%20NRCan%20Vehicle,you%20drive%20in%20a%20year.
        double pricePerKm = this.getPricePerKilometer();
        return defaultKm * pricePerKm;
    }

    @Override
    public Double calculateCostForKms(long kms) {
        return kms * (0.08 + this.getPricePerKilometer());
    }
}
