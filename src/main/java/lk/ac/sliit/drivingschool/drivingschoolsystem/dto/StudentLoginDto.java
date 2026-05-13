package lk.ac.sliit.drivingschool.drivingschoolsystem.dto;

import lombok.*;

/**
 * Data Transfer Object specifically for Student Login requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentLoginDto {
    private String email;
    private String password;

    // Legacy fields - consider removing these if this is just for the login request!
    private String name;
    private Long studentId;
}