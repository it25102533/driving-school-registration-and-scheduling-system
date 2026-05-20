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

    @Column(name = "package_name", nullable = false)
    private String packageName;

    @Column(length = 600)
    private String description;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "number_of_lessons", nullable = false)
    private int numberOfLessons;

    @Column(name = "base_price", nullable = false)
    private double basePrice;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder = 0;
}