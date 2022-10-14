package nl.bss.carrentapi.api.controllers;

import nl.bss.carrentapi.api.mappers.DtoMapper;
import nl.bss.carrentapi.api.models.dto.InvoiceDto;
import nl.bss.carrentapi.api.models.dto.RentalDto;
import nl.bss.carrentapi.api.models.dto.rental.RentalCreateDto;
import nl.bss.carrentapi.api.models.dto.rental.RentalDeliverDto;
import nl.bss.carrentapi.api.models.dto.rental.RentalPeriodDto;
import nl.bss.carrentapi.api.models.entities.Car;
import nl.bss.carrentapi.api.models.entities.Invoice;
import nl.bss.carrentapi.api.models.entities.Rental;
import nl.bss.carrentapi.api.models.entities.User;
import nl.bss.carrentapi.api.repository.CarRepository;
import nl.bss.carrentapi.api.repository.InvoiceRepository;
import nl.bss.carrentapi.api.repository.RentalRepository;
import nl.bss.carrentapi.api.services.interfaces.AuthService;
import nl.bss.carrentapi.api.services.interfaces.InvoiceService;
import nl.bss.carrentapi.api.services.interfaces.RentalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

    private final InvoiceService invoiceService;

    private final InvoiceRepository invoiceRepository;

    public RentalController(DtoMapper dtoMapper, RentalRepository rentalRepository, CarRepository carRepository, RentalService rentalService, AuthService authService, InvoiceService invoiceService, InvoiceRepository invoiceRepository) {
        this.dtoMapper = dtoMapper;
        this.rentalRepository = rentalRepository;
        this.carRepository = carRepository;
        this.rentalService = rentalService;
        this.authService = authService;
        this.invoiceService = invoiceService;
        this.invoiceRepository = invoiceRepository;
    }

    @GetMapping("/car/{id}")
    public ResponseEntity<List<RentalDto>> getRentalsForCar(@RequestHeader("Authorization") String authHeader, @PathVariable Long id) {
        Optional<User> foundUser = authService.getCurrentUserByAuthHeader(authHeader);
        if(foundUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        User user = foundUser.get();

        List<Rental> rentals = rentalRepository.findRentalsByCarId(id);
        return ResponseEntity.ok(rentals.stream()
                .filter(c -> c.getTenant() == user || c.getCarOwner() == user) // Only show rentals the user is related to
                .map(dtoMapper::convertToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/car/{id}/periods")
    public ResponseEntity<List<RentalPeriodDto>> getRentalPeriodsForCar(@RequestHeader("Authorization") String authHeader, @PathVariable Long id) {
        Optional<User> foundUser = authService.getCurrentUserByAuthHeader(authHeader);
        if(foundUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        User user = foundUser.get();

        List<Rental> rentals = rentalRepository.findRentalsByCarId(id);
        return ResponseEntity.ok(rentals.stream()
                .map(dtoMapper::convertToRentalPeriodDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/owned")
    public ResponseEntity<List<RentalDto>> getRentalsForCarTenant(@RequestHeader("Authorization") String authHeader) {
        Optional<User> foundUser = authService.getCurrentUserByAuthHeader(authHeader);
        if(foundUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        User user = foundUser.get();

        List<Rental> rentals = rentalRepository.findRentalsByCarOwnerId(user.getId());
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

        List<Rental> rentals = rentalRepository.findRentalsByTenantIdAndDeliveredAtIsNullAndIsCancelledFalse(user.getId());
        return ResponseEntity.ok(rentals.stream()
                .map(dtoMapper::convertToDto)
                .collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<RentalDto> createRental(@RequestHeader("Authorization") String authHeader, @RequestBody RentalCreateDto rentalCreateDto) {
        Optional<User> foundUser = authService.getCurrentUserByAuthHeader(authHeader);
        if(foundUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        User user = foundUser.get();

        Optional<Car> foundCar = carRepository.findById(rentalCreateDto.getCarId());
        if(foundCar.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Car car = foundCar.get();

        List<Rental> existingRentalsForUser = rentalRepository.findRentalsByCarIdAndDeliveredAtIsNullAndIsCancelledFalse(car.getId());
        if(!existingRentalsForUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        if(!rentalRepository.findRentalsByCarIdBetween(car.getId(), rentalCreateDto.getReservedFrom(), rentalCreateDto.getReservedUntil()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        Rental rental = rentalService.createRental(rentalCreateDto.getReservedFrom(), rentalCreateDto.getReservedUntil(), rentalCreateDto.getKmPackage(), user, car);
        rental = rentalRepository.save(rental);

        return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.convertToDto(rental));

    }

    @PostMapping("{id}/markPickedUp")
    public ResponseEntity<RentalDto> pickupCar(@RequestHeader("Authorization") String authHeader, @PathVariable Long id) {
        Optional<User> foundUser = authService.getCurrentUserByAuthHeader(authHeader);
        if(foundUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        User user = foundUser.get();

        Optional<Rental> foundRental = rentalRepository.findById(id);
        if(foundRental.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Rental rental = foundRental.get();
        if(!LocalDateTime.now().isAfter(rental.getReservedFrom())
                || rental.getTenant() != user
                || rental.getPickedUpAt() != null
                || rental.getDeliveredAt() != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        rental.setPickedUpAt(LocalDateTime.now());
        rental = rentalRepository.save(rental);

        return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.convertToDto(rental));
    }

    @PostMapping("{id}/markDelivered")
    public ResponseEntity<InvoiceDto> deliverCar(@RequestHeader("Authorization") String authHeader, @PathVariable Long id, @RequestBody RentalDeliverDto deliverDto) {
        Optional<User> foundUser = authService.getCurrentUserByAuthHeader(authHeader);
        if(foundUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        User user = foundUser.get();

        Optional<Rental> foundRental = rentalRepository.findById(id);
        if(foundRental.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Rental rental = foundRental.get();
        if(!LocalDateTime.now().isAfter(rental.getReservedFrom())
                || rental.getTenant() != user
                || rental.getPickedUpAt() == null
                || rental.getDeliveredAt() != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        rental.setDeliveredAt(LocalDateTime.now());
        rental.setMileageTotal(deliverDto.getMileageTotal());
        rental.setDrivingStyleScore(deliverDto.getDrivingStyleScore());
        rental = rentalRepository.save(rental);

        Car car = rental.getCar();

        long kmsDriven = rental.getMileageTotal() - car.getKilometersCurrent();
        Double kmsPrice = car.calculateCostForKms(kmsDriven);
        //TODO: calculate over km costs
        Invoice invoice = invoiceService.createInvoice(kmsDriven, kmsPrice, rental.getKmPackage(), 0.0, kmsPrice, false, rental.getTenant(), rental.getCarOwner(), rental);
        invoice = invoiceRepository.save(invoice);
        
        return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.convertToDto(invoice));
    }

    @DeleteMapping("/current")
    public ResponseEntity<RentalDto> cancelRental(@RequestHeader("Authorization") String authHeader) {
        Optional<User> foundUser = authService.getCurrentUserByAuthHeader(authHeader);
        if(foundUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Optional<Rental> foundRental = rentalRepository.findRentalByTenantIdAndPickedUpAtIsNullAndDeliveredAtIsNullAndIsCancelledFalse(foundUser.get().getId());
        if(foundRental.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Rental rental = foundRental.get();

        if(rental.getReservedFrom().isAfter(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        rentalRepository.delete(rental);

        return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.convertToDto(rental));
    }

    @GetMapping("/current")
    public ResponseEntity<RentalDto> getActiveRentalAsTenant(@RequestHeader("Authorization") String authHeader) {
        Optional<User> foundUser = authService.getCurrentUserByAuthHeader(authHeader);
        if(foundUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Optional<Rental> foundRental = rentalRepository.findRentalByTenantIdAndPickedUpAtIsNullAndDeliveredAtIsNullAndIsCancelledFalse(foundUser.get().getId());
        if(foundRental.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Rental rental = foundRental.get();

        return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.convertToDto(rental));
    }
}
