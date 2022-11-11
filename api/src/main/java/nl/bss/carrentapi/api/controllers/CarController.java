package nl.bss.carrentapi.api.controllers;

import lombok.AllArgsConstructor;
import nl.bss.carrentapi.api.dto.car.CarDto;
import nl.bss.carrentapi.api.dto.car.CarUpdateDto;
import nl.bss.carrentapi.api.exceptions.BadRequestException;
import nl.bss.carrentapi.api.exceptions.NotAllowedException;
import nl.bss.carrentapi.api.mappers.DtoMapper;
import nl.bss.carrentapi.api.misc.Constants;
import nl.bss.carrentapi.api.misc.ImageUtil;
import nl.bss.carrentapi.api.models.Car;
import nl.bss.carrentapi.api.models.CarImage;
import nl.bss.carrentapi.api.models.Rental;
import nl.bss.carrentapi.api.models.User;
import nl.bss.carrentapi.api.repository.CarImageRepository;
import nl.bss.carrentapi.api.repository.CarRepository;
import nl.bss.carrentapi.api.repository.RentalRepository;
import nl.bss.carrentapi.api.services.AuthService;
import nl.bss.carrentapi.api.services.CarService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cars")
@Validated
@AllArgsConstructor
public class CarController {
    private final DtoMapper dtoMapper;
    private final ModelMapper modelMapper;
    private final CarService carService;
    private final AuthService authService;

    /**
     * Gets Image IDs for Car
     */
    @GetMapping("/{id}/images")
    @ResponseBody
    public ResponseEntity<List<Long>> findImage(@PathVariable Long id) {
        carService.findCar(id);
        return carService.responseImage(id);
    }

    /**
     * Gets Image for Car
     */
    @GetMapping("/{id}/image/{imageId}")
    @ResponseBody
    public ResponseEntity<byte[]> findImage(@PathVariable Long id, @PathVariable Long imageId) {
        CarImage image = carService.carImage(imageId, id).orElseThrow(() -> new NotAllowedException("That image was not found for this car!"));
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf(image.getType())).body(ImageUtil.decompressImage(image.getData()));
    }

    /**
     * Deletes Image for Car
     */
    @DeleteMapping("/{id}/image/{imageId}")
    @ResponseBody
    public ResponseEntity removeImage(@RequestHeader(name = "Authorization", required = false) String authHeader, @PathVariable Long id, @PathVariable Long imageId) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);
        Car car = carService.findCar(id);

        if (!car.getOwner().equals(user)) {
            throw new NotAllowedException("This is not your car, so you cannot change its images.");
        }

        CarImage image = carService.carImage(imageId, id).orElseThrow(() -> new NotAllowedException("That image was not found for this car!"));
        carService.deleteImage(image);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Uploads Image for Car
     */
    @PostMapping("/{id}/images")
    public ResponseEntity<Long> uploadImage(@RequestHeader(name = "Authorization", required = false) String authHeader, @PathVariable Long id, @RequestParam("image") MultipartFile file) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);
        Car car = carService.findCar(id);

        if (!car.getOwner().equals(user)) {
            throw new NotAllowedException("This is not your car, so you cannot change its images.");
        }

        try {
            String uploadedFileType = file.getContentType();
            if (Constants.ALLOWED_MIME_TYPES.indexOf(uploadedFileType) == -1) {
                throw new NotAllowedException("Invalid content type.");
            } else {
                CarImage image = carService.createCarImage(uploadedFileType, file.getBytes(), car);
                return ResponseEntity.status(HttpStatus.CREATED).body(image.getId());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Finds Car by given ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<CarDto> getCar(@PathVariable Long id) {
        Car car = carService.findCar(id);

        return ResponseEntity.ok(dtoMapper.convertToDto(car));
    }

    @GetMapping("/{id}/tco")
    public ResponseEntity<Double> getCarTco(@PathVariable Long id) {
        Car car = carService.findCar(id);

        return ResponseEntity.ok(car.calculateAnnualCost());
    }

    /**
     * Finds all Cars that can be rented.
     */
    @GetMapping
    public ResponseEntity<List<CarDto>> getCars() {

        List<Car> cars = carService.listCars();
        return ResponseEntity.ok(cars.stream()
                .map(dtoMapper::convertToDto)
                .collect(Collectors.toList()));
    }

    /**
     * Finds Cars that are owned by current User.
     */
    @GetMapping("/mine")
    public ResponseEntity<List<CarDto>> getUserCars(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);

        Set<Car> cars = user.getOwnedCars();
        return ResponseEntity.ok(cars.stream()
                .map(dtoMapper::convertToDto)
                .collect(Collectors.toList()));
    }

    /**
     * Creates Car
     */
    @PostMapping
    public ResponseEntity<CarDto> create(@RequestHeader(name = "Authorization", required = false) String authHeader, @Valid @RequestBody CarDto carDto) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);

        Car car = null;
        switch (carDto.getCarType()) {
            case FUEL_CELL:
                car = carService.createFuelCellCar(carDto.getBrand(), carDto.getModel(), carDto.getColor(), carDto.getLicensePlate(), carDto.getKilometersCurrent(), carDto.getPricePerKilometer(), carDto.getPricePerHour(), carDto.getInitialCost(), carDto.getConstructed(), carDto.getApkUntil(), carDto.getLat(), carDto.getLng(), user);
                break;
            case COMBUSTION:
                if (carDto.getFuelType() == null) {
                    throw new BadRequestException("The fuel type was not set, but it needs to be for combustion cars.");
                }
                car = carService.createCombustionCar(carDto.getBrand(), carDto.getModel(), carDto.getColor(), carDto.getLicensePlate(), carDto.getKilometersCurrent(), carDto.getPricePerKilometer(), carDto.getPricePerHour(), carDto.getInitialCost(), carDto.getConstructed(), carDto.getApkUntil(), carDto.getLat(), carDto.getLng(), carDto.getFuelType(), user);
                break;
            case BATTERY_ELECTRIC:
                car = carService.createBatteryElectricCar(carDto.getBrand(), carDto.getModel(), carDto.getColor(), carDto.getLicensePlate(), carDto.getKilometersCurrent(), carDto.getPricePerKilometer(), carDto.getPricePerHour(), carDto.getInitialCost(), carDto.getConstructed(), carDto.getApkUntil(), carDto.getLat(), carDto.getLng(), user);
                break;
        }
        carService.saveCar(car);

        return ResponseEntity.status(HttpStatus.CREATED).body(dtoMapper.convertToDto(car));
    }

    /**
     * Updates partial Car info
     */
    @PutMapping("/{id}")
    public ResponseEntity<CarDto> update(@RequestHeader(name = "Authorization", required = false) String authHeader, @PathVariable Long id, @Valid @RequestBody CarUpdateDto updateDto) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);
        Car car = carService.findCar(id);

        if (!car.getOwner().equals(user)) {
            throw new NotAllowedException("This is not your car, so you cannot change its details.");
        }

        modelMapper.map(updateDto, car);

        return ResponseEntity.ok(dtoMapper.convertToDto(car));
    }

    /**
     * Deletes car
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<CarDto> delete(@RequestHeader(name = "Authorization", required = false) String authHeader, @PathVariable Long id) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);
        Car car = carService.findCar(id);

        if (!car.getOwner().equals(user)) {
            throw new NotAllowedException("This is not your car, so you cannot change its details.");
        }

        Optional<Rental> rental = carService.findRental(car);
        if (rental.isPresent()) {
            throw new NotAllowedException("The car is currently rented out. Please delete your car when it is brought back.");
        }

        Set<Rental> rentals = car.getRentals();
        rentals.forEach(r -> r.setCar(null));
        carService.saveAll(rentals);

        List<Long> imageIds = carService.getImageIds(car);
        if(imageIds.size() > 0) {
            carService.deleteImages(imageIds);
        }

        carService.deleteCar(car);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
