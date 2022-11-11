package nl.bss.carrentapi.api.repository;

import nl.bss.carrentapi.api.models.CarImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
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

    /**
     * Deletes multiple car images, for example when deleting the car.
     * @param imageIds
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Images i WHERE id IN (:imageIds)", nativeQuery = true)
    void deleteByCarIdAndIdIn(List<Long> imageIds);
}