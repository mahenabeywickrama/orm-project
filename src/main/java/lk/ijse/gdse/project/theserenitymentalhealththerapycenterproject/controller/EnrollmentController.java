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
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom.EnrollmentBO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.EnrollmentDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.PatientDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.TherapyProgramDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.tm.EnrollmentTM;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.tm.ProgramCartTM;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class EnrollmentController implements Initializable {

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private ComboBox<String> cmbMethod;

    @FXML
    private ComboBox<String> cmbPatient;

    @FXML
    private ComboBox<String> cmbProgram;

    @FXML
    private TableColumn<ProgramCartTM, Button> colAction;

    @FXML
    private TableColumn<EnrollmentTM, Double> colCost;

    @FXML
    private TableColumn<EnrollmentTM, LocalDate> colDate;

    @FXML
    private TableColumn<ProgramCartTM, Double> colFee;

    @FXML
    private TableColumn<EnrollmentTM, String> colId;

    @FXML
    private TableColumn<EnrollmentTM, String> colPatient;

    @FXML
    private TableColumn<ProgramCartTM, String> colProgram;

    @FXML
    private TableColumn<ProgramCartTM, String> colProgramId;

    @FXML
    private TableColumn<EnrollmentTM, String> colStatus;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label lblId;

    @FXML
    private Label lblPatientName;

    @FXML
    private Label lblProgramName;

    @FXML
    private Label lblTotalCost;

    @FXML
    private TableView<EnrollmentTM> tblEnrollment;

    @FXML
    private TableView<ProgramCartTM> tblProgram;

    @FXML
    private TextField txtAmount;

    private EnrollmentBO enrollmentBO = BOFactory.getInstance().getBO(BOTypes.ENROLLMENT);

    private final ObservableList<ProgramCartTM> programCartTMS = FXCollections.observableArrayList();

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
        colId.setCellValueFactory(new PropertyValueFactory<>("enrollmentId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("enrollmentDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("enrollmentStatus"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        colPatient.setCellValueFactory(new PropertyValueFactory<>("patientId"));

        colProgramId.setCellValueFactory(new PropertyValueFactory<>("programId"));
        colProgram.setCellValueFactory(new PropertyValueFactory<>("programName"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("fee"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("remove"));

        tblProgram.setItems(programCartTMS);
    }

    private void refreshPage() {
        loadTable();
        setNextId();
        clearFields();
        loadPatientCmb();
        loadProgramCmb();
        loadMethodCmb();
    }

    private void loadTable() {
        try {
            List<EnrollmentDTO> enrollments = enrollmentBO.getEnrollments();
            ObservableList<EnrollmentTM> enrollmentTMS = FXCollections.observableArrayList();

            for (EnrollmentDTO enrollmentDTO : enrollments) {
                EnrollmentTM enrollmentTM = new EnrollmentTM(
                        enrollmentDTO.getEnrollmentId(),
                        enrollmentDTO.getEnrollmentDate(),
                        enrollmentDTO.getEnrollmentStatus(),
                        enrollmentDTO.getTotalCost(),
                        enrollmentDTO.getPatientId()
                );
                enrollmentTMS.add(enrollmentTM);
            }
            tblEnrollment.setItems(enrollmentTMS);
        } catch (Exception e) {
            showErrorAlert(e.getMessage());
        }
    }

    private void setNextId() {
        String nextEnrollmentID = enrollmentBO.getNextEnrollmentID();
        lblId.setText(nextEnrollmentID);
    }

    private void clearFields() {
        datePicker.setValue(null);
        cmbPatient.setValue(null);
        cmbProgram.setValue(null);

        lblTotalCost.setText(null);
        lblPatientName.setText(null);
        lblProgramName.setText(null);

        programCartTMS.clear();
        tblProgram.refresh();

        btnSave.setDisable(false);
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);

        cmbMethod.setValue(null);
        txtAmount.clear();
    }

    private boolean validateInputs() {
        boolean isValid = true;

        resetFieldStyles();

        if (datePicker.getValue() == null) {
            datePicker.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (cmbPatient.getValue() == null) {
            cmbPatient.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (cmbMethod.getValue() == null) {
            cmbMethod.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (!isValid) {
            showErrorAlert("Please enter valid inputs.");
        }

        return isValid;
    }

    private void resetFieldStyles() {
        datePicker.setStyle(null);
        cmbMethod.setStyle(null);
        cmbPatient.setStyle(null);
    }

    private void loadPatientCmb() {
        List<PatientDTO> patientDTOS = enrollmentBO.getPatients();
        ObservableList<String> patientIds = FXCollections.observableArrayList();

        for (PatientDTO patientDTO : patientDTOS) {
            patientIds.add(patientDTO.getPatientId());
        }

        cmbPatient.setItems(patientIds);
    }

    private void loadProgramCmb() {
        List<TherapyProgramDTO> therapyProgramDTOS = enrollmentBO.getTherapyPrograms();
        ObservableList<String> programIds = FXCollections.observableArrayList();

        for (TherapyProgramDTO therapyProgramDTO : therapyProgramDTOS) {
            programIds.add(therapyProgramDTO.getProgramId());
        }

        cmbProgram.setItems(programIds);
    }

    private void loadMethodCmb() {
        List<String> methods = new ArrayList<>();
        methods.add("CARD");
        methods.add("CASH");
        ObservableList<String> methodsCmb = FXCollections.observableArrayList();

        methodsCmb.addAll(methods);
        cmbMethod.setItems(methodsCmb);
    }

    public void calculateTotal(){
        double total = 0;

        for (ProgramCartTM programCartTM : programCartTMS) {
            total += programCartTM.getFee();
        }

        lblTotalCost.setText(String.format("%.2f", total));
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
    void btnAddOnAction(ActionEvent event) {
        String selectedId = cmbProgram.getValue();

        if (selectedId == null) {
            showErrorAlert("Please select a program!");
            return;
        }

        TherapyProgramDTO therapyProgramDTO = enrollmentBO.getTherapyProgram(selectedId);
        String programName = therapyProgramDTO.getName();
        double fee = therapyProgramDTO.getFee();

        for (ProgramCartTM programCartTM : programCartTMS) {
            if (programCartTM.getProgramId().equals(selectedId)) {
                showErrorAlert("Program already exists!");
                clearProgramSection();
                return;
            }
        }

        Button btn = new Button("remove");

        ProgramCartTM newProgramCartTM = new ProgramCartTM(
                selectedId,
                programName,
                fee,
                btn
        );

        btn.setOnAction(actionEvent -> {
            programCartTMS.remove(newProgramCartTM);
            tblProgram.refresh();
            calculateTotal();
        });

        programCartTMS.add(newProgramCartTM);

        clearProgramSection();
        calculateTotal();
    }

    private void clearProgramSection() {
        cmbProgram.setValue(null);
        lblProgramName.setText(null);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        if (!validateInputs()) return;

        String enrollmentId = lblId.getText();

        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this enrollment?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                boolean isDeleted = enrollmentBO.deleteEnrollment(enrollmentId);
                if (isDeleted) {
                    showSuccessAlert("Enrollment deleted successfully.");
                    refreshPage();
                } else {
                    showErrorAlert("Failed to delete enrollment.");
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

        if (programCartTMS.isEmpty()) {
            showErrorAlert("Please add at least one program to the enrollment.");
            return;
        }

        List<String> programIds = new ArrayList<>();
        for (ProgramCartTM programCartTM : programCartTMS) {
            programIds.add(programCartTM.getProgramId());
        }

        String status = "COMPLETED";

        double totalCost = Double.parseDouble(lblTotalCost.getText());
        double payedAmount = Double.parseDouble(txtAmount.getText());

        if (payedAmount < totalCost) {
            status = "UPFRONT";
        }

        try {
            EnrollmentDTO dto = new EnrollmentDTO(
                    lblId.getText(),
                    datePicker.getValue(),
                    "PENDING",
                    status,
                    Double.parseDouble(lblTotalCost.getText()),
                    cmbPatient.getValue(),
                    programIds,
                    Double.parseDouble(txtAmount.getText()),
                    cmbMethod.getValue()
            );

            boolean isSaved = enrollmentBO.saveEnrollment(dto);
            if (isSaved) {
                showSuccessAlert("Enrollment saved successfully.");
                refreshPage();
            } else {
                showErrorAlert("Failed to save enrollment.");
            }

        } catch (Exception e) {
            showErrorAlert(e.getMessage());
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (!validateInputs()) return;

        if (programCartTMS.isEmpty()) {
            showErrorAlert("Please add at least one program to the enrollment.");
            return;
        }

        List<String> programIds = new ArrayList<>();
        for (ProgramCartTM programCartTM : programCartTMS) {
            programIds.add(programCartTM.getProgramId());
        }

        String status = "COMPLETED";

        double totalCost = Double.parseDouble(lblTotalCost.getText());
        double payedAmount = Double.parseDouble(txtAmount.getText());

        if (payedAmount < totalCost) {
            status = "UPFRONT";
        }

        try {
            EnrollmentDTO dto = new EnrollmentDTO(
                    lblId.getText(),
                    datePicker.getValue(),
                    "PENDING",
                    status,
                    Double.parseDouble(lblTotalCost.getText()),
                    cmbPatient.getValue(),
                    programIds,
                    Double.parseDouble(txtAmount.getText()),
                    cmbMethod.getValue()
            );

            boolean isUpdated = enrollmentBO.updateEnrollment(dto);
            if (isUpdated) {
                showSuccessAlert("Enrollment updated successfully.");
                refreshPage();
            } else {
                showErrorAlert("Failed to update enrollment.");
            }

        } catch (Exception e) {
            showErrorAlert(e.getMessage());
        }
    }

    @FXML
    void cmbPatientOnAction(ActionEvent event) {
        if (cmbPatient.getValue() == null) return;

        PatientDTO patientDTO = enrollmentBO.getPatient(cmbPatient.getValue());

        if (patientDTO != null) {
            lblPatientName.setText(patientDTO.getFullName());
        }
    }

    @FXML
    void cmbProgramOnAction(ActionEvent event) {
        if (cmbProgram.getValue() == null) return;

        TherapyProgramDTO programDTO = enrollmentBO.getTherapyProgram(cmbProgram.getValue());

        if (programDTO != null) {
            lblProgramName.setText(programDTO.getName());
        }
    }

    @FXML
    void onClickTable(MouseEvent event) {
        EnrollmentTM selected = tblEnrollment.getSelectionModel().getSelectedItem();
        if (selected != null) {
            lblId.setText(selected.getEnrollmentId());
            datePicker.setValue(selected.getEnrollmentDate());

            EnrollmentDTO enrollmentDTO = enrollmentBO.getEnrollment(selected.getEnrollmentId());
            if (enrollmentDTO != null) {
                cmbPatient.setValue(enrollmentDTO.getPatientId());
                cmbMethod.setValue(enrollmentDTO.getMethod());
                txtAmount.setText(String.valueOf(enrollmentDTO.getPayedAmount()));
                PatientDTO patientDTO = enrollmentBO.getPatient(enrollmentDTO.getPatientId());
                if (patientDTO != null) {
                    lblPatientName.setText(patientDTO.getFullName());
                }

                programCartTMS.clear();
                List<TherapyProgramDTO> programsByEnrollment = enrollmentBO.getProgramsByEnrollment(selected.getEnrollmentId());
                List<String> programIds = new ArrayList<>();
                for (TherapyProgramDTO program : programsByEnrollment) {
                    programIds.add(program.getProgramId());
                }
                for (String programId : programIds) {
                    TherapyProgramDTO programDTO = enrollmentBO.getTherapyProgram(programId);
                    if (programDTO != null) {
                        Button removeBtn = new Button("remove");

                        ProgramCartTM newProgramCartTM = new ProgramCartTM(
                                programDTO.getProgramId(),
                                programDTO.getName(),
                                programDTO.getFee(),
                                removeBtn
                        );

                        removeBtn.setOnAction(actionEvent -> {
                            programCartTMS.remove(newProgramCartTM);
                            tblProgram.refresh();
                            calculateTotal();
                        });

                        programCartTMS.add(newProgramCartTM);
                    }
                }

                calculateTotal();
            }
        }

        btnSave.setDisable(true);
        btnDelete.setDisable(false);
        btnUpdate.setDisable(false);
    }
}
