package lk.ac.sliit.drivingschool.drivingschoolsystem.repository;

import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.StudentFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentFeedbackRepository extends JpaRepository<StudentFeedback, Long> {

    // Pulls all feedback submitted by a specific student
    List<StudentFeedback> findByStudentId(Long studentId);

    // Pulls all performance feedback given to a specific instructor
    List<StudentFeedback> findByInstructorId(Long instructorId);

    // NEW: Pulls reviews for a specific package (e.g., "Beginners Package")
    List<StudentFeedback> findByCourseNameIgnoreCase(String courseName);

    // NEW: Separates general course reviews from instructor specific reviews
    List<StudentFeedback> findByInstructorIsNull();
}