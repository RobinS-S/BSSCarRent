package nl.bss.carrentapi.api.services.interfaces;

import nl.bss.carrentapi.api.enums.CombustionFuelType;
import nl.bss.carrentapi.api.models.BatteryElectricCar;
import nl.bss.carrentapi.api.models.CombustionCar;
import nl.bss.carrentapi.api.models.FuelCellCar;
import nl.bss.carrentapi.api.models.User;

import java.time.LocalDate;

public interface CarService {
    CombustionCar createCombustionCar(String brand, String model, String color, String licensePlate, int kilometersCurrent, Double pricePerHour, Double pricePerKilometer, Double initialCost, LocalDate constructed, LocalDate apkUntil, CombustionFuelType combustionFuelType, User owner);

    BatteryElectricCar createBatteryElectricCar(String brand, String model, String color, String licensePlate, int kilometersCurrent, Double pricePerHour, Double pricePerKilometer, Double initialCost, LocalDate constructed, LocalDate apkUntil, User owner);

    FuelCellCar createFuelCellCar(String brand, String model, String color, String licensePlate, int kilometersCurrent, Double pricePerHour, Double pricePerKilometer, Double initialCost, LocalDate constructed, LocalDate apkUntil, User owner);
}
