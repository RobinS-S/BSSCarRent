package nl.bss.carrentapi.api.services;

import lombok.AllArgsConstructor;
import nl.bss.carrentapi.api.enums.CombustionFuelType;
import nl.bss.carrentapi.api.exceptions.NotAllowedException;
import nl.bss.carrentapi.api.exceptions.NotFoundException;
import nl.bss.carrentapi.api.misc.ImageUtil;
import nl.bss.carrentapi.api.models.*;
import nl.bss.carrentapi.api.repository.CarImageRepository;
import nl.bss.carrentapi.api.repository.CarRepository;
import nl.bss.carrentapi.api.repository.RentalRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class CarService {
    private final CarImageRepository carImageRepository;
    private final CarRepository carRepository;
    private final RentalRepository rentalRepository;

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

    public Optional<Rental> findActiveRentalForCarId(Car car) {
        return rentalRepository.findRentalByCarIdAndPickedUpAtNotNullAndDeliveredAtIsNullAndIsCancelledFalse(car.getId());
    }

    public void saveAll(Set<Rental> rentals) {
        rentalRepository.saveAll(rentals);
    }

    public List<Car> listCars() {
        return carRepository.findAll();
    }

    public void saveCar(Car car) {
        carRepository.save(car);
    }

    public void deleteCar(Car car) {
        carRepository.delete(car);
    }

    public List responseImage(Long id) {
        return carImageRepository.findIDsByCarId(id);
    }

    public Optional<CarImage> findCarImage(long imageId, long id) {
        return carImageRepository.findByIdAndCarId(imageId, id);
    }

    public void deleteImage(CarImage image) {
        carImageRepository.delete(image);
    }

    public void addImageIdToCar(Car car, long imageId) {
        var imageIds = car.getImageIds();
        imageIds.add(imageId);
        car.setImageIds(imageIds);
        carRepository.save(car);
    }

    public void removeImageIdFromCar(Car car, long imageId) {
        var imageIds = car.getImageIds();
        imageIds.remove(imageId);
        car.setImageIds(imageIds);
        carRepository.save(car);
    }

    public List<Long> getImageIds(Car car) {
        return carImageRepository.findIDsByCarId(car.getId());
    }

    public void deleteImages(List<Long> ids) {
        carImageRepository.deleteByCarIdAndIdIn(ids);
    }
}
