package nl.bss.carrentapi.api.repository;

import nl.bss.carrentapi.api.models.entities.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    public List<Rental> findRentalsByCarIdAndDeliveredAtIsNull(long carId);

    public List<Rental> findRentalsByTenantIdAndDeliveredAtIsNull(long userId);

    public List<Rental> findRentalsByCarOwnerIdAndDeliveredAtIsNull(long userId);
}