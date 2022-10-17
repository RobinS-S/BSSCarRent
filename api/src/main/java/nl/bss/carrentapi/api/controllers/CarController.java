package nl.bss.carrentapi.api.controllers;

import lombok.AllArgsConstructor;
import nl.bss.carrentapi.api.dto.car.CarDto;
import nl.bss.carrentapi.api.dto.car.CarUpdateDto;
import nl.bss.carrentapi.api.exceptions.NotAllowedException;
import nl.bss.carrentapi.api.mappers.DtoMapper;
import nl.bss.carrentapi.api.models.*;
import nl.bss.carrentapi.api.repository.CarRepository;
import nl.bss.carrentapi.api.services.interfaces.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    private final CarRepository carRepository;
    private final AuthService authService;

    @GetMapping("/{id}")
    public ResponseEntity<CarDto> getCar(@PathVariable Long id) {
        Optional<Car> car = carRepository.findById(id);
        if (car.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(dtoMapper.convertToDto(car.get()));
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
                car = new FuelCellCar(carDto.getBrand(), carDto.getModel(), carDto.getColor(), carDto.getLicensePlate(), carDto.getKilometersCurrent(), carDto.getPricePerKilometer(), carDto.getPricePerHour(), carDto.getInitialCost(), carDto.getConstructed(), carDto.getApkUntil(), user);
                break;
            case COMBUSTION:
                if (carDto.getFuelType() == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }
                car = new CombustionCar(carDto.getBrand(), carDto.getModel(), carDto.getColor(), carDto.getLicensePlate(), carDto.getKilometersCurrent(), carDto.getPricePerKilometer(), carDto.getPricePerHour(), carDto.getInitialCost(), carDto.getConstructed(), carDto.getApkUntil(), carDto.getFuelType(), user);
                break;
            case BATTERY_ELECTRIC:
                car = new BatteryElectricCar(carDto.getBrand(), carDto.getModel(), carDto.getColor(), carDto.getLicensePlate(), carDto.getKilometersCurrent(), carDto.getPricePerKilometer(), carDto.getPricePerHour(), carDto.getInitialCost(), carDto.getConstructed(), carDto.getApkUntil(), user);
                break;
        }
        car = carRepository.save(car);

        return ResponseEntity.ok(dtoMapper.convertToDto(car));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarDto> update(@RequestHeader(name = "Authorization", required = false) String authHeader, @PathVariable Long id, @Valid @RequestBody CarUpdateDto updateDto) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);

        Optional<Car> foundCar = carRepository.findById(id);
        if (foundCar.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Car car = foundCar.get();

        if (!car.getOwner().equals(user)) {
            throw new NotAllowedException("This is not your car, so you cannot change its details.");
        }

        modelMapper.map(updateDto, car);

        return ResponseEntity.ok(dtoMapper.convertToDto(car));
    }
}
