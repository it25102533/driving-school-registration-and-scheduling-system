package lk.ac.sliit.drivingschool.drivingschoolsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Data Transfer Object for Lesson management.
 * Simplifies tracking data bindings between Thymeleaf schedules and domain entities.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonDto {
    private Long id;
    private Long studentId;
    private Long instructorId;
    private Long vehicleId;
    private String lessonTime; // Raw datetime-local string input from HTML forms (yyyy-MM-dd'T'HH:mm)
    private String status;
    private String vehicleType;

    // Flattened display fields for readable UI integration
    private String instructorName;
    private String vehicleModel;
    private String formattedDate;
}