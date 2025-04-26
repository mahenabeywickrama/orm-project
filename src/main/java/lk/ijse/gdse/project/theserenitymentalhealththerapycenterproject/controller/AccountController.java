package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.BOFactory;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.BOTypes;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom.UserBO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.UserDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.User;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.util.CurrentUser;
import org.mindrot.jbcrypt.BCrypt;

public class AccountController {

    @FXML
    private Button btnSave;

    @FXML
    private TextField txtConfirm;

    @FXML
    private TextField txtOld;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUsername;

    private final UserBO userBO = BOFactory.getInstance().getBO(BOTypes.USER);

    @FXML
    public void initialize() {
        if (CurrentUser.getCurrentUser() != null) {
            txtUsername.setText(CurrentUser.getCurrentUser().getUsername());
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String newUsername = txtUsername.getText().trim();
        String newPassword = txtPassword.getText().trim();
        String confirmPassword = txtConfirm.getText().trim();
        String oldPassword = txtOld.getText().trim();

        if (newUsername.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty() || oldPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation", "All fields must be filled.");
            return;
        }

        if (!BCrypt.checkpw(oldPassword, CurrentUser.getCurrentUser().getPassword())) {
            showAlert(Alert.AlertType.ERROR, "Authentication Failed", "Old password is incorrect.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Mismatch", "New password and confirm password do not match.");
            return;
        }

        UserDTO updatedUser = new UserDTO(
                CurrentUser.getCurrentUser().getUserId(),
                newUsername,
                newPassword,
                CurrentUser.getCurrentUser().getRole()
        );

        boolean isUpdated = userBO.updateUser(updatedUser);

        if (isUpdated) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Account details updated.");
            CurrentUser.setCurrentUser(updatedUser);
            clearFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update account.");
        }
    }

    private void clearFields() {
        txtOld.setText("");
        txtPassword.setText("");
        txtConfirm.setText("");
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
