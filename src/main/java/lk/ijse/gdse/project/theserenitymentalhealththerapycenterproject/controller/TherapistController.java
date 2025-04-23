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
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom.TherapistBO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.TherapistDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.tm.TherapistTM;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class TherapistController implements Initializable {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<TherapistTM, String> colContact;

    @FXML
    private TableColumn<TherapistTM, String> colId;

    @FXML
    private TableColumn<TherapistTM, String> colName;

    @FXML
    private TableColumn<TherapistTM, String> colSpecialization;

    @FXML
    private Label lblId;

    @FXML
    private TableView<TherapistTM> tblTherapist;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtSpecialization;

    TherapistBO therapistBO = BOFactory.getInstance().getBO(BOTypes.THERAPIST);

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
        colId.setCellValueFactory(new PropertyValueFactory<>("therapistId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSpecialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
    }

    private void refreshPage() {
        loadTable();
        setNextId();
        clearFields();
    }

    private void loadTable() {
        try {
            List<TherapistDTO> therapists = therapistBO.getTherapists();
            ObservableList<TherapistTM> therapistTMS = FXCollections.observableArrayList();

            for (TherapistDTO therapistDTO : therapists) {
                TherapistTM therapistTM = new TherapistTM(
                        therapistDTO.getTherapistId(),
                        therapistDTO.getName(),
                        therapistDTO.getSpecialization(),
                        therapistDTO.getContactNumber()
                );
                therapistTMS.add(therapistTM);
            }
            tblTherapist.setItems(therapistTMS);
        } catch (Exception e) {
            showErrorAlert(e.getMessage());
        }
    }

    private void setNextId() {
        String nextId = therapistBO.getNextTherapistId();
        lblId.setText(nextId);
    }

    private void clearFields() {
        txtContact.clear();
        txtSpecialization.clear();
        txtName.clear();

        btnSave.setDisable(false);
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
    }

    private boolean validateInputs() {
        boolean isValid = true;

        resetFieldStyles();

        if (txtName.getText().isEmpty()) {
            txtName.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (txtContact.getText().isEmpty() ||
                !txtContact.getText().matches("\\d{10,15}")) {
            txtContact.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (txtSpecialization.getText().isEmpty()) {
            txtSpecialization.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (!isValid) {
            showErrorAlert("Please enter valid inputs.");
        }

        return isValid;
    }

    private void resetFieldStyles() {
        txtName.setStyle(null);
        txtSpecialization.setStyle(null);
        txtContact.setStyle(null);
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

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this therapist?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get() == ButtonType.YES) {
                boolean isDeleted = therapistBO.deleteTherapist(id);
                if (isDeleted) {
                    showSuccessAlert("Therapist deleted successfully.");
                    refreshPage();
                } else {
                    showErrorAlert("Failed to delete therapist.");
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
            TherapistDTO dto = new TherapistDTO(
                    lblId.getText(),
                    txtName.getText(),
                    txtSpecialization.getText(),
                    txtContact.getText()
            );

            boolean isSaved = therapistBO.saveTherapist(dto);
            if (isSaved) {
                showSuccessAlert("Therapist saved successfully.");
                refreshPage();
            } else {
                showErrorAlert("Failed to save therapist.");
            }

        } catch (Exception e) {
            showErrorAlert(e.getMessage());
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (!validateInputs()) return;

        try {
            TherapistDTO dto = new TherapistDTO(
                    lblId.getText(),
                    txtName.getText(),
                    txtSpecialization.getText(),
                    txtContact.getText()
            );

            boolean isUpdated = therapistBO.updateTherapist(dto);
            if (isUpdated) {
                showSuccessAlert("Therapist updated successfully.");
                refreshPage();
            } else {
                showErrorAlert("Failed to update therapist.");
            }

        } catch (Exception e) {
            showErrorAlert(e.getMessage());
        }
    }

    @FXML
    void onClickTable(MouseEvent event) {
        TherapistTM selected = tblTherapist.getSelectionModel().getSelectedItem();
        if (selected != null) {
            lblId.setText(selected.getTherapistId());
            txtName.setText(selected.getName());
            txtSpecialization.setText(selected.getSpecialization());
            txtContact.setText(selected.getContactNumber());

            btnSave.setDisable(true);
            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
        }
    }
}
