package nl.bss.carrentapi.api.repository;

import nl.bss.carrentapi.api.models.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    public List<Car> getByOwnerId(long userId);

    public Car getByLicensePlate(String licensePlate);
}