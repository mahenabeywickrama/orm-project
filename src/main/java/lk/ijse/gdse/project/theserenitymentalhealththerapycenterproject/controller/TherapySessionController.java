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
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom.TherapySessionBO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.PatientDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.TherapistDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.TherapyProgramDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.TherapySessionDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.tm.PatientTM;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.tm.TherapyProgramTM;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.tm.TherapySessionTM;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.TherapySession;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class TherapySessionController implements Initializable {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private ComboBox<String> cmbPatient;

    @FXML
    private ComboBox<String> cmbProgram;

    @FXML
    private TableColumn<TherapySessionTM, LocalDate> colDate;

    @FXML
    private TableColumn<TherapySessionTM, String> colId;

    @FXML
    private TableColumn<TherapySessionTM, String> colPatient;

    @FXML
    private TableColumn<TherapySessionTM, String> colProgram;

    @FXML
    private TableColumn<TherapySessionTM, String> colTherapist;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label lblId;

    @FXML
    private Label lblPatientName;

    @FXML
    private Label lblProgramName;

    @FXML
    private Label lblTherapist;

    @FXML
    private Label lblTherapistName;

    @FXML
    private TableView<TherapySessionTM> tblSession;

    private TherapySessionBO therapySessionBO = BOFactory.getInstance().getBO(BOTypes.SESSION);

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
        colId.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("sessionDate"));
        colPatient.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colTherapist.setCellValueFactory(new PropertyValueFactory<>("therapistId"));
        colProgram.setCellValueFactory(new PropertyValueFactory<>("programId"));
    }

    private void refreshPage() {
        loadTable();
        setNextId();
        clearFields();
        loadPatientCmb();
        loadProgramCmb();
    }

    private void loadTable() {
        try {
            List<TherapySessionDTO> therapySessionDTOS = therapySessionBO.getSessions();
            ObservableList<TherapySessionTM> therapySessionTMS = FXCollections.observableArrayList();

            for (TherapySessionDTO therapySessionDTO : therapySessionDTOS) {
                TherapySessionTM therapySessionTM = new TherapySessionTM(
                        therapySessionDTO.getSessionId(),
                        therapySessionDTO.getSessionDate(),
                        therapySessionDTO.getPatientId(),
                        therapySessionDTO.getTherapistId(),
                        therapySessionDTO.getProgramId()
                );
                therapySessionTMS.add(therapySessionTM);
            }
            tblSession.setItems(therapySessionTMS);
        } catch (Exception e) {
            showErrorAlert(e.getMessage());
        }
    }

    private void setNextId() {
        String nextId = therapySessionBO.getNextSessionID();
        lblId.setText(nextId);
    }

    private void clearFields() {
        datePicker.setValue(null);
        cmbPatient.setValue(null);
        cmbProgram.setValue(null);

        lblTherapist.setText("");
        lblTherapistName.setText("");
        lblPatientName.setText("");
        lblProgramName.setText("");

        applyBlockedDates(new ArrayList<>());

        btnSave.setDisable(false);
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
    }

    private boolean validateInputs() {
        boolean isValid = true;

        resetFieldStyles();

        if (datePicker.getValue() == null) {
            datePicker.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (!isValid) {
            showErrorAlert("Please enter valid date.");
        }

        return isValid;
    }

    private void resetFieldStyles() {
        datePicker.setStyle(null);
    }

    private void loadPatientCmb() {
        List<PatientDTO> patientDTOS = therapySessionBO.getPatients();
        ObservableList<String> patientIds = FXCollections.observableArrayList();

        for (PatientDTO patientDTO : patientDTOS) {
            patientIds.add(patientDTO.getPatientId());
        }

        cmbPatient.setItems(patientIds);
    }

    private void loadProgramCmb() {
        List<TherapyProgramDTO> therapyProgramDTOS = therapySessionBO.getPrograms();
        ObservableList<String> programIds = FXCollections.observableArrayList();

        for (TherapyProgramDTO therapyProgramDTO : therapyProgramDTOS) {
            programIds.add(therapyProgramDTO.getProgramId());
        }

        cmbProgram.setItems(programIds);
    }

    private void checkAvailability(String therapistId) {
        List<TherapySessionDTO> sessionsFromTherapist = therapySessionBO.getSessionsFromTherapist(therapistId);
        List<LocalDate> blockedDates = new ArrayList<>();

        for (TherapySessionDTO dto : sessionsFromTherapist) {
            blockedDates.add(dto.getSessionDate());
        }

        applyBlockedDates(blockedDates);
    }

    private void applyBlockedDates(List<LocalDate> blockedDates) {
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (blockedDates.contains(date)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffdddd;");
                }
            }
        });
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

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this therapy session?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get() == ButtonType.YES) {
                boolean isDeleted = therapySessionBO.deleteSession(id);
                if (isDeleted) {
                    showSuccessAlert("Therapy session deleted successfully.");
                    refreshPage();
                } else {
                    showErrorAlert("Failed to delete therapy session.");
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
            TherapySessionDTO dto = new TherapySessionDTO(
                    lblId.getText(),
                    datePicker.getValue(),
                    cmbPatient.getValue(),
                    lblTherapist.getText(),
                    cmbProgram.getValue()
            );

            boolean isSaved = therapySessionBO.saveSession(dto);
            if (isSaved) {
                showSuccessAlert("Therapy session saved successfully.");
                refreshPage();
            } else {
                showErrorAlert("Failed to save therapy session.");
            }

        } catch (Exception e) {
            showErrorAlert(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (!validateInputs()) return;

        try {
            TherapySessionDTO dto = new TherapySessionDTO(
                    lblId.getText(),
                    datePicker.getValue(),
                    cmbPatient.getValue(),
                    lblTherapist.getText(),
                    cmbProgram.getValue()
            );

            boolean isUpdated = therapySessionBO.updateSession(dto);
            if (isUpdated) {
                showSuccessAlert("Therapy session updated successfully.");
                refreshPage();
            } else {
                showErrorAlert("Failed to update therapy session.");
            }

        } catch (Exception e) {
            showErrorAlert(e.getMessage());
        }
    }

    @FXML
    void cmbPatientOnAction(ActionEvent event) {
        if (cmbPatient.getValue() == null) return;

        PatientDTO patientDTO = therapySessionBO.getPatient(cmbPatient.getValue());

        if (patientDTO != null) {
            lblPatientName.setText(patientDTO.getFullName());
        }
    }

    @FXML
    void cmbProgramOnAction(ActionEvent event) {
        if (cmbProgram.getValue() == null) return;

        TherapyProgramDTO programDTO = therapySessionBO.getProgram(cmbProgram.getValue());

        if (programDTO != null) {
            lblProgramName.setText(programDTO.getName());
            lblTherapist.setText(programDTO.getTherapistId());

            TherapistDTO therapistDTO = therapySessionBO.getTherapist(programDTO.getTherapistId());
            if (therapistDTO != null) {
                lblTherapistName.setText(therapistDTO.getName());
                checkAvailability(therapistDTO.getTherapistId());
            }
        }
    }

    @FXML
    void onClickTable(MouseEvent event) {
        TherapySessionTM selected = tblSession.getSelectionModel().getSelectedItem();
        if (selected != null) {
            lblId.setText(selected.getSessionId());
            datePicker.setValue(selected.getSessionDate());
            cmbPatient.setValue(selected.getPatientId());
            cmbProgram.setValue(selected.getProgramId());

            PatientDTO patientDTO = therapySessionBO.getPatient(selected.getPatientId());
            lblPatientName.setText(patientDTO.getFullName());

            TherapyProgramDTO therapyProgramDTO = therapySessionBO.getProgram(selected.getProgramId());
            lblProgramName.setText(therapyProgramDTO.getName());

            lblTherapist.setText(therapyProgramDTO.getTherapistId());
            TherapistDTO therapistDTO = therapySessionBO.getTherapist(selected.getTherapistId());
            lblTherapistName.setText(therapistDTO.getName());
            checkAvailability(therapistDTO.getTherapistId());

            btnSave.setDisable(true);
            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
        }
    }
}
