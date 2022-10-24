package nl.bss.carrentapi.api.controllers;

import lombok.AllArgsConstructor;
import nl.bss.carrentapi.api.dto.car.CarDto;
import nl.bss.carrentapi.api.dto.car.CarUpdateDto;
import nl.bss.carrentapi.api.exceptions.BadRequestException;
import nl.bss.carrentapi.api.exceptions.NotAllowedException;
import nl.bss.carrentapi.api.mappers.DtoMapper;
import nl.bss.carrentapi.api.misc.ImageUtil;
import nl.bss.carrentapi.api.models.Car;
import nl.bss.carrentapi.api.models.CarImage;
import nl.bss.carrentapi.api.models.Rental;
import nl.bss.carrentapi.api.models.User;
import nl.bss.carrentapi.api.repository.CarImageRepository;
import nl.bss.carrentapi.api.repository.CarRepository;
import nl.bss.carrentapi.api.repository.RentalRepository;
import nl.bss.carrentapi.api.services.interfaces.AuthService;
import nl.bss.carrentapi.api.services.interfaces.CarService;
import nl.bss.carrentapi.api.misc.Constants;
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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Arrays;

@RestController
@RequestMapping("/api/cars")
@Validated
@AllArgsConstructor
public class CarController {
    private final DtoMapper dtoMapper;
    private final ModelMapper modelMapper;
    private final RentalRepository rentalRepository;
    private final CarRepository carRepository;
    private final CarService carService;
    private final AuthService authService;
    private final CarImageRepository carImageRepository;

    @GetMapping("/{id}/images")
    @ResponseBody
    public ResponseEntity<List<Long>> findImage(@PathVariable Long id) {
        carService.findCar(id);
        return ResponseEntity.ok(carImageRepository.findIDsByCarId(id));
    }

    @GetMapping("/{id}/image/{imageId}")
    @ResponseBody
    public ResponseEntity<byte[]> findImage(@PathVariable Long id, @PathVariable Long imageId) {
        CarImage image = carImageRepository.findByIdAndCarId(imageId, id).orElseThrow(() -> new NotAllowedException("That image was not found for this car!"));
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf(image.getType())).body(ImageUtil.decompressImage(image.getData()));
    }

    @DeleteMapping("/{id}/image/{imageId}")
    @ResponseBody
    public ResponseEntity removeImage(@RequestHeader(name = "Authorization", required = false) String authHeader, @PathVariable Long id, @PathVariable Long imageId) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);
        Car car = carService.findCar(id);

        if (!car.getOwner().equals(user)) {
            throw new NotAllowedException("This is not your car, so you cannot change its images.");
        }

        CarImage image = carImageRepository.findByIdAndCarId(imageId, id).orElseThrow(() -> new NotAllowedException("That image was not found for this car!"));
        carImageRepository.delete(image);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

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
            }
            else {
                CarImage image = carService.createCarImage(uploadedFileType, file.getBytes(), car);
                return ResponseEntity.status(HttpStatus.CREATED).body(image.getId());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarDto> getCar(@PathVariable Long id) {
        Car car = carService.findCar(id);

        return ResponseEntity.ok(dtoMapper.convertToDto(car));
    }

    @GetMapping
    public ResponseEntity<List<CarDto>> getCars() {
        List<Car> cars = carRepository.findAll();
        return ResponseEntity.ok(cars.stream()
                .map(dtoMapper::convertToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<CarDto>> getUserCars(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);

        Set<Car> cars = user.getOwnedCars();
        return ResponseEntity.ok(cars.stream()
                .map(dtoMapper::convertToDto)
                .collect(Collectors.toList()));
    }

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
        car = carRepository.save(car);

        return ResponseEntity.status(HttpStatus.CREATED).body(dtoMapper.convertToDto(car));
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<CarDto> delete(@RequestHeader(name = "Authorization", required = false) String authHeader, @PathVariable Long id) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);
        Car car = carService.findCar(id);

        if (!car.getOwner().equals(user)) {
            throw new NotAllowedException("This is not your car, so you cannot change its details.");
        }

        Set<Rental> rentals = car.getRentals();
        rentals.forEach(r -> r.setCar(null));
        rentalRepository.saveAll(rentals);
        carRepository.delete(car);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
