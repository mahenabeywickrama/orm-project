package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom;

import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.SuperBO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.PatientDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.TherapyProgramDTO;

import java.util.List;

public interface PatientBO extends SuperBO {
    boolean savePatient(PatientDTO dto);
    boolean updatePatient(PatientDTO dto);
    boolean deletePatient(String id);
    String getNextPatientId();
    List<PatientDTO> getPatients();
    PatientDTO getPatientById(String id);
    List<PatientDTO> getPatientsEnrolledInAllPrograms();
    List<String> getPatientIdsEnrolledInAllPrograms();
    List<TherapyProgramDTO> getProgramsForPatient(String patientId);
}
