package nl.bss.carrentapi.api.services;

import lombok.AllArgsConstructor;
import nl.bss.carrentapi.api.exceptions.NotAllowedException;
import nl.bss.carrentapi.api.exceptions.NotFoundException;
import nl.bss.carrentapi.api.models.Car;
import nl.bss.carrentapi.api.models.Invoice;
import nl.bss.carrentapi.api.models.Rental;
import nl.bss.carrentapi.api.models.User;
import nl.bss.carrentapi.api.repository.CarRepository;
import nl.bss.carrentapi.api.repository.InvoiceRepository;
import nl.bss.carrentapi.api.repository.RentalRepository;
import nl.bss.carrentapi.api.services.interfaces.CarService;
import nl.bss.carrentapi.api.services.interfaces.InvoiceService;
import nl.bss.carrentapi.api.services.interfaces.RentalService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceService invoiceService;
    private final CarService carService;
    private final CarRepository carRepository;

    /**
     * Creates new rental with given information.
     */
    @Override
    public Rental createRental(User user, long carId, LocalDateTime reservedFrom, LocalDateTime reservedUntil, long kmPackage) {
        Car car = carService.findCar(carId);
        Optional<Rental> existingRentalsForUser = rentalRepository.findRentalByTenantIdAndDeliveredAtIsNullAndIsCancelledFalse(user.getId());

        if (existingRentalsForUser.isPresent()) {
            throw new NotAllowedException("You already have a rental that you need to cancel first.");
        }

        if (reservedFrom.isBefore(LocalDateTime.now()) || reservedUntil.isBefore(LocalDateTime.now())) {
            throw new NotAllowedException("You can't create a Rental in the past!");
        }

        Optional<Rental> blockedByRental = rentalRepository.findBlockingRentalBetween(car.getId(), reservedFrom, reservedUntil);
        if (!blockedByRental.isEmpty()) {
            throw new NotAllowedException("This car has already been booked between these times.");
        }

        Rental rental = new Rental(reservedFrom, reservedUntil, kmPackage, user, car.getOwner(), car);
        return rentalRepository.save(rental);
    }

    @Override
    public Rental findRental(long rentalId) {
        return rentalRepository.findById(rentalId).orElseThrow(() -> new NotFoundException("That rental was not found."));
    }

    /**
     * Marks car as picked up by tenant
     */
    @Override
    public Rental pickupCar(long rentalId, User user) {
        Rental rental = findRental(rentalId);

        if (!LocalDateTime.now().isAfter(rental.getReservedFrom())) {
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
        return rentalRepository.save(rental);
    }

    @Override
    public Rental cancelRental(User user) {
        Rental rental = rentalRepository.findRentalByTenantIdAndPickedUpAtIsNullAndDeliveredAtIsNullAndIsCancelledFalse(user.getId()).orElseThrow(() -> new NotFoundException("You don't have an active rental as tenant."));

        if (rental.getReservedFrom().isAfter(LocalDateTime.now())) {
            throw new NotAllowedException("This rental has already started, so you must pick it up and deliver it back.");
        }

        if (rental.isCancelled()) {
            throw new NotAllowedException("This rental has already been cancelled.");
        }

        rental.setCancelled(true);
        return rentalRepository.save(rental);
    }

    @Override
    public Invoice deliverCar(long rentalId, User user, long mileageTotal, Double drivingScore, Double lat, Double lng) {
        Rental rental = findRental(rentalId);

        if (!LocalDateTime.now().isAfter(rental.getReservedFrom())) {
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
        if(mileageTotal < car.getKilometersCurrent()) {
            throw new NotAllowedException("The kilometer count you submitted is lower than the count before you rented it.");
        }

        rental.setDeliveredAt(LocalDateTime.now());
        rental.setMileageTotal(mileageTotal);
        rental.setDrivingStyleScore(drivingScore);
        rental = rentalRepository.save(rental);

        long kmsDriven = rental.getMileageTotal() - car.getKilometersCurrent();
        Double mileageCost;
        if (kmsDriven <= rental.getKmPackage()){
            mileageCost = 0.0;
        } else {
            mileageCost = car.calculateCostForKms(kmsDriven - rental.getKmPackage());
        }

        car.setKilometersCurrent(rental.getMileageTotal());
        car.setLat(lat);
        car.setLng(lng);

        carRepository.save(car);

        Double hoursUsed = Double.valueOf(ChronoUnit.SECONDS.between(rental.getReservedFrom(), rental.getDeliveredAt())) / 3600;
        Double totalHourCost = hoursUsed * car.getPricePerHour();

        Double totalCosts = car.getInitialCost() + totalHourCost + mileageCost * drivingScore;
        
        Invoice invoice = invoiceService.createInvoice(kmsDriven, car.getInitialCost(), mileageCost, rental.getKmPackage(), totalHourCost, hoursUsed, totalCosts, false, rental.getTenant(), rental.getCarOwner(), rental);
        return invoiceRepository.save(invoice);
    }
}
