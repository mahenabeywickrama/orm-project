package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.custom.impl;

import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.config.FactoryConfiguration;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.custom.PatientDAO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.Patient;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.TherapyProgram;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class PatientDAOImpl implements PatientDAO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public boolean save(Patient entity) {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.persist(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean update(Patient entity) {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            Patient patient = session.get(Patient.class, entity.getPatientId());
            patient.setFullName(entity.getFullName());
            patient.setEmail(entity.getEmail());
            patient.setPhoneNumber(entity.getPhoneNumber());
            patient.setMedicalHistory(entity.getMedicalHistory());

            session.merge(patient);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean delete(String s) {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            Patient entity = session.get(Patient.class, s);
            session.remove(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<Patient> findById(String s) {
        Session session = factoryConfiguration.getSession();
        Patient patient = session.get(Patient.class, s);
        session.close();

        if (patient == null) {
            return Optional.empty();
        }
        return Optional.of(patient);
    }

    @Override
    public List<Patient> getAll() {
        Session session = factoryConfiguration.getSession();
        Query<Patient> query = session.createQuery("from Patient", Patient.class);
        return query.list();
    }

    @Override
    public Optional<String> getLastId() {
        Session session = factoryConfiguration.getSession();

        String lastId = session
                .createQuery("SELECT p.id FROM Patient p ORDER BY p.id DESC", String.class)
                .setMaxResults(1)
                .uniqueResult();
        return Optional.ofNullable(lastId);
    }

    @Override
    public List<Patient> getPatientsEnrolledInAllPrograms() {
        try (Session session = factoryConfiguration.getSession()) {
            Long totalPrograms = session.createQuery("SELECT COUNT(tp.programId) FROM TherapyProgram tp", Long.class)
                    .getSingleResult();

            return session.createQuery(
                            "SELECT p FROM Patient p " +
                                    "JOIN p.enrollments e " +
                                    "JOIN e.therapyPrograms tp " +
                                    "GROUP BY p.patientId " +
                                    "HAVING COUNT(DISTINCT tp.programId) = :programCount",
                            Patient.class
                    ).setParameter("programCount", totalPrograms)
                    .getResultList();
        }
    }

    @Override
    public List<TherapyProgram> getProgramsByPatientId(String patientId) {
        try (Session session = factoryConfiguration.getSession()) {
            return session.createQuery(
                            "SELECT tp " +
                                    "FROM Enrollment e " +
                                    "JOIN e.therapyPrograms tp " +
                                    "WHERE e.patient.patientId = :pid",
                            TherapyProgram.class)
                    .setParameter("pid", patientId)
                    .getResultList();
        }
    }


}
