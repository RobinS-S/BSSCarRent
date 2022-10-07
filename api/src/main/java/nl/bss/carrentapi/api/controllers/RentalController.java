package nl.bss.carrentapi.api.controllers;

import nl.bss.carrentapi.api.mappers.DtoMapper;
import nl.bss.carrentapi.api.models.dto.RentalDto;
import nl.bss.carrentapi.api.models.dto.car.CarDto;
import nl.bss.carrentapi.api.models.dto.rental.RentalCreateDto;
import nl.bss.carrentapi.api.models.entities.Car;
import nl.bss.carrentapi.api.models.entities.Rental;
import nl.bss.carrentapi.api.models.entities.User;
import nl.bss.carrentapi.api.repository.CarRepository;
import nl.bss.carrentapi.api.repository.RentalRepository;
import nl.bss.carrentapi.api.services.interfaces.AuthService;
import nl.bss.carrentapi.api.services.interfaces.RentalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import javax.validation.Path;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class RentalController {

    private final DtoMapper dtoMapper;

    private final RentalRepository rentalRepository;

    private final CarRepository carRepository;

    private final RentalService rentalService;

    private final AuthService authService;

    public RentalController(DtoMapper dtoMapper, RentalRepository rentalRepository, CarRepository carRepository, RentalService rentalService, AuthService authService) {
        this.dtoMapper = dtoMapper;
        this.rentalRepository = rentalRepository;
        this.carRepository = carRepository;
        this.rentalService = rentalService;
        this.authService = authService;
    }

    @GetMapping()
    public ResponseEntity<List<RentalDto>> indexAll() {
        List<Rental> rentals = rentalRepository.findAll();

        return ResponseEntity.ok(rentals.stream()
                .map(dtoMapper::convertToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<RentalDto>> findRentals(@RequestHeader("Authorization") String authHeader) {
        Optional<User> foundUser = authService.getCurrentUserByAuthHeader(authHeader);
        if(foundUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        User user = foundUser.get();

        Set<Rental> rentals = user.getRentals();
        return ResponseEntity.ok(rentals.stream()
                .map(dtoMapper::convertToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}/rentals")
    public ResponseEntity<List<RentalDto>> getRentalsForCar(@PathVariable Long id) {
        List<Rental> rentals = rentalRepository.findRentalsByCarIdAndDeliveredAtIsNull(id);
        return ResponseEntity.ok(rentals.stream()
                .map(dtoMapper::convertToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}/rentals")
    public ResponseEntity<List<RentalDto>> getRentalsForCarOwner(@PathVariable Long id) {
        List<Rental> rentals = rentalRepository.findRentalsByCarOwnerIdAndDeliveredAtIsNull(id);
        return ResponseEntity.ok(rentals.stream()
                .map(dtoMapper::convertToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}/rentals")
    public ResponseEntity<List<RentalDto>> getRentalsForCarTenant(@PathVariable Long id) {
        List<Rental> rentals = rentalRepository.findRentalsByTenantIdAndDeliveredAtIsNull(id);
        return ResponseEntity.ok(rentals.stream()
                .map(dtoMapper::convertToDto)
                .collect(Collectors.toList()));
    }

    @PostMapping("/{id}")
    public ResponseEntity<RentalDto> createRental(@RequestHeader("Authorization") String authHeader, @PathVariable Long carId, @Valid @RequestBody CarDto carDto, @RequestBody RentalCreateDto rentalCreateDto) {
        Optional<User> foundUser = authService.getCurrentUserByAuthHeader(authHeader);
        if(foundUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        User user = foundUser.get();

        Optional<Car> foundCar = carRepository.findById(carId);
        if(foundCar.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Car car = foundCar.get();

        if(!rentalRepository.findRentalsByCarIdAndDeliveredAtIsNull(carId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(null);
        }

        Rental rental = rentalService.createRental(rentalCreateDto.getReservedFrom(), rentalCreateDto.getReservedUntil(), rentalCreateDto.getKmPackage(), user, car);
        rental = rentalRepository.save(rental);

        return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.convertToDto(rental));

    }
}
