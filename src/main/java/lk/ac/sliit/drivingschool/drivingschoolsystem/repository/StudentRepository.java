package lk.ac.sliit.drivingschool.drivingschoolsystem.repository;

import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository interface for Student entity.
 * JpaRepository provides standard CRUD operations.
 */
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Useful for login or profile lookup
    Optional<Student> findByEmailIgnoreCase(String email);

    // Useful for registration validation to prevent duplicate emails
    boolean existsByEmailIgnoreCase(String email);
}