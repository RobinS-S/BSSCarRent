package nl.bss.carrentapi.api.services;

import lombok.AllArgsConstructor;
import nl.bss.carrentapi.api.exceptions.NotAllowedException;
import nl.bss.carrentapi.api.exceptions.NotFoundException;
import nl.bss.carrentapi.api.misc.NumberRounding;
import nl.bss.carrentapi.api.models.Car;
import nl.bss.carrentapi.api.models.Invoice;
import nl.bss.carrentapi.api.models.Rental;
import nl.bss.carrentapi.api.models.User;
import nl.bss.carrentapi.api.repository.CarRepository;
import nl.bss.carrentapi.api.repository.InvoiceRepository;
import nl.bss.carrentapi.api.repository.RentalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RentalService {
    private final RentalRepository rentalRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceService invoiceService;
    private final CarService carService;
    private final CarRepository carRepository;

    /**
     * Creates new rental with given information.
     *
     * @param user
     * @param carId
     * @param reservedFrom
     * @param reservedUntil
     * @param kmPackage
     */
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

    /**
     * Find Rental with given rentalId
     *
     * @param rentalId
     */
    public Rental findRental(long rentalId) {
        return rentalRepository.findById(rentalId).orElseThrow(() -> new NotFoundException("That rental was not found."));
    }

    /**
     * Marks car as picked up by tenant if its rental time has already started, it is his rental, it hasn't been picked up or delivered yet and it has been delivered by the previous renter.
     *
     * @param rentalId
     * @param user
     */
    public Rental pickupCar(long rentalId, User user) {
        Rental rental = findRental(rentalId);

        if (!LocalDateTime.now().isAfter(rental.getReservedFrom())) {
            throw new NotAllowedException("The rental start time hasn't started yet.");
        }

        if (rental.getTenant() != user) {
            throw new NotAllowedException("This is not your rental.");
        }

        if (rental.getPickedUpAt() != null) {
            throw new NotAllowedException("This rental has already been picked up.");
        }

        if (rental.getDeliveredAt() != null) {
            throw new NotAllowedException("This rental has already been delivered.");
        }

        Optional<Rental> broughtBackLateRental = rentalRepository.findRentalByCarIdAndPickedUpAtNotNullAndDeliveredAtIsNullAndIsCancelledFalse(rental.getCar().getId());
        if (broughtBackLateRental.isPresent()) {
            throw new NotAllowedException("Sorry, the previous renter has not brought the car yet back, so you cannot pick it up yet. Please wait and have a coffee.");
        }

        rental.setPickedUpAt(LocalDateTime.now());
        return rentalRepository.save(rental);
    }

    /**
     * Cancels the current Rental for a User if he has one, it hasn't started yet, and it hasn't been cancelled.
     *
     * @param user
     */
    public Rental cancelRental(User user) {
        Rental rental = rentalRepository.findRentalByTenantIdAndPickedUpAtIsNullAndDeliveredAtIsNullAndIsCancelledFalse(user.getId()).orElseThrow(() -> new NotFoundException("You don't have an active rental as tenant."));

        if (rental.getReservedFrom().isBefore(LocalDateTime.now())) {
            throw new NotAllowedException("This rental has already started, so you must pick it up and deliver it back.");
        }

        if (rental.isCancelled()) {
            throw new NotAllowedException("This rental has already been cancelled.");
        }

        rental.setCancelled(true);
        return rentalRepository.save(rental);
    }

    /**
     * Delivers the given car if the rental has already started, it belongs ot the user, it has been picked up yet, has not been delivered and the mileage is equal or higher to when it has been.
     *
     * @param rentalId
     * @param user
     * @param mileageTotal
     * @param drivingScore
     * @param lat
     * @param lng
     * @return
     */
    public Invoice deliverCar(long rentalId, User user, long mileageTotal, Double drivingScore, Double lat, Double lng) {
        Rental rental = findRental(rentalId);

        if (!LocalDateTime.now().isAfter(rental.getReservedFrom())) {
            throw new NotAllowedException("The rental start time hasn't started yet.");
        }

        if (rental.getTenant() != user) {
            throw new NotAllowedException("This is not your rental.");
        }

        if (rental.getPickedUpAt() == null) {
            throw new NotAllowedException("This rental has not been picked up yet.");
        }

        if (rental.getDeliveredAt() != null) {
            throw new NotAllowedException("This rental has already been delivered.");
        }

        Car car = rental.getCar();
        if (mileageTotal < car.getKilometersCurrent()) {
            throw new NotAllowedException("The kilometer count you submitted is lower than the count before you rented it.");
        }

        /**
         * limit drivingscore minimum and maximum
         */
        if (drivingScore < 0.8) {
            drivingScore = 0.8;
        } else if (drivingScore > 1.2) {
            drivingScore = 1.2;
        }

        // Updates Rental info
        rental.setDeliveredAt(LocalDateTime.now());
        rental.setMileageTotal(mileageTotal);
        rental.setDrivingStyleScore(drivingScore);
        rental = rentalRepository.save(rental);

        long kmsDriven = rental.getMileageTotal() - car.getKilometersCurrent();
        Double mileageCost;
        if (kmsDriven <= rental.getKmPackage()) {
            mileageCost = 0.0;
        } else {
            mileageCost = car.calculateCostForKms(kmsDriven - rental.getKmPackage());
        }

        // Update Car with the post-Rental information so the info is up to date
        car.setKilometersCurrent(rental.getMileageTotal());
        car.setLat(lat);
        car.setLng(lng);
        carRepository.save(car);

        // Calculate costs and round them
        Double hoursUsed = NumberRounding.round(Double.valueOf(ChronoUnit.SECONDS.between(rental.getReservedFrom(), rental.getDeliveredAt())) / 3600, 2);
        Double totalHourCost = NumberRounding.round(hoursUsed * car.getPricePerHour(), 2);

        Double totalCosts = (car.getInitialCost() + totalHourCost + mileageCost) * drivingScore;
        totalCosts = NumberRounding.round(totalCosts, 2);

        // Create and return Invoice
        Invoice invoice = invoiceService.createInvoice(kmsDriven, car.getInitialCost(), mileageCost, rental.getKmPackage(), totalHourCost, hoursUsed, totalCosts, false, rental.getTenant(), rental.getCarOwner(), rental);
        return invoiceRepository.save(invoice);
    }

    public List<Rental> getRentalsForCar(Long id) {
        return rentalRepository.findRentalsByCarId(id);
    }

    public List<Rental> getRentalsByCarOwner(User user) {
        return rentalRepository.findRentalsByCarOwnerId(user.getId());
    }

    public Optional<Rental> findOpenRentalForUserId(User user) {
        return rentalRepository.findRentalByTenantIdAndPickedUpAtIsNullAndDeliveredAtIsNullAndIsCancelledFalse(user.getId());
    }
}
