package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.custom.impl;

import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.config.FactoryConfiguration;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.custom.UserDAO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.TherapySession;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public boolean save(User entity) {
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
    public boolean update(User entity) {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            User user = session.get(User.class, entity.getUserId());
            user.setUsername(entity.getUsername());
            user.setPassword(entity.getPassword());
            user.setRole(entity.getRole());
            session.merge(user);
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
            User entity = session.get(User.class, s);
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
    public Optional<User> findById(String s) {
        Session session = factoryConfiguration.getSession();
        User user = session.get(User.class, s);
        session.close();

        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(user);
    }

    @Override
    public List<User> getAll() {
        Session session = factoryConfiguration.getSession();
        Query<User> query = session.createQuery("from User", User.class);
        return query.list();
    }

    @Override
    public Optional<String> getLastId() {
        Session session = factoryConfiguration.getSession();

        String lastId = session
                .createQuery("SELECT u.id FROM User u ORDER BY u.id DESC", String.class)
                .setMaxResults(1)
                .uniqueResult();
        return Optional.ofNullable(lastId);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Session session = factoryConfiguration.getSession();
        try {
            Query<User> query = session.createQuery("FROM User WHERE username = :username", User.class);
            query.setParameter("username", username);
            return query.uniqueResultOptional();
        } finally {
            session.close();
        }
    }

}
