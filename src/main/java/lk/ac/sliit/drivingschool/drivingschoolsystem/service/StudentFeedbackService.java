package lk.ac.sliit.drivingschool.drivingschoolsystem.service;

import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.StudentFeedbackDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Instructor;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Student;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.StudentFeedback;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.InstructorRepository;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.StudentFeedbackRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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

        if (dto.getInstructorId() != null) {
            Instructor instructor = instructorRepository.findById(dto.getInstructorId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Instructor ID"));
            feedback.setInstructor(instructor);
        } else if (dto.getCourseName() != null && !dto.getCourseName().trim().isEmpty()) {
            feedback.setCourseName(dto.getCourseName().trim());
        } else {
            throw new IllegalArgumentException("Feedback submission must contain either an instructor profile reference or a valid course title descriptor.");
        }

        feedbackRepository.save(feedback);
    }

    @Transactional(readOnly = true)
    public List<StudentFeedbackDto> getInstructorFeedbackForStaff() {
        return feedbackRepository.findByInstructorIsNotNullOrderBySubmissionDateDesc().stream()
                .map(f -> mapToDto(f, "INSTRUCTOR"))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StudentFeedbackDto> getCourseFeedbackForStaff() {
        return feedbackRepository.findByInstructorIsNullOrderBySubmissionDateDesc().stream()
                .map(f -> mapToDto(f, "COURSE"))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }

    private StudentFeedbackDto mapToDto(StudentFeedback feedback, String type) {
        StudentFeedbackDto dto = new StudentFeedbackDto();
        dto.setId(feedback.getId());
        dto.setRating(feedback.getRating());
        dto.setComments(feedback.getComments());
        dto.setFeedbackType(type);

        if (feedback.getStudent() != null) {
            dto.setStudentName(feedback.getStudent().getName());
        }

        if (feedback.getInstructor() != null) {
            dto.setInstructorId(feedback.getInstructor().getId());
            dto.setInstructorName(feedback.getInstructor().getName());
        }

        if (feedback.getCourseName() != null) {
            dto.setCourseName(feedback.getCourseName());
        }

        if (feedback.getSubmissionDate() != null) {
            dto.setDateFormatted(feedback.getSubmissionDate()
                    .format(DateTimeFormatter.ofPattern("MMM dd, yyyy - hh:mm a")));
        }

        return dto;
    }
}
