package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.custom.impl;

import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.config.FactoryConfiguration;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.custom.TherapySessionDAO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.TherapyProgram;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.TherapySession;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class TherapySessionDAOImpl implements TherapySessionDAO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public boolean save(TherapySession entity) {
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
    public boolean update(TherapySession entity) {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.merge(entity);
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
            TherapySession entity = session.get(TherapySession.class, s);
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
    public Optional<TherapySession> findById(String s) {
        Session session = factoryConfiguration.getSession();
        TherapySession therapySession = session.get(TherapySession.class, s);
        session.close();

        if (therapySession == null) {
            return Optional.empty();
        }
        return Optional.of(therapySession);
    }

    @Override
    public List<TherapySession> getAll() {
        Session session = factoryConfiguration.getSession();
        Query<TherapySession> query = session.createQuery("from TherapySession", TherapySession.class);
        return query.list();
    }

    @Override
    public Optional<String> getLastId() {
        Session session = factoryConfiguration.getSession();

        String lastId = session
                .createQuery("SELECT s.id FROM TherapySession s ORDER BY s.id DESC", String.class)
                .setMaxResults(1)
                .uniqueResult();
        return Optional.ofNullable(lastId);
    }

    @Override
    public List<TherapySession> findSessionsByTherapistId(String therapistId) {
        Session session = factoryConfiguration.getSession();
        List<TherapySession> sessions = session
                .createQuery("FROM TherapySession s WHERE s.therapist.therapistId = :id", TherapySession.class)
                .setParameter("id", therapistId)
                .list();
        session.close();
        return sessions;
    }

}
