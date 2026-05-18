package lk.ac.sliit.drivingschool.drivingschoolsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "lesson_packages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LessonPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "package_name")
    private String packageName;

    private int numberOfLessons;
    private double basePrice;
}