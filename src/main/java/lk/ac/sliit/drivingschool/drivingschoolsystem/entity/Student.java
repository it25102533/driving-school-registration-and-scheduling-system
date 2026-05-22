package lk.ac.sliit.drivingschool.drivingschoolsystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student extends Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int age;

    private String licenseType;

    @Column(unique = true, length = 128, nullable = false)
    private String email;

    @Column(length = 120, nullable = false)
    private String passwordHash;

    @Column(length = 500)
    private String address;
}