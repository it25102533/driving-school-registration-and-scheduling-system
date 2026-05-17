package lk.ac.sliit.drivingschool.drivingschoolsystem.repository;

import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.ProgressNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProgressRepository extends JpaRepository<ProgressNote, Long> {

    // Custom query to find all progress notes for a specific student ID
    List<ProgressNote> findByStudent_Id(Long studentId);
}