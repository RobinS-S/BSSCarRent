package nl.bss.carrentapi.api.services.interfaces;

import nl.bss.carrentapi.api.models.Invoice;
import nl.bss.carrentapi.api.models.Rental;
import nl.bss.carrentapi.api.models.User;

import java.time.LocalDateTime;

public interface RentalService {
    Rental createRental(User user, long carId, LocalDateTime reservedFrom, LocalDateTime reservedUntil, long kmPackage);

    Rental findRental(long rentalId);

    Rental pickupCar(long rentalId, User user);

    Rental cancelRental(User user);

    Invoice deliverCar(long rentalId, User user, long mileageTotal, Double drivingScore, Double lat, Double lng);
}
