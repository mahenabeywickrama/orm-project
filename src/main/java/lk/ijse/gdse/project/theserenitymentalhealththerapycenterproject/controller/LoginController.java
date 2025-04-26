package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.BOFactory;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.BOTypes;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom.UserBO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.UserDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.util.CurrentUser;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Button btnLogin;

    @FXML
    private AnchorPane contentPane;

    @FXML
    private PasswordField passwordField;

    @FXML
    private RadioButton radioShow;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUsername;

    private UserBO userBO = BOFactory.getInstance().getBO(BOTypes.USER);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        checkForUsers();

        txtPassword.managedProperty().bind(radioShow.selectedProperty());
        txtPassword.visibleProperty().bind(radioShow.selectedProperty());

        passwordField.managedProperty().bind(radioShow.selectedProperty().not());
        passwordField.visibleProperty().bind(radioShow.selectedProperty().not());

        txtPassword.textProperty().bindBidirectional(passwordField.textProperty());
    }

    @FXML
    void btnLoginOnAction(ActionEvent event) {
        String username = txtUsername.getText();
        String password = radioShow.isSelected() ? txtPassword.getText() : passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Username and password must not be empty.");
            return;
        }

        try {
            boolean isValid = userBO.validateLogin(username, password);

            if (isValid) {
                UserDTO loggedInUser = userBO.getUserByUsername(username);
                CurrentUser.setCurrentUser(loggedInUser);
                Node node = FXMLLoader.load(getClass().getResource("/view/MainLayout.fxml"));
                contentPane.getChildren().setAll(node);
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Login Error", "An error occurred while trying to log in.");
        }
    }

    private void checkForUsers() {
        List<UserDTO> users = userBO.getUsers();
        if (users.isEmpty()) {
            UserDTO userDTO = new UserDTO(
                    userBO.getNextUserID(),
                    "admin",
                    "1234",
                    "ADMIN"
            );
            userBO.saveUser(userDTO);
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
