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
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom.PatientBO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.PatientDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.TherapyProgramDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.tm.PatientProgramTM;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.tm.PatientTM;

import java.net.URL;
import java.util.*;

public class PatientController implements Initializable {

    @FXML
    private Button btnAllPrograms;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnPrograms;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<PatientTM, String> colEmail;

    @FXML
    private TableColumn<PatientTM, String> colId;

    @FXML
    private TableColumn<PatientTM, String> colMedical;

    @FXML
    private TableColumn<PatientTM, String> colName;

    @FXML
    private TableColumn<PatientTM, String> colPhone;

    @FXML
    private TableColumn<PatientProgramTM, String> colProgramId;

    @FXML
    private TableColumn<PatientProgramTM, String> colProgramName;

    @FXML
    private Label lblId;

    @FXML
    private TableView<PatientTM> tblPatient;

    @FXML
    private TableView<PatientProgramTM> tblProgram;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtMedical;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPhone;

    private PatientBO patientBO = BOFactory.getInstance().getBO(BOTypes.PATIENT);

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
        colId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colMedical.setCellValueFactory(new PropertyValueFactory<>("medicalHistory"));

        colProgramId.setCellValueFactory(new PropertyValueFactory<>("pid"));
        colProgramName.setCellValueFactory(new PropertyValueFactory<>("programName"));
    }

    private void refreshPage() {
        loadTable();
        setNextId();
        clearFields();
        highlightFullyEnrolledPatients();
    }

    private void loadTable() {
        try {
            List<PatientDTO> patients = patientBO.getPatients();
            ObservableList<PatientTM> patientTMS = FXCollections.observableArrayList();

            for (PatientDTO patientDTO : patients) {
                PatientTM patientTM = new PatientTM(
                        patientDTO.getPatientId(),
                        patientDTO.getFullName(),
                        patientDTO.getEmail(),
                        patientDTO.getPhoneNumber(),
                        patientDTO.getMedicalHistory()
                );
                patientTMS.add(patientTM);
            }
            tblPatient.setItems(patientTMS);
        } catch (Exception e) {
            showErrorAlert(e.getMessage());
        }
    }

    private void setNextId() {
        String nextId = patientBO.getNextPatientId();
        lblId.setText(nextId);
    }

    private void clearFields() {
        txtEmail.clear();
        txtMedical.clear();
        txtName.clear();
        txtPhone.clear();

        tblProgram.getItems().clear();

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

        if (txtEmail.getText().isEmpty() ||
                !txtEmail.getText().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            txtEmail.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (txtPhone.getText().isEmpty() ||
                !txtPhone.getText().matches("\\d{10,15}")) {
            txtPhone.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (txtMedical.getText().isEmpty()) {
            txtMedical.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (!isValid) {
            showErrorAlert("Please enter valid inputs.");
        }

        return isValid;
    }

    private void resetFieldStyles() {
        txtName.setStyle(null);
        txtEmail.setStyle(null);
        txtPhone.setStyle(null);
        txtMedical.setStyle(null);
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

    private void highlightFullyEnrolledPatients() {
        try {
            List<String> fullyEnrolledPatientIds = patientBO.getPatientIdsEnrolledInAllPrograms();

            tblPatient.setRowFactory(tableView -> createRow(fullyEnrolledPatientIds));
        } catch (Exception e) {
            showErrorAlert("Failed to highlight patients: " + e.getMessage());
        }
    }

    private TableRow<PatientTM> createRow(List<String> fullyEnrolledPatientIds) {
        TableRow<PatientTM> row = new TableRow<>();

        row.itemProperty().addListener((obs, oldItem, newItem) -> {
            if (newItem == null) {
                row.setStyle("");
            } else if (fullyEnrolledPatientIds.contains(newItem.getPatientId())) {
                row.setStyle("-fx-background-color: #ccffcc;");
            } else {
                row.setStyle("");
            }
        });

        return row;
    }

    private void loadProgramsForPatient(String patientId) {
        try {

            List<TherapyProgramDTO> programs = patientBO.getProgramsForPatient(patientId);

            ObservableList<PatientProgramTM> items = FXCollections.observableArrayList();
            for (TherapyProgramDTO dto : programs) {
                items.add(new PatientProgramTM(
                        dto.getProgramId(),
                        dto.getName()
                ));
            }
            tblProgram.setItems(items);

        } catch (Exception e) {
            showErrorAlert("Failed to load programs: " + e.getMessage());
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        if (!validateInputs()) return;

        try {
            String id = lblId.getText();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this patient?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get() == ButtonType.YES) {
                boolean isDeleted = patientBO.deletePatient(id);
                if (isDeleted) {
                    showSuccessAlert("Patient deleted successfully.");
                    refreshPage();
                } else {
                    showErrorAlert("Failed to delete patient.");
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
            PatientDTO dto = new PatientDTO(
                    lblId.getText(),
                    txtName.getText(),
                    txtEmail.getText(),
                    txtPhone.getText(),
                    txtMedical.getText()
            );

            boolean isSaved = patientBO.savePatient(dto);
            if (isSaved) {
                showSuccessAlert("Patient saved successfully.");
                refreshPage();
            } else {
                showErrorAlert("Failed to save patient.");
            }

        } catch (Exception e) {
            showErrorAlert(e.getMessage());
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (!validateInputs()) return;

        try {
            PatientDTO dto = new PatientDTO(
                    lblId.getText(),
                    txtName.getText(),
                    txtEmail.getText(),
                    txtPhone.getText(),
                    txtMedical.getText()
            );

            boolean isUpdated = patientBO.updatePatient(dto);
            if (isUpdated) {
                showSuccessAlert("Patient updated successfully.");
                refreshPage();
            } else {
                showErrorAlert("Failed to update patient.");
            }

        } catch (Exception e) {
            showErrorAlert(e.getMessage());
        }
    }

    @FXML
    void onClickTable(MouseEvent event) {
        PatientTM selected = tblPatient.getSelectionModel().getSelectedItem();
        if (selected != null) {
            lblId.setText(selected.getPatientId());
            txtName.setText(selected.getFullName());
            txtEmail.setText(selected.getEmail());
            txtPhone.setText(selected.getPhoneNumber());
            txtMedical.setText(selected.getMedicalHistory());

            loadProgramsForPatient(selected.getPatientId());

            btnSave.setDisable(true);
            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
        }
    }
}
