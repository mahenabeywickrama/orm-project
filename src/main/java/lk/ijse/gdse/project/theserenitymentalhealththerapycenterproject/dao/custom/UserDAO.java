package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.custom;

import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.CrudDAO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.User;

import java.util.Optional;

public interface UserDAO extends CrudDAO<User, String> {
    Optional<User> findByUsername(String username);
}
