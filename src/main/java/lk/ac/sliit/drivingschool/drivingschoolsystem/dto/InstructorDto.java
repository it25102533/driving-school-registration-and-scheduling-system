package lk.ac.sliit.drivingschool.drivingschoolsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Data Transfer Object for Instructor.
 * Used to transfer structured data cleanly between the Thymeleaf UI and Service layers.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstructorDto {
    private Long id;
    private String name;           // Flattened from Person for easy UI binding
    private String phone;          // Flattened from Person for easy UI binding
    private String licenseNumber;
    private String specialization;
}