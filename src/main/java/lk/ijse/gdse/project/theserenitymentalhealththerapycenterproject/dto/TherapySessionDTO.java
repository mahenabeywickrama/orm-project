package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class TherapySessionDTO {
    private String sessionId;
    private LocalDate sessionDate;
    private String patientId;
    private String therapistId;
    private String programId;
}
