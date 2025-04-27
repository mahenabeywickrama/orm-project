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
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom.TherapyProgramBO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.TherapistDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.TherapyProgramDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.tm.TherapistTM;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.tm.TherapyProgramTM;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.TherapyProgram;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class TherapyProgramController implements Initializable {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private ComboBox<String> cmbTherapist;

    @FXML
    private TableColumn<TherapyProgramTM, Integer> colDuration;

    @FXML
    private TableColumn<TherapyProgramTM, Double> colFee;

    @FXML
    private TableColumn<TherapyProgramTM, String> colId;

    @FXML
    private TableColumn<TherapyProgramTM, String> colName;

    @FXML
    private TableColumn<TherapyProgramTM, String> colTherapist;

    @FXML
    private Label lblId;

    @FXML
    private Label lblTherapistName;

    @FXML
    private TableView<TherapyProgramTM> tblProgram;

    @FXML
    private TextField txtDuration;

    @FXML
    private TextField txtFee;

    @FXML
    private TextField txtName;

    private TherapyProgramBO therapyProgramBO = BOFactory.getInstance().getBO(BOTypes.PROGRAM);

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
        colId.setCellValueFactory(new PropertyValueFactory<>("programId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("durationInWeeks"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("fee"));
        colTherapist.setCellValueFactory(new PropertyValueFactory<>("therapistId"));
    }

    private void refreshPage() {
        loadTable();
        setNextId();
        clearFields();
        loadCmb();
    }

    private void loadTable() {
        try {
            List<TherapyProgramDTO> therapyProgramDTOs = therapyProgramBO.getPrograms();
            ObservableList<TherapyProgramTM> therapyProgramTMS = FXCollections.observableArrayList();

            for (TherapyProgramDTO therapyProgramDTO : therapyProgramDTOs) {
                TherapyProgramTM therapyProgramTM = new TherapyProgramTM(
                        therapyProgramDTO.getProgramId(),
                        therapyProgramDTO.getName(),
                        therapyProgramDTO.getDurationInWeeks(),
                        therapyProgramDTO.getFee(),
                        therapyProgramDTO.getTherapistId()
                );
                therapyProgramTMS.add(therapyProgramTM);
            }
            tblProgram.setItems(therapyProgramTMS);
        } catch (Exception e) {
            showErrorAlert(e.getMessage());
        }
    }

    private void setNextId() {
        String nextId = therapyProgramBO.getNextProgramId();
        lblId.setText(nextId);
    }

    private void clearFields() {
        txtName.clear();
        txtDuration.clear();
        txtFee.clear();

        cmbTherapist.getItems().clear();
        lblTherapistName.setText("");

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

        if (txtDuration.getText().isEmpty() || !txtDuration.getText().matches("\\d+")) {
            txtDuration.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (txtFee.getText().isEmpty() || !txtFee.getText().matches("\\d+(\\.\\d{1,2})?")) {
            txtFee.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (!isValid) {
            showErrorAlert("Please enter valid program details.");
        }

        return isValid;
    }

    private void resetFieldStyles() {
        txtName.setStyle(null);
        txtDuration.setStyle(null);
        txtFee.setStyle(null);
    }

    private void loadCmb() {
        List<TherapistDTO> therapists = therapyProgramBO.getTherapists();
        ObservableList<String> therapistIds = FXCollections.observableArrayList();

        for (TherapistDTO therapistDTO : therapists) {
            therapistIds.add(therapistDTO.getTherapistId());
        }

        cmbTherapist.setItems(therapistIds);
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

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this therapy program?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get() == ButtonType.YES) {
                boolean isDeleted = therapyProgramBO.deleteProgram(id);
                if (isDeleted) {
                    showSuccessAlert("Therapy program deleted successfully.");
                    refreshPage();
                } else {
                    showErrorAlert("Failed to delete therapy program.");
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
            TherapyProgramDTO dto = new TherapyProgramDTO(
                    lblId.getText(),
                    txtName.getText(),
                    Integer.parseInt(txtDuration.getText()),
                    Double.parseDouble(txtFee.getText()),
                    cmbTherapist.getValue()
            );

            boolean isSaved = therapyProgramBO.saveProgram(dto);
            if (isSaved) {
                showSuccessAlert("Therapy program saved successfully.");
                refreshPage();
            } else {
                showErrorAlert("Failed to save therapy program.");
            }

        } catch (Exception e) {
            showErrorAlert(e.getMessage());
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (!validateInputs()) return;

        try {
            TherapyProgramDTO dto = new TherapyProgramDTO(
                    lblId.getText(),
                    txtName.getText(),
                    Integer.parseInt(txtDuration.getText()),
                    Double.parseDouble(txtFee.getText()),
                    cmbTherapist.getValue()
            );

            boolean isUpdated = therapyProgramBO.updateProgram(dto);
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
    void cmbTherapistOnAction(ActionEvent event) {
        if (cmbTherapist.getValue() == null) return;

        TherapistDTO therapistDTO = therapyProgramBO.getTherapist(cmbTherapist.getValue());

        if (therapistDTO != null) {
            lblTherapistName.setText(therapistDTO.getName());
        }
    }

    @FXML
    void onClickTable(MouseEvent event) {
        TherapyProgramTM selected = tblProgram.getSelectionModel().getSelectedItem();
        if (selected != null) {
            lblId.setText(selected.getProgramId());
            txtName.setText(selected.getName());
            txtDuration.setText(String.valueOf(selected.getDurationInWeeks()));
            txtFee.setText(String.valueOf(selected.getFee()));
            cmbTherapist.setValue(selected.getTherapistId());
            TherapistDTO therapistDTO = therapyProgramBO.getTherapist(selected.getTherapistId());
            lblTherapistName.setText(therapistDTO.getName());

            btnSave.setDisable(true);
            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
        }
    }
}
