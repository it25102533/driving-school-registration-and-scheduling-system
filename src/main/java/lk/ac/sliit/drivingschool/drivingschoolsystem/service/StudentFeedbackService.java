package lk.ac.sliit.drivingschool.drivingschoolsystem.service;

import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.StudentFeedbackDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Instructor;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Student;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.StudentFeedback;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.InstructorRepository;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.StudentFeedbackRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentFeedbackService {

    private final StudentFeedbackRepository feedbackRepository;
    private final InstructorRepository instructorRepository;

    public StudentFeedbackService(StudentFeedbackRepository feedbackRepository, InstructorRepository instructorRepository) {
        this.feedbackRepository = feedbackRepository;
        this.instructorRepository = instructorRepository;
    }

    @Transactional
    public void saveFeedback(Student loggedInStudent, StudentFeedbackDto dto) {
        StudentFeedback feedback = new StudentFeedback();
        feedback.setStudent(loggedInStudent);
        feedback.setRating(dto.getRating());
        feedback.setComments(dto.getComments());

        // FLOW A: Processing Instructor Performance Reviews
        if (dto.getInstructorId() != null) {
            Instructor instructor = instructorRepository.findById(dto.getInstructorId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Instructor ID"));
            feedback.setInstructor(instructor);
        }
        // FLOW B: Processing Course Curriculum Reviews
        else if (dto.getCourseName() != null && !dto.getCourseName().trim().isEmpty()) {
            feedback.setCourseName(dto.getCourseName().trim());
        }
        // FAILSAFE: Handles corrupt submissions where both crucial data parameters are missing
        else {
            throw new IllegalArgumentException("Feedback submission must contain either an instructor profile reference or a valid course title descriptor.");
        }

        feedbackRepository.save(feedback);
    }
}