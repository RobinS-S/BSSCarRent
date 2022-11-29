package nl.bss.carrentapi.api.services;

import lombok.AllArgsConstructor;
import nl.bss.carrentapi.api.enums.CombustionFuelType;
import nl.bss.carrentapi.api.exceptions.NotAllowedException;
import nl.bss.carrentapi.api.exceptions.NotFoundException;
import nl.bss.carrentapi.api.misc.ImageUtil;
import nl.bss.carrentapi.api.models.*;
import nl.bss.carrentapi.api.repository.CarImageRepository;
import nl.bss.carrentapi.api.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class CarService {
    private final CarImageRepository carImageRepository;
    private final CarRepository carRepository;

    /**
     * Creates a new combustion car along with car info and fuel type. Still needs to be saved in order to persist.
     */
    public CombustionCar createCombustionCar(String brand, String model, String color, String licensePlate, long kilometersCurrent, Double pricePerHour, Double pricePerKilometer, Double initialCost, LocalDate constructed, LocalDate apkUntil, Double lat, Double lng, CombustionFuelType combustionFuelType, User owner) {
        return new CombustionCar(brand, model, color, licensePlate, kilometersCurrent, pricePerHour, pricePerKilometer, initialCost, constructed, apkUntil, lat, lng, combustionFuelType, owner);
    }

    /**
     * Creates a new battery-electric car along with car info. Still needs to be saved in order to persist.
     */
    public BatteryElectricCar createBatteryElectricCar(String brand, String model, String color, String licensePlate, long kilometersCurrent, Double pricePerHour, Double pricePerKilometer, Double initialCost, LocalDate constructed, LocalDate apkUntil, Double lat, Double lng, User owner) {
        return new BatteryElectricCar(brand, model, color, licensePlate, kilometersCurrent, pricePerHour, pricePerKilometer, initialCost, constructed, apkUntil, lat, lng, owner);
    }

    /**
     * Creates a new fuel-cell car along with car info. Still needs to be saved in order to persist.
     */
    public FuelCellCar createFuelCellCar(String brand, String model, String color, String licensePlate, long kilometersCurrent, Double pricePerHour, Double pricePerKilometer, Double initialCost, LocalDate constructed, LocalDate apkUntil, Double lat, Double lng, User owner) {
        return new FuelCellCar(brand, model, color, licensePlate, kilometersCurrent, pricePerHour, pricePerKilometer, initialCost, constructed, apkUntil, lat, lng, owner);
    }

    /**
     * Creates car image for given Car with data and Content-Type.
     *
     * @param contentType
     * @param bytes
     * @param car
     */
    public CarImage createCarImage(String contentType, byte[] bytes, Car car) {
        CarImage image = new CarImage(contentType);
        image.setData(ImageUtil.compressImage(bytes));
        image.car = car;
        image = carImageRepository.save(image);
        return image;
    }

    /**
     * Finds a Car with given id.
     *
     * @param id
     */
    public Car findCar(long id) {
        return carRepository.findById(id).orElseThrow(() -> new NotFoundException("That car was not found."));
    }
}
