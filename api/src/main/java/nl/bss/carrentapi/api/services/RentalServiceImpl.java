package nl.bss.carrentapi.api.services;

import nl.bss.carrentapi.api.models.entities.Car;
import nl.bss.carrentapi.api.models.entities.Rental;
import nl.bss.carrentapi.api.models.entities.User;
import nl.bss.carrentapi.api.repository.RentalRepository;
import nl.bss.carrentapi.api.services.interfaces.RentalService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;

    public RentalServiceImpl(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    /**
     * Creates new rental with given information. Still needs to be saved in order to persist.
     */
    @Override
    public Rental createRental(LocalDateTime reservedFrom, LocalDateTime reservedUntil, long kmPackage, User tenant, Car car) {
        return new Rental(reservedFrom, reservedUntil, kmPackage, tenant, car.getOwner(), car);
    }

    /**
     * Checks if there are any active rentals for given car
     */
    @Override
    public boolean isBeingRented(Car car) {
        List<Rental> rentals = rentalRepository.findRentalsByCarIdAndDeliveredAtIsNull(car.getId());
        return rentals != null && rentals.size() > 0;
    }

    /**
     * Marks car as delivered by car owner and fills out ride details
     */
    @Override
    public void deliverCar(Rental rental, LocalDateTime deliveredAt, long mileageTotal) {
        if (deliveredAt.isAfter(rental.getReservedUntil())) {
            // The car has been delivered late.
            // TODO: Apply penalties?
        }

        rental.setDeliveredAt(deliveredAt);
        rental.setMileageTotal(mileageTotal);
    }
}
