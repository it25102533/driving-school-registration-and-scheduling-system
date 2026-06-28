package lk.ac.sliit.drivingschool.drivingschoolsystem.repository;

import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    java.util.Optional<Instructor> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);

    List<Instructor> findByAssignedPackage_Id(Long packageId);
    List<Instructor> findByAssignedVehicle_Id(Long vehicleId);
}