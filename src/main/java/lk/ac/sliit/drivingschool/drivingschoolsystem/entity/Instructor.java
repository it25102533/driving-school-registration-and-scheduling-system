package lk.ac.sliit.drivingschool.drivingschoolsystem.entity;

// Updated to point directly to your actual shared Person entity location
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Person;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.LessonPackage;
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

    @Column(unique = true, length = 128)
    private String email;

    @Column(length = 120)
    private String passwordHash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id")
    private LessonPackage assignedPackage;
}