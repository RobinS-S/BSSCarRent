package nl.bss.carrentapi.api.controllers;

import lombok.AllArgsConstructor;
import nl.bss.carrentapi.api.dto.InvoiceDto;
import nl.bss.carrentapi.api.dto.RentalDto;
import nl.bss.carrentapi.api.dto.rental.RentalCreateDto;
import nl.bss.carrentapi.api.dto.rental.RentalDeliverDto;
import nl.bss.carrentapi.api.dto.rental.RentalPeriodDto;
import nl.bss.carrentapi.api.exceptions.NotFoundException;
import nl.bss.carrentapi.api.mappers.DtoMapper;
import nl.bss.carrentapi.api.models.Invoice;
import nl.bss.carrentapi.api.models.Rental;
import nl.bss.carrentapi.api.models.User;
import nl.bss.carrentapi.api.repository.RentalRepository;
import nl.bss.carrentapi.api.services.AuthService;
import nl.bss.carrentapi.api.services.RentalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rentals")
@Validated
@AllArgsConstructor
public class RentalController {
    private final DtoMapper dtoMapper;
    private final RentalRepository rentalRepository;
    private final RentalService rentalService;
    private final AuthService authService;

    /**
     * Shows Rentals for a given carID, but only those related to the User
     */
    @GetMapping("/car/{id}")
    public ResponseEntity<List<RentalDto>> getRentalsForCar(@RequestHeader(name = "Authorization", required = false) String authHeader, @PathVariable Long id) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);

        List<Rental> rentals = rentalRepository.findRentalsByCarId(id);
        return ResponseEntity.ok(rentals.stream()
                .filter(c -> c.getTenant() == user || c.getCarOwner() == user) // Only show rentals the user is related to
                .map(dtoMapper::convertToDto)
                .collect(Collectors.toList()));
    }

    /**
     * Gets rental periods that have been reserved
     */
    @GetMapping("/car/{id}/periods")
    public ResponseEntity<List<RentalPeriodDto>> getRentalPeriodsForCar(@PathVariable Long id) {
        List<Rental> rentals = rentalRepository.findRentalsByCarId(id);
        return ResponseEntity.ok(rentals.stream()
                .map(dtoMapper::convertToRentalPeriodDto)
                .collect(Collectors.toList()));
    }

    /**
     * Gets rentals where the cars are rented by the logged-in User
     */
    @GetMapping("/owned")
    public ResponseEntity<List<RentalDto>> getRentalsForCarTenant(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);

        List<Rental> rentals = rentalRepository.findRentalsByCarOwnerId(user.getId());
        return ResponseEntity.ok(rentals.stream()
                .map(dtoMapper::convertToDto)
                .collect(Collectors.toList()));
    }

    /**
     * Gets rentals where the cars are owned by the logged-in User
     */
    @GetMapping("/mine")
    public ResponseEntity<List<RentalDto>> getRentalsForCarOwner(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);

        List<Rental> rentals = rentalRepository.findRentalsByTenantId(user.getId());
        return ResponseEntity.ok(rentals.stream()
                .map(dtoMapper::convertToDto)
                .collect(Collectors.toList()));
    }

    /**
     * Creates Rental
     */
    @PostMapping
    public ResponseEntity<RentalDto> createRental(@RequestHeader(name = "Authorization", required = false) String authHeader, @RequestBody RentalCreateDto rentalCreateDto) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);
        Rental rental = rentalService.createRental(user, rentalCreateDto.getCarId(), rentalCreateDto.getReservedFrom(), rentalCreateDto.getReservedUntil(), rentalCreateDto.getKmPackage());

        return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.convertToDto(rental));
    }

    /**
     * Marks Rental as picked up.
     */
    @PostMapping("{id}/markPickedUp")
    public ResponseEntity<RentalDto> pickupCar(@RequestHeader(name = "Authorization", required = false) String authHeader, @PathVariable Long id) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);
        Rental rental = rentalService.pickupCar(id, user);

        return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.convertToDto(rental));
    }

    /**
     * Marks Rental as delivered, generates Invoice
     */
    @PostMapping("{id}/markDelivered")
    public ResponseEntity<InvoiceDto> deliverCar(@RequestHeader(name = "Authorization", required = false) String authHeader, @PathVariable Long id, @RequestBody RentalDeliverDto deliverDto) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);
        Invoice invoice = rentalService.deliverCar(id, user, deliverDto.getMileageTotal(), deliverDto.getDrivingStyleScore(), deliverDto.getLat(), deliverDto.getLng());

        return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.convertToDto(invoice));
    }

    /**
     * Gets active Rental as renter
     */
    @GetMapping("/current")
    public ResponseEntity<RentalDto> getActiveRentalAsTenant(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);
        Rental rental = rentalRepository.findRentalByTenantIdAndPickedUpAtIsNullAndDeliveredAtIsNullAndIsCancelledFalse(user.getId()).orElseThrow(() -> new NotFoundException("You don't have an active rental as tenant."));

        return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.convertToDto(rental));
    }

    /**
     * Cancels Rental
     */
    @DeleteMapping("/current")
    public ResponseEntity<RentalDto> cancelRental(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        User user = authService.getCurrentUserByAuthHeader(authHeader);
        Rental rental = rentalService.cancelRental(user);

        return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.convertToDto(rental));
    }
}
