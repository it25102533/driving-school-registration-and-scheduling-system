package lk.ac.sliit.drivingschool.drivingschoolsystem.dto;

import lombok.*;

/**
 * Data Transfer Object for Student.
 * Used to transfer data between the Controller and Service layers.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
    private Long id;
    private String name;
    private String phone;
    private int age;
    private String licenseType;
    private String studentType;
    private String email;
    private String password; // Plain text password from the registration form
    private String address;
}