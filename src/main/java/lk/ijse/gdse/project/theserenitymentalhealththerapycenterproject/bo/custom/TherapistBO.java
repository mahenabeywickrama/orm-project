package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom;

import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.SuperBO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.TherapistDTO;

import java.util.List;

public interface TherapistBO extends SuperBO {
    boolean saveTherapist(TherapistDTO dto);
    boolean updateTherapist(TherapistDTO dto);
    boolean deleteTherapist(String id);
    String getNextTherapistId();
    List<TherapistDTO> getTherapists();
    TherapistDTO getTherapistById(String id);
}
