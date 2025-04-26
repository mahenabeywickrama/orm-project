package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.custom;

import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.CrudDAO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.TherapySession;

import java.util.List;

public interface TherapySessionDAO extends CrudDAO<TherapySession, String> {
    List<TherapySession> findSessionsByTherapistId(String therapistId);
}
