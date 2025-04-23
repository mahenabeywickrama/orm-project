package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom;

import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.SuperBO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.TherapistDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.TherapyProgramDTO;

import java.util.List;

public interface TherapyProgramBO extends SuperBO {
    boolean saveProgram(TherapyProgramDTO dto);
    boolean updateProgram(TherapyProgramDTO dto);
    boolean deleteProgram(String id);
    String getNextProgramId();
    List<TherapyProgramDTO> getPrograms();
    List<TherapistDTO> getTherapists();
    TherapyProgramDTO getProgram(String id);
    TherapistDTO getTherapist(String id);
}
