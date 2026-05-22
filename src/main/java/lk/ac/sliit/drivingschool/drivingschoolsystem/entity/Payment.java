package lk.ac.sliit.drivingschool.drivingschoolsystem.entity;

import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Student;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id // GENERATING PRIMARY KEY
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // FOREIGN KEY
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", nullable = false)
    private LessonPackage lessonPackage;

    private double amountPaid;
    private LocalDateTime paymentDate; // LOCAL DATE&TIME
}