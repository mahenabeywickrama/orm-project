package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.custom.impl;

import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.config.FactoryConfiguration;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.custom.TherapyProgramDAO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.Therapist;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.TherapyProgram;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class TherapyProgramDAOImpl implements TherapyProgramDAO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public boolean save(TherapyProgram entity) {
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
    public boolean update(TherapyProgram entity) {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            TherapyProgram therapyProgram = session.get(TherapyProgram.class, entity.getProgramId());
            therapyProgram.setProgramId(entity.getProgramId());
            therapyProgram.setName(entity.getName());
            therapyProgram.setFee(entity.getFee());
            therapyProgram.setTherapist(entity.getTherapist());

            session.merge(therapyProgram);
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
            TherapyProgram entity = session.get(TherapyProgram.class, s);
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
    public Optional<TherapyProgram> findById(String s) {
        Session session = factoryConfiguration.getSession();
        TherapyProgram therapyProgram = session.get(TherapyProgram.class, s);
        session.close();

        if (therapyProgram == null) {
            return Optional.empty();
        }
        return Optional.of(therapyProgram);
    }

    @Override
    public List<TherapyProgram> getAll() {
        Session session = factoryConfiguration.getSession();
        Query<TherapyProgram> query = session.createQuery("from TherapyProgram", TherapyProgram.class);
        return query.list();
    }

    @Override
    public Optional<String> getLastId() {
        Session session = factoryConfiguration.getSession();

        String lastId = session
                .createQuery("SELECT p.programId FROM TherapyProgram p ORDER BY p.programId DESC", String.class)
                .setMaxResults(1)
                .uniqueResult();
        return Optional.ofNullable(lastId);
    }
}
