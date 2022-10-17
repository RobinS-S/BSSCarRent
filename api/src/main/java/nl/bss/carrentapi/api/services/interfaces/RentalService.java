package nl.bss.carrentapi.api.services.interfaces;

import nl.bss.carrentapi.api.models.Car;
import nl.bss.carrentapi.api.models.Rental;
import nl.bss.carrentapi.api.models.User;

import java.time.LocalDateTime;

public interface RentalService {
    Rental createRental(LocalDateTime reservedFrom, LocalDateTime reservedUntil, long kmPackage, User tenant, Car car);

    boolean isBeingRented(Car car);

    void deliverCar(Rental rental, LocalDateTime deliveredAt, long mileageTotal);
}
