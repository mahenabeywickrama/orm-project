package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.BOFactory;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.BOTypes;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom.UserBO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.TherapistDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.TherapyProgramDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.UserDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.tm.TherapyProgramTM;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.tm.UserTM;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private ComboBox<String> cmbRole;

    @FXML
    private TableColumn<UserTM, String> colId;

    @FXML
    private TableColumn<UserTM, String> colPassword;

    @FXML
    private TableColumn<UserTM, String> colRole;

    @FXML
    private TableColumn<UserTM, String> colUsername;

    @FXML
    private Label lblId;

    @FXML
    private TableView<UserTM> tblUser;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUsername;

    private UserBO userBO = BOFactory.getInstance().getBO(BOTypes.USER);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValues();
        try {
            refreshPage();
        } catch (Exception e) {
            showErrorAlert(e.getMessage());
        }
    }

    private void setCellValues() {
        colId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
    }

    private void refreshPage() {
        loadTable();
        setNextId();
        clearFields();
        loadCmb();
    }

    private void loadTable() {
        try {
            List<UserDTO> users = userBO.getUsers();
            ObservableList<UserTM> userTMS = FXCollections.observableArrayList();

            for (UserDTO userDTO : users) {
                UserTM userTM = new UserTM(
                        userDTO.getUserId(),
                        userDTO.getUsername(),
                        "********",
                        userDTO.getRole()
                );
                userTMS.add(userTM);
            }
            tblUser.setItems(userTMS);
        } catch (Exception e) {
            showErrorAlert(e.getMessage());
        }
    }

    private void setNextId() {
        String nextId = userBO.getNextUserID();
        lblId.setText(nextId);
    }

    private void clearFields() {
        txtUsername.clear();
        txtPassword.clear();

        cmbRole.setValue(null);

        btnSave.setDisable(false);
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);

        txtPassword.setDisable(false);
    }

    private boolean validateInputs() {
        boolean isValid = true;

        resetFieldStyles();

        if (txtUsername.getText().isEmpty()) {
            txtUsername.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (txtPassword.getText().isEmpty()) {
            txtPassword.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (cmbRole.getValue() == null) {
            cmbRole.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (!isValid) {
            showErrorAlert("Please enter valid user details.");
        }

        return isValid;
    }

    private void resetFieldStyles() {
        txtUsername.setStyle(null);
        txtPassword.setStyle(null);
        cmbRole.setStyle(null);
    }

    private void loadCmb() {
        List<String> roles = new ArrayList<>();
        roles.add("ADMIN");
        roles.add("RECEPTIONIST");
        ObservableList<String> rolesCmb = FXCollections.observableArrayList();

        rolesCmb.addAll(roles);
        cmbRole.setItems(rolesCmb);
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Operation Completed");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Operation Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        if (!validateInputs()) return;

        try {
            String id = lblId.getText();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this user?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get() == ButtonType.YES) {
                boolean isDeleted = userBO.deleteUser(id);
                if (isDeleted) {
                    showSuccessAlert("User deleted successfully.");
                    refreshPage();
                } else {
                    showErrorAlert("Failed to delete user.");
                }
            }

        } catch (Exception e) {
            showErrorAlert(e.getMessage());
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        refreshPage();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!validateInputs()) return;

        try {
            UserDTO dto = new UserDTO(
                    lblId.getText(),
                    txtUsername.getText(),
                    txtPassword.getText(),
                    cmbRole.getValue()
            );

            boolean isSaved = userBO.saveUser(dto);
            if (isSaved) {
                showSuccessAlert("User saved successfully.");
                refreshPage();
            } else {
                showErrorAlert("Failed to save user.");
            }

        } catch (Exception e) {
            showErrorAlert(e.getMessage());
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (!validateInputs()) return;

        try {
            UserDTO dto = new UserDTO(
                    lblId.getText(),
                    txtUsername.getText(),
                    txtPassword.getText(),
                    cmbRole.getValue()
            );

            boolean isUpdated = userBO.updateUser(dto);
            if (isUpdated) {
                showSuccessAlert("Therapy program updated successfully.");
                refreshPage();
            } else {
                showErrorAlert("Failed to update therapy program.");
            }

        } catch (Exception e) {
            showErrorAlert(e.getMessage());
        }
    }

    @FXML
    void onClickTable(MouseEvent event) {
        UserTM selected = tblUser.getSelectionModel().getSelectedItem();
        if (selected != null) {
            lblId.setText(selected.getUserId());
            txtUsername.setText(selected.getUsername());
            txtPassword.setText(String.valueOf(selected.getPassword()));
            cmbRole.setValue(selected.getRole());

            btnSave.setDisable(true);
            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);

            txtPassword.setDisable(true);
        }
    }
}
