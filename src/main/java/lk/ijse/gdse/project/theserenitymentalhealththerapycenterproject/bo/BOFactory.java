package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo;

import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom.impl.PatientBOImpl;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom.impl.TherapistBOImpl;
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
            case PROGRAM -> null;
            case THERAPIST -> (T) new TherapistBOImpl();
            case USER -> null;
            case ENROLLMENT -> null;
            case SESSION -> null;
            case PAYMENT -> null;
        };
    }
}
