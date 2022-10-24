package nl.bss.carrentapi.api.repository;

import nl.bss.carrentapi.api.models.CarImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CarImageRepository extends JpaRepository<CarImage, Long> {
    /**
     * Finds car image IDs for a given car ID.
     *
     * @param carId
     * @return List of car image IDs (Long).
     */
    @Query(value = "SELECT i.Id from Images i WHERE car_id = :carId", nativeQuery = true)
    List<Long> findIDsByCarId(@Param("carId") long carId);

    /**
     * Loads full car image for given car ID and image ID.
     *
     * @param carId
     * @return Optional CarImage.
     */
    Optional<CarImage> findByIdAndCarId(long id, long carId);
}