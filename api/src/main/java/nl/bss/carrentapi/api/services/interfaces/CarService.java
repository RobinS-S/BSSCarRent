package nl.bss.carrentapi.api.services.interfaces;

import nl.bss.carrentapi.api.enums.CombustionFuelType;
import nl.bss.carrentapi.api.models.entities.BatteryElectricCar;
import nl.bss.carrentapi.api.models.entities.CombustionCar;
import nl.bss.carrentapi.api.models.entities.FuelCellCar;
import nl.bss.carrentapi.api.models.entities.User;

import java.time.LocalDate;

public interface CarService {
    public abstract CombustionCar createCombustionCar(String brand, String model, String color, String licensePlate, int kilometersCurrent, Double pricePerHour, Double pricePerKilometer, Double initialCost, LocalDate constructed, LocalDate apkUntil, CombustionFuelType combustionFuelType, User owner);

    public abstract BatteryElectricCar createBatteryElectricCar(String brand, String model, String color, String licensePlate, int kilometersCurrent, Double pricePerHour, Double pricePerKilometer, Double initialCost, LocalDate constructed, LocalDate apkUntil, User owner);

    public abstract FuelCellCar createFuelCellCar(String brand, String model, String color, String licensePlate, int kilometersCurrent, Double pricePerHour, Double pricePerKilometer, Double initialCost, LocalDate constructed, LocalDate apkUntil, User owner);
}
