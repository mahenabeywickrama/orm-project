package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom;

import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.SuperBO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.UserDTO;

import java.util.List;

public interface UserBO extends SuperBO {
    boolean saveUser(UserDTO dto);
    boolean updateUser(UserDTO dto);
    boolean deleteUser(String id);
    String getNextUserID();
    List<UserDTO> getUsers();
    UserDTO getUser(String id);
    boolean validateLogin(String username, String rawPassword);
    UserDTO getUserByUsername(String username);
}
