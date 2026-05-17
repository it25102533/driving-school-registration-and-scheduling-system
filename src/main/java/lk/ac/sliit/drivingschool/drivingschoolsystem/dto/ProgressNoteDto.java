package lk.ac.sliit.drivingschool.drivingschoolsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Data Transfer Object for ProgressNote.
 * Used to transfer structured data cleanly between the Thymeleaf UI and Service layers.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressNoteDto {
    private Long id;
    private Long studentId; // Matches UI form context tracking cleanly
    private String lessonTopic;
    private String instructorNote;
    private String dateFormatted; // Formatted date string for user-friendly presentation
}