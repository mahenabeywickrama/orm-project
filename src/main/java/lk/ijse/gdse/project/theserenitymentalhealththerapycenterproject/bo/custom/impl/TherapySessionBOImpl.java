package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom.impl;

import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom.TherapySessionBO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.DAOFactory;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.DAOTypes;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.custom.PatientDAO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.custom.TherapistDAO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.custom.TherapyProgramDAO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.custom.TherapySessionDAO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.PatientDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.TherapistDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.TherapyProgramDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.TherapySessionDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.Patient;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.Therapist;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.TherapyProgram;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.TherapySession;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TherapySessionBOImpl implements TherapySessionBO {
    TherapySessionDAO therapySessionDAO = DAOFactory.getInstance().getDAO(DAOTypes.SESSION);
    PatientDAO patientDAO = DAOFactory.getInstance().getDAO(DAOTypes.PATIENT);
    TherapyProgramDAO therapyProgramDAO = DAOFactory.getInstance().getDAO(DAOTypes.PROGRAM);
    TherapistDAO therapistDAO = DAOFactory.getInstance().getDAO(DAOTypes.THERAPIST);

    @Override
    public boolean saveSession(TherapySessionDTO dto) {
        Optional<Patient> patientDAOById = patientDAO.findById(dto.getPatientId());
        if (patientDAOById.isEmpty()) return false;

        Patient patient = patientDAOById.get();

        Optional<TherapyProgram> therapyProgramDAOById = therapyProgramDAO.findById(dto.getProgramId());
        if (therapyProgramDAOById.isEmpty()) return false;

        TherapyProgram therapyProgram = therapyProgramDAOById.get();

        Optional<Therapist> therapistDAOById = therapistDAO.findById(dto.getTherapistId());
        if (therapistDAOById.isEmpty()) return false;

        Therapist therapist = therapistDAOById.get();

        TherapySession therapySession = new TherapySession(
                dto.getSessionId(),
                dto.getSessionDate(),
                patient,
                therapist,
                therapyProgram
        );

        return therapySessionDAO.save(therapySession);
    }

    @Override
    public boolean updateSession(TherapySessionDTO dto) {
        Optional<Patient> patientDAOById = patientDAO.findById(dto.getPatientId());
        if (patientDAOById.isEmpty()) return false;

        Patient patient = patientDAOById.get();

        Optional<TherapyProgram> therapyProgramDAOById = therapyProgramDAO.findById(dto.getProgramId());
        if (therapyProgramDAOById.isEmpty()) return false;

        TherapyProgram therapyProgram = therapyProgramDAOById.get();

        Optional<Therapist> therapistDAOById = therapistDAO.findById(dto.getTherapistId());
        if (therapistDAOById.isEmpty()) return false;

        Therapist therapist = therapistDAOById.get();

        TherapySession updatedSession = new TherapySession(
                dto.getSessionId(),
                dto.getSessionDate(),
                patient,
                therapist,
                therapyProgram
        );

        return therapySessionDAO.update(updatedSession);
    }

    @Override
    public boolean deleteSession(String id) {
        return therapySessionDAO.delete(id);
    }

    @Override
    public String getNextSessionID() {
        Optional<String> lastId = therapySessionDAO.getLastId();

        if (lastId.isPresent()) {
            String lastID = lastId.get();
            int numericPart = Integer.parseInt(lastID.substring(2));
            numericPart++;
            return String.format("TS%03d", numericPart);
        } else {
            return "TS001";
        }
    }

    @Override
    public List<TherapySessionDTO> getSessions() {
        List<TherapySession> therapySessions = therapySessionDAO.getAll();
        List<TherapySessionDTO> therapySessionDTOs = new ArrayList<>();

        for (TherapySession therapySession : therapySessions) {
            TherapySessionDTO therapySessionDTO = new TherapySessionDTO();
            therapySessionDTO.setSessionId(therapySession.getSessionId());
            therapySessionDTO.setSessionDate(therapySession.getSessionDate());
            therapySessionDTO.setPatientId(therapySession.getPatient().getPatientId());
            therapySessionDTO.setProgramId(therapySession.getProgram().getProgramId());
            therapySessionDTO.setTherapistId(therapySession.getTherapist().getTherapistId());
            therapySessionDTOs.add(therapySessionDTO);
        }
        return therapySessionDTOs;
    }

    @Override
    public List<PatientDTO> getPatients() {
        List<Patient> patients = patientDAO.getAll();
        List<PatientDTO> patientDTOS = new ArrayList<>();

        for (Patient patient : patients) {
            PatientDTO patientDTO = new PatientDTO();
            patientDTO.setPatientId(patient.getPatientId());
            patientDTO.setFullName(patient.getFullName());
            patientDTO.setEmail(patient.getEmail());
            patientDTO.setPhoneNumber(patient.getPhoneNumber());
            patientDTO.setMedicalHistory(patient.getMedicalHistory());
            patientDTOS.add(patientDTO);
        }
        return patientDTOS;
    }

    @Override
    public List<TherapyProgramDTO> getPrograms() {
        List<TherapyProgram> therapyPrograms = therapyProgramDAO.getAll();
        List<TherapyProgramDTO> therapyProgramDTOS = new ArrayList<>();

        for (TherapyProgram program : therapyPrograms) {
            TherapyProgramDTO dto = new TherapyProgramDTO();
            dto.setProgramId(program.getProgramId());
            dto.setName(program.getName());
            dto.setDurationInWeeks(program.getDurationInWeeks());
            dto.setFee(program.getFee());
            dto.setTherapistId(program.getTherapist().getTherapistId());
            therapyProgramDTOS.add(dto);
        }
        return therapyProgramDTOS;
    }

    @Override
    public TherapySessionDTO getSession(String id) {
        Optional<TherapySession> therapySession = therapySessionDAO.findById(id);
        if (therapySession.isPresent()) {
            TherapySession session = therapySession.get();
            TherapySessionDTO therapySessionDTO = new TherapySessionDTO();
            therapySessionDTO.setSessionId(session.getSessionId());
            therapySessionDTO.setSessionDate(session.getSessionDate());
            therapySessionDTO.setPatientId(session.getPatient().getPatientId());
            therapySessionDTO.setProgramId(session.getProgram().getProgramId());
            therapySessionDTO.setTherapistId(session.getTherapist().getTherapistId());

            return therapySessionDTO;
        }
        return null;
    }

    @Override
    public PatientDTO getPatient(String id) {
        Optional<Patient> patient = patientDAO.findById(id);
        if (patient.isPresent()) {
            Patient p = patient.get();
            PatientDTO patientDTO = new PatientDTO();
            patientDTO.setPatientId(p.getPatientId());
            patientDTO.setFullName(p.getFullName());
            patientDTO.setEmail(p.getEmail());
            patientDTO.setPhoneNumber(p.getPhoneNumber());
            patientDTO.setMedicalHistory(p.getMedicalHistory());
            return patientDTO;
        }
        return null;
    }

    @Override
    public TherapyProgramDTO getProgram(String id) {
        Optional<TherapyProgram> therapyProgram = therapyProgramDAO.findById(id);
        if (therapyProgram.isPresent()) {
            TherapyProgram program = therapyProgram.get();
            TherapyProgramDTO therapyProgramDTO = new TherapyProgramDTO();
            therapyProgramDTO.setProgramId(program.getProgramId());
            therapyProgramDTO.setName(program.getName());
            therapyProgramDTO.setDurationInWeeks(program.getDurationInWeeks());
            therapyProgramDTO.setFee(program.getFee());
            therapyProgramDTO.setTherapistId(program.getTherapist().getTherapistId());

            return therapyProgramDTO;
        }
        return null;
    }

    @Override
    public TherapistDTO getTherapist(String id) {
        Optional<Therapist> therapist = therapistDAO.findById(id);
        if (therapist.isPresent()) {
            Therapist th = therapist.get();
            TherapistDTO therapistDTO = new TherapistDTO();
            therapistDTO.setTherapistId(th.getTherapistId());
            therapistDTO.setName(th.getName());
            therapistDTO.setSpecialization(th.getSpecialization());
            therapistDTO.setContactNumber(th.getContactNumber());

            return therapistDTO;
        }
        return null;
    }

    @Override
    public List<TherapySessionDTO> getSessionsFromTherapist(String id) {
        List<TherapySession> sessions = therapySessionDAO.findSessionsByTherapistId(id);
        List<TherapySessionDTO> therapySessionDTOS = new ArrayList<>();

        for (TherapySession s : sessions) {
            TherapySessionDTO therapySessionDTO = new TherapySessionDTO();
            therapySessionDTO.setSessionId(s.getSessionId());
            therapySessionDTO.setSessionDate(s.getSessionDate());
            therapySessionDTO.setPatientId(s.getPatient().getPatientId());
            therapySessionDTO.setTherapistId(s.getTherapist().getTherapistId());
            therapySessionDTO.setProgramId(s.getProgram().getProgramId());
            therapySessionDTOS.add(therapySessionDTO);
        }
        return therapySessionDTOS;
    }
}
