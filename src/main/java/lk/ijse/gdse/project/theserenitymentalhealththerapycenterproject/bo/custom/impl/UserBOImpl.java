package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom.impl;

import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom.UserBO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.DAOFactory;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.DAOTypes;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.custom.UserDAO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.UserDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.User;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.util.CurrentUser;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserBOImpl implements UserBO {
    UserDAO userDAO = DAOFactory.getInstance().getDAO(DAOTypes.USER);

    @Override
    public boolean saveUser(UserDTO dto) {
        Optional<User> byUsername = userDAO.findByUsername(dto.getUsername());
        if (byUsername.isPresent()) return false;

        String hashedPassword = BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt());

        User user = new User(
                dto.getUserId(),
                dto.getUsername(),
                hashedPassword,
                dto.getRole(),
                null
        );

        return userDAO.save(user);
    }

    @Override
    public boolean updateUser(UserDTO dto) {
        if (!dto.getUsername().equals(CurrentUser.getCurrentUser().getUsername())) {
            Optional<User> existingUser = userDAO.findByUsername(dto.getUsername());
            if (existingUser.isPresent()) {
                return false;
            }
        }

        String password = dto.getPassword();

        if (!password.startsWith("$2a$") && !password.startsWith("$2b$")) {
            password = BCrypt.hashpw(password, BCrypt.gensalt());
        }

        User user = new User(
                dto.getUserId(),
                dto.getUsername(),
                password,
                dto.getRole(),
                null
        );

        return userDAO.update(user);
    }

    @Override
    public boolean deleteUser(String id) {
        return userDAO.delete(id);
    }

    @Override
    public String getNextUserID() {
        Optional<String> lastTherapistId = userDAO.getLastId();

        if (lastTherapistId.isPresent()) {
            String lastID = lastTherapistId.get();
            int numericPart = Integer.parseInt(lastID.substring(1));
            numericPart++;
            return String.format("U%03d", numericPart);
        } else {
            return "U001";
        }
    }

    @Override
    public List<UserDTO> getUsers() {
        List<User> users = userDAO.getAll();
        List<UserDTO> userDTOs = new ArrayList<>();

        for (User user : users) {
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(user.getUserId());
            userDTO.setUsername(user.getUsername());
            userDTO.setPassword(user.getPassword());
            userDTO.setRole(user.getRole());
            userDTOs.add(userDTO);
        }

        return userDTOs;
    }

    @Override
    public UserDTO getUser(String id) {
        Optional<User> byId = userDAO.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(user.getUserId());
            userDTO.setUsername(user.getUsername());
            userDTO.setPassword(user.getPassword());
            userDTO.setRole(user.getRole());
            return userDTO;
        }
        return null;
    }

    @Override
    public boolean validateLogin(String username, String rawPassword) {
        Optional<User> userOpt = userDAO.findByUsername(username);
        if (userOpt.isEmpty()) return false;

        return BCrypt.checkpw(rawPassword, userOpt.get().getPassword());
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        Optional<User> byUsername = userDAO.findByUsername(username);
        if (byUsername.isPresent()) {
            User user = byUsername.get();
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(user.getUserId());
            userDTO.setUsername(user.getUsername());
            userDTO.setPassword(user.getPassword());
            userDTO.setRole(user.getRole());
            return userDTO;
        }
        return null;
    }
}
