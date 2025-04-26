package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom.impl;

import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom.EnrollmentBO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.config.FactoryConfiguration;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.DAOFactory;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.DAOTypes;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.custom.*;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.EnrollmentDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.PatientDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.TherapyProgramDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.UserDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.*;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.util.CurrentUser;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnrollmentBOImpl implements EnrollmentBO {
    EnrollmentDAO enrollmentDAO = DAOFactory.getInstance().getDAO(DAOTypes.ENROLLMENT);
    PatientDAO patientDAO = DAOFactory.getInstance().getDAO(DAOTypes.PATIENT);
    TherapyProgramDAO therapyProgramDAO = DAOFactory.getInstance().getDAO(DAOTypes.PROGRAM);
    PaymentDAO paymentDAO = DAOFactory.getInstance().getDAO(DAOTypes.PAYMENT);
    UserDAO userDAO = DAOFactory.getInstance().getDAO(DAOTypes.USER);

    FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public boolean saveEnrollment(EnrollmentDTO dto) {
        Session session = factoryConfiguration.getSession();
        Transaction tx = session.beginTransaction();

        try {
            Patient patient = session.get(Patient.class, dto.getPatientId());
            if (patient == null) return false;

            List<TherapyProgram> programs = new ArrayList<>();
            for (String programId : dto.getProgramIds()) {
                TherapyProgram program = session.get(TherapyProgram.class, programId);
                if (program != null) programs.add(program);
            }

            Payment payment = new Payment();
            payment.setPaymentId(getNextPaymentID());
            payment.setTotalCost(dto.getTotalCost());
            payment.setPayedAmount(dto.getPayedAmount());
            payment.setBalance(0);
            payment.setStatus(dto.getPaymentStatus());
            payment.setMethod(dto.getMethod());
            payment.setPaymentDate(dto.getEnrollmentDate());

            UserDTO currentUser = CurrentUser.getCurrentUser();
            Optional<User> user = userDAO.findByUsername(currentUser.getUsername());
            if (user.isPresent()) {
                payment.setProcessedBy(user.get());
            }

            Enrollment enrollment = new Enrollment();
            enrollment.setEnrollmentId(dto.getEnrollmentId());
            enrollment.setEnrollmentDate(dto.getEnrollmentDate());
            enrollment.setEnrollmentStatus("PENDING");
            enrollment.setTotalCost(dto.getTotalCost());
            enrollment.setPatient(patient);
            enrollment.setTherapyPrograms(programs);
            enrollment.setPayment(payment);

            payment.setEnrollment(enrollment);

            session.persist(payment);
            session.persist(enrollment);

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateEnrollment(EnrollmentDTO dto) {
        Session session = factoryConfiguration.getSession();
        Transaction tx = session.beginTransaction();

        try {
            Enrollment enrollment = session.get(Enrollment.class, dto.getEnrollmentId());
            if (enrollment == null) return false;

            if (enrollment.getEnrollmentStatus().equals("PENDING")) {
                Patient patient = session.get(Patient.class, dto.getPatientId());
                if (patient != null) {
                    enrollment.setPatient(patient);
                }
            } else {
                System.err.println("Cannot change patient for a paid enrollment.");
                return false;
            }

            enrollment.setEnrollmentDate(dto.getEnrollmentDate());
            enrollment.setEnrollmentStatus(dto.getEnrollmentStatus());

            List<TherapyProgram> programs = new ArrayList<>();
            for (String programId : dto.getProgramIds()) {
                TherapyProgram program = session.get(TherapyProgram.class, programId);
                if (program != null) programs.add(program);
            }
            enrollment.setTherapyPrograms(programs);

            enrollment.setTotalCost(dto.getTotalCost());

            session.merge(enrollment);
            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean deleteEnrollment(String id) {
        Session session = factoryConfiguration.getSession();
        Transaction tx = session.beginTransaction();

        try {
            Enrollment enrollment = session.get(Enrollment.class, id);
            if (enrollment == null) {
                return false;
            }

            if (!"PENDING".equals(enrollment.getEnrollmentStatus())) {
                System.err.println("Cannot delete a paid or active enrollment.");
                return false;
            }

            Payment payment = enrollment.getPayment();
            if (payment != null) {
                session.remove(payment);
            }

            session.remove(enrollment);

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public String getNextPaymentID() {
        Optional<String> lastId = paymentDAO.getLastId();

        if (lastId.isPresent()) {
            String lastID = lastId.get();
            int numericPart = Integer.parseInt(lastID.substring(1));
            numericPart++;
            return String.format("P%03d", numericPart);
        } else {
            return "P001";
        }
    }

    @Override
    public String getNextEnrollmentID() {
        Optional<String> lastId = enrollmentDAO.getLastId();

        if (lastId.isPresent()) {
            String lastID = lastId.get();
            int numericPart = Integer.parseInt(lastID.substring(1));
            numericPart++;
            return String.format("E%03d", numericPart);
        } else {
            return "E001";
        }
    }

    @Override
    public List<EnrollmentDTO> getEnrollments() {
        List<Enrollment> enrollments = enrollmentDAO.getAll();
        List<EnrollmentDTO> enrollmentDTOs = new ArrayList<>();

        for (Enrollment enrollment : enrollments) {
            EnrollmentDTO enrollmentDTO = new EnrollmentDTO();
            enrollmentDTO.setEnrollmentId(enrollment.getEnrollmentId());
            enrollmentDTO.setEnrollmentDate(enrollment.getEnrollmentDate());
            enrollmentDTO.setEnrollmentStatus(enrollment.getEnrollmentStatus());
            enrollmentDTO.setTotalCost(enrollment.getTotalCost());
            enrollmentDTO.setPatientId(enrollment.getPatient().getPatientId());

            List<TherapyProgram> therapyPrograms = enrollment.getTherapyPrograms();
            List<String> programIds = new ArrayList<>();
            for (TherapyProgram therapyProgram : therapyPrograms) {
                programIds.add(therapyProgram.getProgramId());
            }
            enrollmentDTO.setProgramIds(programIds);

            enrollmentDTOs.add(enrollmentDTO);
        }
        return enrollmentDTOs;
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
    public List<TherapyProgramDTO> getTherapyPrograms() {
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
    public EnrollmentDTO getEnrollment(String id) {
        Optional<Enrollment> opt = enrollmentDAO.findById(id);
        if (opt.isEmpty()) return null;

        Enrollment e = opt.get();
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setEnrollmentId(e.getEnrollmentId());
        dto.setEnrollmentDate(e.getEnrollmentDate());
        dto.setEnrollmentStatus(e.getEnrollmentStatus());
        dto.setTotalCost(e.getTotalCost());
        dto.setPatientId(e.getPatient().getPatientId());

//        List<String> programIds = new ArrayList<>();
//        for (TherapyProgram program : e.getTherapyPrograms()) {
//            programIds.add(program.getProgramId());
//        }
//        dto.setProgramIds(programIds);

        return dto;
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
    public TherapyProgramDTO getTherapyProgram(String id) {
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
    public List<TherapyProgramDTO> getProgramsByEnrollment(String id) {
        List<TherapyProgram> programsFromEnrollment = enrollmentDAO.getProgramsFromEnrollment(id);
        List<TherapyProgramDTO> programDTOS = new ArrayList<>();
        for (TherapyProgram program : programsFromEnrollment) {
            TherapyProgramDTO dto = new TherapyProgramDTO();
            dto.setProgramId(program.getProgramId());
            dto.setName(program.getName());
            dto.setDurationInWeeks(program.getDurationInWeeks());
            dto.setFee(program.getFee());
            dto.setTherapistId(program.getTherapist().getTherapistId());
            programDTOS.add(dto);
        }
        return programDTOS;
    }
}
