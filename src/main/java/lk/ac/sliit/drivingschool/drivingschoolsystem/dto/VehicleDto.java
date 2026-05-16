package lk.ac.sliit.drivingschool.drivingschoolsystem.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Vehicle data.
 * Used for transferring data between the Controller and the Service layer.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDto {
    private Long id;
    private String model;
    private String plateNumber;
    private String type;
}