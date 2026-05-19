package lk.ac.sliit.drivingschool.drivingschoolsystem.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor // Handles your default constructor block automatically
public class StudentFeedbackDto {

    private Long instructorId; // Used when rating an instructor

    private String courseName;  // NEW: Used when rating a course/package

    private int rating;         // 1 to 5 stars bound to both forms

    private String comments;    // Optional feedback notes bound to both forms
}