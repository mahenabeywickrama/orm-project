package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TherapyProgramDTO {
    private String programId;
    private String name;
    private int durationInWeeks;
    private double fee;
    private String therapistId;
}
