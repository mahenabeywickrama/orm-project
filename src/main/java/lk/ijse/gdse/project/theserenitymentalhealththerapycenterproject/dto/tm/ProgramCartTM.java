package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProgramCartTM {
    private String programId;
    private String programName;
    private double fee;
    private Button remove;
}
