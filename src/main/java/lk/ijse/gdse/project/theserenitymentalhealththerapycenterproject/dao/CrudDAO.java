package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao;

import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.SuperEntity;

import java.util.List;
import java.util.Optional;

public interface CrudDAO <T extends SuperEntity, ID> extends SuperDAO {
    boolean save(T entity);
    boolean update(T entity);
    boolean delete(ID id);
    Optional<T> findById(ID id);
    List<T> getAll();
    Optional<String> getLastId();
}
