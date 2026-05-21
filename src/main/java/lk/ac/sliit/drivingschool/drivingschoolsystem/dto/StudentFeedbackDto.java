package lk.ac.sliit.drivingschool.drivingschoolsystem.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class StudentFeedbackDto {

    private Long id;
    private Long instructorId; // Used when rating an instructor
    private String courseName;  // Used when rating a course/package
    private int rating;
    private String comments;

    // Display fields for staff dashboard
    private String studentName;
    private String instructorName;
    private String dateFormatted;
    private String feedbackType; // INSTRUCTOR or COURSE
}