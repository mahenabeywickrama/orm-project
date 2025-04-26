package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo;

import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom.impl.*;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.DAOTypes;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.SuperDAO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.custom.impl.*;

public class BOFactory {
    private static BOFactory boFactory;

    private BOFactory() {}

    public static BOFactory getInstance() {
        if (boFactory == null) {
            boFactory = new BOFactory();
        }
        return boFactory;
    }

    @SuppressWarnings("unchecked")

    public <T extends SuperBO> T getBO(BOTypes type) {
        return switch (type) {
            case PATIENT -> (T) new PatientBOImpl();
            case PROGRAM -> (T) new TherapyProgramBOImpl();
            case THERAPIST -> (T) new TherapistBOImpl();
            case USER -> (T) new UserBOImpl();
            case ENROLLMENT -> (T) new EnrollmentBOImpl();
            case SESSION -> (T) new TherapySessionBOImpl();
            case PAYMENT -> (T) new PaymentBOImpl();
        };
    }
}
