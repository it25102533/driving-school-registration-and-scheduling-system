package lk.ac.sliit.drivingschool.drivingschoolsystem.entity;

// Updated to point directly to your actual shared Person entity location
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Person;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "instructors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Instructor extends Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String licenseNumber;
    private String specialization;
}