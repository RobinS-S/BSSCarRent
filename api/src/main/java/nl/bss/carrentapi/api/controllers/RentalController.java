package nl.bss.carrentapi.api.controllers;

import lombok.AllArgsConstructor;
import nl.bss.carrentapi.api.dto.InvoiceDto;
import nl.bss.carrentapi.api.dto.RentalDto;
import nl.bss.carrentapi.api.dto.rental.RentalCreateDto;
import nl.bss.carrentapi.api.dto.rental.RentalDeliverDto;
import nl.bss.carrentapi.api.dto.rental.RentalPeriodDto;
import nl.bss.carrentapi.api.exceptions.NotAllowedException;
import nl.bss.carrentapi.api.mappers.DtoMapper;
import nl.bss.carrentapi.api.models.Car;
import nl.bss.carrentapi.api.models.Invoice;
import nl.bss.carrentapi.api.models.Rental;
import nl.bss.carrentapi.api.models.User;
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
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rentals")
@Validated
@AllArgsConstructor
public class RentalController {
    private final DtoMapper dtoMapper;
    private final RentalRepository rentalRepository;
    private final CarRepository carRepository;
    private final RentalService rentalService;
    private final AuthService authService;
    private final InvoiceService invoiceService;
    private final InvoiceRepository invoiceRepository;

    @GetMapping("/car/{id}")
    public ResponseEntity<List<RentalDto>> getRentalsForCar(@RequestHeader(name = "Authorization", required = false) String authHeader, @PathVariable Long id) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);

        List<Rental> rentals = rentalRepository.findRentalsByCarId(id);
        return ResponseEntity.ok(rentals.stream()
                .filter(c -> c.getTenant() == user || c.getCarOwner() == user) // Only show rentals the user is related to
                .map(dtoMapper::convertToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/car/{id}/periods")
    public ResponseEntity<List<RentalPeriodDto>> getRentalPeriodsForCar(@PathVariable Long id) {
        List<Rental> rentals = rentalRepository.findRentalsByCarId(id);
        return ResponseEntity.ok(rentals.stream()
                .map(dtoMapper::convertToRentalPeriodDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/owned")
    public ResponseEntity<List<RentalDto>> getRentalsForCarTenant(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);

        List<Rental> rentals = rentalRepository.findRentalsByCarOwnerId(user.getId());
        return ResponseEntity.ok(rentals.stream()
                .map(dtoMapper::convertToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<RentalDto>> getRentalsForCarOwner(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);

        List<Rental> rentals = rentalRepository.findRentalsByTenantId(user.getId());
        return ResponseEntity.ok(rentals.stream()
                .map(dtoMapper::convertToDto)
                .collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<RentalDto> createRental(@RequestHeader(name = "Authorization", required = false) String authHeader, @RequestBody RentalCreateDto rentalCreateDto) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);

        Optional<Car> foundCar = carRepository.findById(rentalCreateDto.getCarId());
        if (foundCar.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Car car = foundCar.get();

        Optional<Rental> existingRentalsForUser = rentalRepository.findRentalByTenantIdAndDeliveredAtIsNullAndIsCancelledFalse(user.getId());
        if (existingRentalsForUser.isPresent()) {
            throw new NotAllowedException("You already have a rental that you need to cancel first.");
        }

        if (rentalCreateDto.getReservedFrom().isBefore(LocalDateTime.now()) || rentalCreateDto.getReservedUntil().isBefore(LocalDateTime.now())) {
            throw new NotAllowedException("You can't create a Rental in the past!");
        }

        Optional<Rental> blockedByRental = rentalRepository.findBlockingRentalBetween(car.getId(), rentalCreateDto.getReservedFrom(), rentalCreateDto.getReservedUntil());
        if (!blockedByRental.isEmpty()) {
            throw new NotAllowedException("This car has already been booked between these times.");
        }

        Rental rental = rentalService.createRental(rentalCreateDto.getReservedFrom(), rentalCreateDto.getReservedUntil(), rentalCreateDto.getKmPackage(), user, car);
        rental = rentalRepository.save(rental);

        return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.convertToDto(rental));

    }

    @PostMapping("{id}/markPickedUp")
    public ResponseEntity<RentalDto> pickupCar(@RequestHeader(name = "Authorization", required = false) String authHeader, @PathVariable Long id) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);

        Optional<Rental> foundRental = rentalRepository.findById(id);
        if (foundRental.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Rental rental = foundRental.get();
        if (!LocalDateTime.now().isAfter(rental.getReservedFrom()))
        {
            throw new NotAllowedException("The rental start time hasn't started yet.");
        }

        if(rental.getTenant() != user) {
            throw new NotAllowedException("This is not your rental.");
        }

        if(rental.getPickedUpAt() != null) {
            throw new NotAllowedException("This rental has already been picked up.");
        }

        if(rental.getDeliveredAt() != null) {
            throw new NotAllowedException("This rental has already been delivered.");
        }

        Optional<Rental> broughtBackLateRental = rentalRepository.findRentalByCarIdAndPickedUpAtNotNullAndDeliveredAtIsNullAndIsCancelledFalse(rental.getCar().getId());
        if(broughtBackLateRental.isPresent()) {
            throw new NotAllowedException("Sorry, the previous renter has not brought the car yet back, so you cannot pick it up yet. Please wait and have a coffee.");
        }

        rental.setPickedUpAt(LocalDateTime.now());
        rental = rentalRepository.save(rental);

        return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.convertToDto(rental));
    }

    @PostMapping("{id}/markDelivered")
    public ResponseEntity<InvoiceDto> deliverCar(@RequestHeader(name = "Authorization", required = false) String authHeader, @PathVariable Long id, @RequestBody RentalDeliverDto deliverDto) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);

        Optional<Rental> foundRental = rentalRepository.findById(id);
        if (foundRental.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Rental rental = foundRental.get();

        if (!LocalDateTime.now().isAfter(rental.getReservedFrom()))
        {
            throw new NotAllowedException("The rental start time hasn't started yet.");
        }

        if(rental.getTenant() != user) {
            throw new NotAllowedException("This is not your rental.");
        }

        if(rental.getPickedUpAt() == null) {
            throw new NotAllowedException("This rental has not been picked up yet.");
        }

        if(rental.getDeliveredAt() != null) {
            throw new NotAllowedException("This rental has already been delivered.");
        }

        Car car = rental.getCar();
        if(deliverDto.getMileageTotal() < car.getKilometersCurrent()) {
            throw new NotAllowedException("The kilometer count you submitted is lower than the count before you rented it.");
        }

        rental.setDeliveredAt(LocalDateTime.now());
        rental.setMileageTotal(deliverDto.getMileageTotal());
        rental.setDrivingStyleScore(deliverDto.getDrivingStyleScore());
        rental = rentalRepository.save(rental);

        long kmsDriven = rental.getMileageTotal() - car.getKilometersCurrent();
        Double mileageCost = car.calculateCostForKms(kmsDriven);

        long overKms = kmsDriven - rental.getKmPackage();
        Double overKmPackageCosts = 0.0;
        if(overKms > 0) {
            overKmPackageCosts = 2 * car.calculateCostForKms(overKms);
        }

        car.setKilometersCurrent(rental.getMileageTotal());
        car.setLat(deliverDto.getLat());
        car.setLng(deliverDto.getLng());

        carRepository.save(car);

        Double hoursUsed = Double.valueOf(ChronoUnit.SECONDS.between(rental.getReservedFrom(), rental.getDeliveredAt())) / 3600;
        Double totalHourCost = hoursUsed * car.getPricePerHour();

        Double totalCosts = car.getInitialCost() + totalHourCost + mileageCost + overKmPackageCosts;

        Invoice invoice = invoiceService.createInvoice(kmsDriven, car.getInitialCost(), mileageCost, rental.getKmPackage(), overKmPackageCosts, totalHourCost, hoursUsed, totalCosts, false, rental.getTenant(), rental.getCarOwner(), rental);
        invoice = invoiceRepository.save(invoice);

        return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.convertToDto(invoice));
    }

    @GetMapping("/current")
    public ResponseEntity<RentalDto> getActiveRentalAsTenant(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);

        Optional<Rental> foundRental = rentalRepository.findRentalByTenantIdAndPickedUpAtIsNullAndDeliveredAtIsNullAndIsCancelledFalse(user.getId());
        if (foundRental.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Rental rental = foundRental.get();

        return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.convertToDto(rental));
    }

    @DeleteMapping("/current")
    public ResponseEntity<RentalDto> cancelRental(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);

        Optional<Rental> foundRental = rentalRepository.findRentalByTenantIdAndPickedUpAtIsNullAndDeliveredAtIsNullAndIsCancelledFalse(user.getId());
        if (foundRental.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Rental rental = foundRental.get();

        if (rental.getReservedFrom().isAfter(LocalDateTime.now())) {
            throw new NotAllowedException("This rental has already started, so you must pick it up and deliver it back.");
        }

        rentalRepository.delete(rental);

        return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.convertToDto(rental));
    }
}
