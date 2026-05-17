package lk.ac.sliit.drivingschool.drivingschoolsystem.entity;


import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Student;
import jakarta.persistence.*;
        import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "progress_notes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProgressNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    private String lessonTopic;
    private String instructorNote;
    private LocalDateTime date;
}