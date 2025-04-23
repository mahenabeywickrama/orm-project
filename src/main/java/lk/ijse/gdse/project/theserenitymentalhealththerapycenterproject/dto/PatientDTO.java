package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientDTO {
    private String patientId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String medicalHistory;
}
