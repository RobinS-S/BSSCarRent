package nl.bss.carrentapi.api.repository;

import nl.bss.carrentapi.api.models.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findRentalsByCarId(long userId);

    List<Rental> findRentalsByCarOwnerId(long userId);

    List<Rental> findRentalsByTenantId(long userId);

    /**
     * Gets rentals that haven't been brought back yet
     */
    Optional<Rental> findRentalByCarIdAndDeliveredAtIsNullAndIsCancelledFalse(long carId);

    /**
     * Gets rental that hasn't been brought back yet, but has been picked up
     */
    Optional<Rental> findRentalByCarIdAndPickedUpAtNotNullAndDeliveredAtIsNullAndIsCancelledFalse(long carId);

    /**
     * Gets rentals between a datetime-range
     */
    @Query(value = "SELECT * FROM rentals WHERE car_id = :carId AND (reserved_from BETWEEN :start and :end OR reserved_until BETWEEN :start and :end) AND NOT is_cancelled AND delivered_at IS NULL", nativeQuery = true)
    Optional<Rental> findBlockingRentalBetween(@Param("carId") long carId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    /**
     * Gets rental by a tenant id that are still open
     */
    Optional<Rental> findRentalByTenantIdAndDeliveredAtIsNullAndIsCancelledFalse(long userId);

    /**
     * Gets the 'active' rental for a user that hasn't picked up his rental car yet
     */
    Optional<Rental> findRentalByTenantIdAndPickedUpAtIsNullAndDeliveredAtIsNullAndIsCancelledFalse(long userId);
}