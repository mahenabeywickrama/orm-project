package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EnrollmentTM {
    private String enrollmentId;
    private LocalDate enrollmentDate;
    private String enrollmentStatus;
    private double totalCost;
    private String patientId;
}
