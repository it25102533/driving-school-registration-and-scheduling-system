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
    private String studentType;

    @Column(unique = true, length = 128, nullable = false)
    private String email;

    @Column(length = 120, nullable = false)
    private String passwordHash;

    @Column(length = 500)
    private String address;

    /**
     * Logic for calculating discounts.
     * Placing the string literal first avoids NullPointerExceptions.
     */
    public double calculateDiscount(double basePrice) {
        if ("Corporate".equalsIgnoreCase(this.studentType)) {
            return basePrice * 0.2;
        }
        return basePrice * 0.1;
    }
}