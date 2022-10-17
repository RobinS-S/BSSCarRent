package nl.bss.carrentapi.api.repository;

import nl.bss.carrentapi.api.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByOwnerId(long userId);
}