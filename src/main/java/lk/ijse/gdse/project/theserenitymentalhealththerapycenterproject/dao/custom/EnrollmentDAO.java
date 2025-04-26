package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.custom;

import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.CrudDAO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.Enrollment;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.TherapyProgram;

import java.util.List;

public interface EnrollmentDAO extends CrudDAO<Enrollment, String> {
    List<TherapyProgram> getProgramsFromEnrollment(String enrollmentId);
}
