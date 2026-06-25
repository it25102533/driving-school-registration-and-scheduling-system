package lk.ac.sliit.drivingschool.drivingschoolsystem.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
    private Long id;
    private String name;
    private String phone;
    private int age;
    private String licenseType;
    private String transmissionPreference;
    private String licenseCode;
    private String licenseCategory;
    private String licenseDisplay;
    private String email;
    private String password;
    private String address;
}