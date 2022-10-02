package nl.bss.carrentapi.api.repository;

import nl.bss.carrentapi.api.models.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {
    public List<Car> findByOwnerId(long userId);

    public Optional<Car> findByLicensePlate(String licensePlate);
}