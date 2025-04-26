package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom;

import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.SuperBO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.PatientDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.TherapistDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.TherapyProgramDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.TherapySessionDTO;

import java.util.List;

public interface TherapySessionBO extends SuperBO {
    boolean saveSession(TherapySessionDTO dto);
    boolean updateSession(TherapySessionDTO dto);
    boolean deleteSession(String id);
    String getNextSessionID();
    List<TherapySessionDTO> getSessions();
    List<PatientDTO> getPatients();
    List<TherapyProgramDTO> getPrograms();
    TherapySessionDTO getSession(String id);
    PatientDTO getPatient(String id);
    TherapyProgramDTO getProgram(String id);
    TherapistDTO getTherapist(String id);
    List<TherapySessionDTO> getSessionsFromTherapist(String id);
}
