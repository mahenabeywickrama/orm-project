package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom.impl;

import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom.PatientBO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.DAOFactory;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.DAOTypes;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.custom.PatientDAO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.PatientDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.PatientProgramDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.TherapyProgramDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.Patient;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.TherapyProgram;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientBOImpl implements PatientBO {
    PatientDAO patientDAO = DAOFactory.getInstance().getDAO(DAOTypes.PATIENT);

    @Override
    public boolean savePatient(PatientDTO dto) {
        Patient patient = new Patient(
                dto.getPatientId(),
                dto.getFullName(),
                dto.getEmail(),
                dto.getPhoneNumber(),
                dto.getMedicalHistory(),
                null,
                null
        );

        return patientDAO.save(patient);
    }

    @Override
    public boolean updatePatient(PatientDTO dto) {
        Patient patient = new Patient(
                dto.getPatientId(),
                dto.getFullName(),
                dto.getEmail(),
                dto.getPhoneNumber(),
                dto.getMedicalHistory(),
                null,
                null
        );

        return patientDAO.update(patient);
    }

    @Override
    public boolean deletePatient(String id) {
        return patientDAO.delete(id);
    }

    @Override
    public String getNextPatientId() {
        Optional<String> lastPatientID = patientDAO.getLastId();

        if (lastPatientID.isPresent()) {
            String lastID = lastPatientID.get();
            int numericPart = Integer.parseInt(lastID.substring(1));
            numericPart++;
            return String.format("P%03d", numericPart);
        } else {
            return "P001";
        }
    }

    @Override
    public List<PatientDTO> getPatients() {
        List<Patient> patients = patientDAO.getAll();
        List<PatientDTO> patientDTOs = new ArrayList<>();

        for (Patient patient : patients) {
            PatientDTO patientDTO = new PatientDTO();
            patientDTO.setPatientId(patient.getPatientId());
            patientDTO.setFullName(patient.getFullName());
            patientDTO.setEmail(patient.getEmail());
            patientDTO.setPhoneNumber(patient.getPhoneNumber());
            patientDTO.setMedicalHistory(patient.getMedicalHistory());
            patientDTOs.add(patientDTO);
        }

        return patientDTOs;
    }

    @Override
    public PatientDTO getPatientById(String id) {
        Optional<Patient> byId = patientDAO.findById(id);
        if (byId.isPresent()) {
            Patient patient = byId.get();
            PatientDTO patientDTO = new PatientDTO();
            patientDTO.setPatientId(patient.getPatientId());
            patientDTO.setFullName(patient.getFullName());
            patientDTO.setEmail(patient.getEmail());
            patientDTO.setPhoneNumber(patient.getPhoneNumber());
            patientDTO.setMedicalHistory(patient.getMedicalHistory());
            return patientDTO;
        }
        return null;
    }

    @Override
    public List<PatientDTO> getPatientsEnrolledInAllPrograms() {
        List<Patient> patients = patientDAO.getPatientsEnrolledInAllPrograms();
        List<PatientDTO> patientDTOs = new ArrayList<>();

        for (Patient patient : patients) {
            PatientDTO dto = new PatientDTO();
            dto.setPatientId(patient.getPatientId());
            dto.setFullName(patient.getFullName());
            dto.setEmail(patient.getEmail());
            dto.setPhoneNumber(patient.getPhoneNumber());
            dto.setMedicalHistory(patient.getMedicalHistory());
            patientDTOs.add(dto);
        }

        return patientDTOs;
    }

    @Override
    public List<String> getPatientIdsEnrolledInAllPrograms() {
        List<Patient> patients = patientDAO.getPatientsEnrolledInAllPrograms();
        List<String> ids = new ArrayList<>();

        for (Patient p : patients) {
            ids.add(p.getPatientId());
        }
        return ids;
    }

    @Override
    public List<TherapyProgramDTO> getProgramsForPatient(String patientId) {
        List<TherapyProgram> programs = patientDAO.getProgramsByPatientId(patientId);
        List<TherapyProgramDTO> dtos = new ArrayList<>();
        for (TherapyProgram p : programs) {
            TherapyProgramDTO dto = new TherapyProgramDTO();
            dto.setProgramId(p.getProgramId());
            dto.setName(p.getName());
            dto.setDurationInWeeks(p.getDurationInWeeks());
            dto.setFee(p.getFee());
            dto.setTherapistId(p.getTherapist().getTherapistId());
            dtos.add(dto);
        }
        return dtos;
    }

}
