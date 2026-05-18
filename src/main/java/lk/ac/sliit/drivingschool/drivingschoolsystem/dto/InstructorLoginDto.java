package lk.ac.sliit.drivingschool.drivingschoolsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstructorLoginDto {
    private String email;
    private String password;
}
