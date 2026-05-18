package lk.ac.sliit.drivingschool.drivingschoolsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Data Transfer Object for Payments.
 * Simplifies tracking data bindings between Thymeleaf payment views and domain entities.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private Long id;
    private Long studentId;
    private Long packageId;

    // Flattened display fields for readable UI integration
    private String packageName;
    private double amountPaid;
    private String formattedDate;
}