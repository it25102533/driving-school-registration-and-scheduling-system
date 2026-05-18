package lk.ac.sliit.drivingschool.drivingschoolsystem.repository;

import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    // Derived query methods to fetch schedules cleanly
    List<Lesson> findByStudent_Id(Long studentId);
    List<Lesson> findByInstructor_Id(Long instructorId);
}