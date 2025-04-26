package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EnrollmentDTO {
    private String enrollmentId;
    private LocalDate enrollmentDate;
    private String enrollmentStatus;
    private String paymentStatus;
    private double totalCost;
    private String patientId;
    private List<String> programIds;
    private double payedAmount;
    private String method;
}
