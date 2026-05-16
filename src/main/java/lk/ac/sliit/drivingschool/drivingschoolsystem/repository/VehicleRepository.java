package lk.ac.sliit.drivingschool.drivingschoolsystem.repository;

import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Vehicle entity.
 * Extends JpaRepository to provide standard CRUD operations.
 */
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    /**
     * Custom query method to check if a vehicle exists by its plate number.
     * Spring Data JPA automatically generates the implementation based on the method name.
     * * @param plateNumber the license plate to check
     * @return true if found, false otherwise
     */
    boolean existsByPlateNumber(String plateNumber);
}