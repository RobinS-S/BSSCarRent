package nl.bss.carrentapi.api.services;

import nl.bss.carrentapi.api.enums.CombustionFuelType;
import nl.bss.carrentapi.api.models.entities.BatteryElectricCar;
import nl.bss.carrentapi.api.models.entities.CombustionCar;
import nl.bss.carrentapi.api.models.entities.FuelCellCar;
import nl.bss.carrentapi.api.models.entities.User;
import nl.bss.carrentapi.api.services.interfaces.CarService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CarServiceImpl implements CarService {
    /**
     * Creates a new combustion car along with car info and fuel type. Still needs to be saved in order to persist.
     */
    @Override
    public CombustionCar createCombustionCar(String brand, String model, String color, String licensePlate, int kilometersCurrent, Double pricePerKilometer, Double initialCost, LocalDate constructed, LocalDate apkUntil, CombustionFuelType combustionFuelType, User owner) {
        return new CombustionCar(brand, model, color, licensePlate, kilometersCurrent, pricePerKilometer, initialCost, constructed, apkUntil, combustionFuelType, owner);
    }

    /**
     * Creates a new battery-electric car along with car info. Still needs to be saved in order to persist.
     */
    @Override
    public BatteryElectricCar createBatteryElectricCar(String brand, String model, String color, String licensePlate, int kilometersCurrent, Double pricePerKilometer, Double initialCost, LocalDate constructed, LocalDate apkUntil, User owner) {
        return new BatteryElectricCar(brand, model, color, licensePlate, kilometersCurrent, pricePerKilometer, initialCost, constructed, apkUntil, owner);
    }

    /**
     * Creates a new fuel-cell car along with car info. Still needs to be saved in order to persist.
     */
    @Override
    public FuelCellCar createFuelCellCar(String brand, String model, String color, String licensePlate, int kilometersCurrent, Double pricePerKilometer, Double initialCost, LocalDate constructed, LocalDate apkUntil, User owner) {
        return new FuelCellCar(brand, model, color, licensePlate, kilometersCurrent, pricePerKilometer, initialCost, constructed, apkUntil, owner);
    }
}