package lk.ac.sliit.drivingschool.drivingschoolsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vehicles")
@Data                // Generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor   // Generates the empty constructor required by JPA
@AllArgsConstructor  // Generates a constructor with all fields
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Model is required")
    @Column(nullable = false)
    private String model;

    @NotBlank(message = "Plate number is required")
    @Column(name = "plate_number", unique = true, nullable = false)
    private String plateNumber;

    /** Fuel / transmission type (e.g., Petrol, Automatic) */
    @Column(nullable = false)
    private String type;
}