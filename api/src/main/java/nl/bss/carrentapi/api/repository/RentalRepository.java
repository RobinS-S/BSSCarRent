package nl.bss.carrentapi.api.repository;

import nl.bss.carrentapi.api.models.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findRentalsByCarId(long rentalId);

    @Query(value = "SELECT * FROM rentals WHERE car_id = :carId AND (reserved_until >= :start) AND NOT is_cancelled AND delivered_at IS NULL", nativeQuery = true)
    List<Rental> findRentalsByCarIdAndIsCancelledFalseFromThisTime(@Param("carId") long carId, @Param("start") LocalDateTime start);

    List<Rental> findRentalsByCarOwnerId(long rentalId);

    List<Rental> findRentalsByTenantId(long rentalId);

    /**
     * Finds rentals that haven't been brought back yet for given carId
     */
    Optional<Rental> findRentalByCarIdAndDeliveredAtIsNullAndIsCancelledFalse(long carId);

    /**
     * Finds a rental that hasn't been brought back yet, but has been picked up for given carId
     */
    Optional<Rental> findRentalByCarIdAndPickedUpAtNotNullAndDeliveredAtIsNullAndIsCancelledFalse(long carId);

    /**
     * Finds rentals between a LocalDateTime-range for given carId
     */
    @Query(value = "SELECT * FROM rentals WHERE car_id = :carId AND (reserved_from BETWEEN :start and :end OR reserved_until BETWEEN :start and :end) AND NOT is_cancelled AND delivered_at IS NULL", nativeQuery = true)
    Optional<Rental> findBlockingRentalBetween(@Param("carId") long carId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    /**
     * Finds open rental by the User id that rents it
     */
    Optional<Rental> findRentalByTenantIdAndDeliveredAtIsNullAndIsCancelledFalse(long tenantId);

    /**
     * Finds open rental that has neither been picked up or delivered yet where rentalId is the renter
     */
    Optional<Rental> findRentalByTenantIdAndPickedUpAtIsNullAndDeliveredAtIsNullAndIsCancelledFalse(long tenantId);
}