package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.custom;

import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.CrudDAO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.Patient;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.TherapyProgram;

import java.util.List;

public interface PatientDAO extends CrudDAO<Patient, String> {
    List<Patient> getPatientsEnrolledInAllPrograms();
    List<TherapyProgram> getProgramsByPatientId(String patientId);
}
