package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom;

import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.SuperBO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.EnrollmentDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.PatientDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.TherapyProgramDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.TherapyProgram;

import java.util.List;

public interface EnrollmentBO extends SuperBO {
    boolean saveEnrollment(EnrollmentDTO dto);
    boolean updateEnrollment(EnrollmentDTO dto);
    boolean deleteEnrollment(String id);
    String getNextEnrollmentID();
    String getNextPaymentID();
    List<EnrollmentDTO> getEnrollments();
    List<PatientDTO> getPatients();
    List<TherapyProgramDTO> getTherapyPrograms();
    EnrollmentDTO getEnrollment(String id);
    PatientDTO getPatient(String id);
    TherapyProgramDTO getTherapyProgram(String id);
    List<TherapyProgramDTO> getProgramsByEnrollment(String id);
}
