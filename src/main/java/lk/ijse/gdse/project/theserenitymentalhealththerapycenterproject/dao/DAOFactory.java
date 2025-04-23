package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao;

import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.custom.impl.*;

public class DAOFactory {
    private static DAOFactory daoFactory;

    private DAOFactory() {}

    public static DAOFactory getInstance() {
        if (daoFactory == null) {
            daoFactory = new DAOFactory();
        }
        return daoFactory;
    }

    @SuppressWarnings("unchecked")

    public <T extends SuperDAO> T getDAO(DAOTypes type) {
        return switch (type) {
            case PATIENT -> (T) new PatientDAOImpl();
            case PROGRAM -> (T) new TherapyProgramDAOImpl();
            case THERAPIST -> (T) new TherapistDAOImpl();
            case USER -> (T) new UserDAOImpl();
            case ENROLLMENT -> (T) new EnrollmentDAOImpl();
            case SESSION -> (T) new TherapySessionDAOImpl();
            case PAYMENT -> (T) new PaymentDAOImpl();
        };
    }
}
