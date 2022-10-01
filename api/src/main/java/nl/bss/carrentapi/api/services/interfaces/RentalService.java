package nl.bss.carrentapi.api.services.interfaces;

import nl.bss.carrentapi.api.models.entities.Car;
import nl.bss.carrentapi.api.models.entities.Rental;
import nl.bss.carrentapi.api.models.entities.User;

import java.time.LocalDateTime;

public interface RentalService {
    public Rental createRental(LocalDateTime reservedFrom, LocalDateTime reservedUntil, long kmPackage, User tenant, Car car);

    public boolean isBeingRented(Car car);

    public void deliverCar(Rental rental, LocalDateTime deliveredAt, long mileageTotal);
}
