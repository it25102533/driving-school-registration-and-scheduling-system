package lk.ac.sliit.drivingschool.drivingschoolsystem.entity;

import jakarta.persistence.*;
        import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_feedback")
@Getter
@Setter
@NoArgsConstructor // Automatically injects your default constructor block
public class StudentFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    // FIXED: Changed nullable to true so this entity can handle BOTH course feedback and instructor feedback forms
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id", nullable = true)
    private Instructor instructor;

    // NEW: Captures the course/package name parameter submitted by your secondary form
    @Column(name = "course_name")
    private String courseName;

    @Column(nullable = false)
    private int rating; // 1 to 5 stars

    @Column(length = 1000)
    private String comments;

    @Column(name = "submission_date", nullable = false)
    private LocalDateTime submissionDate;

    // Automatically assigns the current timestamp before writing the row to MySQL
    @PrePersist
    protected void onCreate() {
        this.submissionDate = LocalDateTime.now();
    }
}