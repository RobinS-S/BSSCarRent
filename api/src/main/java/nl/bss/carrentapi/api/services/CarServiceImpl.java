package nl.bss.carrentapi.api.services;

import lombok.AllArgsConstructor;
import nl.bss.carrentapi.api.enums.CombustionFuelType;
import nl.bss.carrentapi.api.exceptions.NotAllowedException;
import nl.bss.carrentapi.api.misc.ImageUtil;
import nl.bss.carrentapi.api.models.*;
import nl.bss.carrentapi.api.repository.CarImageRepository;
import nl.bss.carrentapi.api.repository.CarRepository;
import nl.bss.carrentapi.api.services.interfaces.CarService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarImageRepository carImageRepository;
    private final CarRepository carRepository;

    /**
     * Creates a new combustion car along with car info and fuel type. Still needs to be saved in order to persist.
     */
    @Override
    public CombustionCar createCombustionCar(String brand, String model, String color, String licensePlate, long kilometersCurrent, Double pricePerHour, Double pricePerKilometer, Double initialCost, LocalDate constructed, LocalDate apkUntil, Double lat, Double lng, CombustionFuelType combustionFuelType, User owner) {
        return new CombustionCar(brand, model, color, licensePlate, kilometersCurrent, pricePerHour, pricePerKilometer, initialCost, constructed, apkUntil, lat, lng, combustionFuelType, owner);
    }

    /**
     * Creates a new battery-electric car along with car info. Still needs to be saved in order to persist.
     */
    @Override
    public BatteryElectricCar createBatteryElectricCar(String brand, String model, String color, String licensePlate, long kilometersCurrent, Double pricePerHour, Double pricePerKilometer, Double initialCost, LocalDate constructed, LocalDate apkUntil, Double lat, Double lng, User owner) {
        return new BatteryElectricCar(brand, model, color, licensePlate, kilometersCurrent, pricePerHour, pricePerKilometer, initialCost, constructed, apkUntil, lat, lng, owner);
    }

    /**
     * Creates a new fuel-cell car along with car info. Still needs to be saved in order to persist.
     */
    @Override
    public FuelCellCar createFuelCellCar(String brand, String model, String color, String licensePlate, long kilometersCurrent, Double pricePerHour, Double pricePerKilometer, Double initialCost, LocalDate constructed, LocalDate apkUntil, Double lat, Double lng, User owner) {
        return new FuelCellCar(brand, model, color, licensePlate, kilometersCurrent, pricePerHour, pricePerKilometer, initialCost, constructed, apkUntil, lat, lng, owner);
    }

    @Override
    public CarImage createCarImage(String contentType, byte[] bytes, Car car) {
        CarImage image = new CarImage(contentType);
        image.setData(ImageUtil.compressImage(bytes));
        image.car = car;
        image = carImageRepository.save(image);
        return image;
    }

    @Override
    public Car findCar(long id) {
        return carRepository.findById(id).orElseThrow(() -> new NotAllowedException("That car was not found."));
    }
}
