package nl.bss.carrentapi.api.controllers;

import nl.bss.carrentapi.api.mappers.DtoMapper;
import nl.bss.carrentapi.api.models.dto.RentalDto;
import nl.bss.carrentapi.api.models.dto.car.CarDto;
import nl.bss.carrentapi.api.models.dto.rental.RentalCreateDto;
import nl.bss.carrentapi.api.models.entities.Car;
import nl.bss.carrentapi.api.models.entities.Invoice;
import nl.bss.carrentapi.api.models.entities.Rental;
import nl.bss.carrentapi.api.models.entities.User;
import nl.bss.carrentapi.api.repository.CarRepository;
import nl.bss.carrentapi.api.repository.RentalRepository;
import nl.bss.carrentapi.api.services.interfaces.AuthService;
import nl.bss.carrentapi.api.services.interfaces.RentalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import javax.validation.Path;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rentals")
@Validated
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

    @GetMapping("/car/{id}")
    public ResponseEntity<List<RentalDto>> getRentalsForCar(@PathVariable Long id) {
        List<Rental> rentals = rentalRepository.findRentalsByCarIdAndDeliveredAtIsNull(id);
        return ResponseEntity.ok(rentals.stream()
                .map(dtoMapper::convertToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/owned")
    public ResponseEntity<List<RentalDto>> getRentalsForCarTenant(@RequestHeader("Authorization") String authHeader) {
        Optional<User> foundUser = authService.getCurrentUserByAuthHeader(authHeader);
        if(foundUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        User user = foundUser.get();

        List<Rental> rentals = rentalRepository.findRentalsByTenantIdAndDeliveredAtIsNull(user.getId());
        return ResponseEntity.ok(rentals.stream()
                .map(dtoMapper::convertToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<RentalDto>> getRentalsForCarOwner(@RequestHeader("Authorization") String authHeader) {
        Optional<User> foundUser = authService.getCurrentUserByAuthHeader(authHeader);
        if(foundUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        User user = foundUser.get();

        List<Rental> rentals = rentalRepository.findRentalsByCarOwnerIdAndDeliveredAtIsNull(user.getId());
        return ResponseEntity.ok(rentals.stream()
                .map(dtoMapper::convertToDto)
                .collect(Collectors.toList()));
    }

    @PostMapping()
    public ResponseEntity<RentalDto> createRental(@RequestHeader("Authorization") String authHeader, @PathVariable Long carId, @RequestBody RentalCreateDto rentalCreateDto) {
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

        if(!rentalRepository.findRentalsByCarIdAndDeliveredAtIsNull(car.getId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        Rental rental = rentalService.createRental(rentalCreateDto.getReservedFrom(), rentalCreateDto.getReservedUntil(), rentalCreateDto.getKmPackage(), user, car);
        rental = rentalRepository.save(rental);

        return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.convertToDto(rental));

    }

    @PostMapping("/pickup")
    public ResponseEntity<RentalDto> pickupCar(@RequestHeader("Authorization") String authHeader, @PathVariable Long id) {
        Optional<User> foundUser = authService.getCurrentUserByAuthHeader(authHeader);
        if(foundUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Optional<Rental> foundRental = rentalRepository.findById(id);
        if(foundRental.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Rental rental = foundRental.get();
        if(!rental.getReservedFrom().isAfter(LocalDateTime.now())
                || rental.getReservedFrom().isEqual(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        rental.setDeliveredAt(LocalDateTime.now());
        rentalRepository.save(rental);
        
        return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.convertToDto(rental));

    }

    @DeleteMapping("/current")
    public ResponseEntity<RentalDto> cancelRental(@RequestHeader("Authorization") String authHeader, @PathVariable Long id) {
        Optional<User> foundUser = authService.getCurrentUserByAuthHeader(authHeader);
        if(foundUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Optional<Rental> foundRental = rentalRepository.findById(id);
        if(foundRental.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Rental rental = foundRental.get();

        if(!rental.getReservedFrom().isAfter(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        rentalRepository.delete(rental);

        return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.convertToDto(rental));
    }
}
