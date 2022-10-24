package nl.bss.carrentapi.api.repository;

import nl.bss.carrentapi.api.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    /**
     * Finds a car by its owner's User ID.
     *
     * @param userId
     * @return
     */
    List<Car> findByOwnerId(long userId);
}