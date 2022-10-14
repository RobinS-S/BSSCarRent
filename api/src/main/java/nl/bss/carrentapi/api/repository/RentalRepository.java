package nl.bss.carrentapi.api.repository;

import nl.bss.carrentapi.api.models.entities.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    public List<Rental> findRentalsByTenantIdAndDeliveredAtIsNullAndIsCancelledFalse(long userId);

    public List<Rental> findRentalsByCarId(long userId);

    public List<Rental> findRentalsByCarIdAndDeliveredAtIsNullAndIsCancelledFalse(long carId);

    @Query(value = "SELECT * FROM rentals WHERE car_id = :carId AND (reserved_from BETWEEN :start and :end OR reserved_until BETWEEN :start and :end) AND NOT is_cancelled", nativeQuery = true)
    public List<Rental> findRentalsByCarIdBetween(@Param("carId") long carId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    public List<Rental> findRentalsByCarOwnerId(long userId);

    /** Gets the 'active' rental for a user that has picked up his rental car and still needs to deliver it back */
    public Optional<Rental> findRentalByTenantIdAndDeliveredAtIsNullAndPickedUpAtIsNotNull(long userId);

    /** Gets the 'active' rental for a user that hasn't picked up his rental car yet */
    public Optional<Rental> findRentalByTenantIdAndPickedUpAtIsNullAndDeliveredAtIsNullAndIsCancelledFalse(long userId);
}