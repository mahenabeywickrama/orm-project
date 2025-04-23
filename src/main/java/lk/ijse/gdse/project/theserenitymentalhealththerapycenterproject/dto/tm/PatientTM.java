package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientTM {
    private String patientId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String medicalHistory;
}
