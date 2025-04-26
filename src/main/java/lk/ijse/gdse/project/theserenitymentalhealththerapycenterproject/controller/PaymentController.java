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
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom.PaymentBO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.PatientDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.PaymentDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.tm.PaymentTM;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.util.CurrentUser;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class PaymentController implements Initializable {

    @FXML
    private Button btnCheckout;

    @FXML
    private Button btnReset;

    @FXML
    private ComboBox<String> cmbMethod;

    @FXML
    private TableColumn<PaymentTM, Double> colAmountCompleted;

    @FXML
    private TableColumn<PaymentTM, Double> colAmountPending;

    @FXML
    private TableColumn<PaymentTM, Double> colCostCompleted;

    @FXML
    private TableColumn<PaymentTM, Double> colCostPending;

    @FXML
    private TableColumn<PaymentTM, LocalDate> colDateCompleted;

    @FXML
    private TableColumn<PaymentTM, LocalDate> colDatePending;

    @FXML
    private TableColumn<PaymentTM, String> colEnrollmentCompleted;

    @FXML
    private TableColumn<PaymentTM, String> colEnrollmentPending;

    @FXML
    private TableColumn<PaymentTM, String> colIdCompleted;

    @FXML
    private TableColumn<PaymentTM, String> colIdPending;

    @FXML
    private TableColumn<PaymentTM, String> colMethodCompleted;

    @FXML
    private TableColumn<PaymentTM, String> colMethodPending;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label lblEnrollment;

    @FXML
    private Label lblId;

    @FXML
    private Label lblPatient;

    @FXML
    private Label lblPatientName;

    @FXML
    private Label lblRemaining;

    @FXML
    private Label lblTotal;

    @FXML
    private TableView<PaymentTM> tblCompleted;

    @FXML
    private TableView<PaymentTM> tblPending;

    @FXML
    private TextField txtAmount;

    @FXML
    private TextField txtRemarks;

    private PaymentBO paymentBO = BOFactory.getInstance().getBO(BOTypes.PAYMENT);

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
        colIdPending.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colDatePending.setCellValueFactory(new PropertyValueFactory<>("date"));
        colAmountPending.setCellValueFactory(new PropertyValueFactory<>("payedAmount"));
        colMethodPending.setCellValueFactory(new PropertyValueFactory<>("method"));
        colCostPending.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        colEnrollmentPending.setCellValueFactory(new PropertyValueFactory<>("enrollmentId"));

        colIdCompleted.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colDateCompleted.setCellValueFactory(new PropertyValueFactory<>("date"));
        colAmountCompleted.setCellValueFactory(new PropertyValueFactory<>("payedAmount"));
        colMethodCompleted.setCellValueFactory(new PropertyValueFactory<>("method"));
        colCostCompleted.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        colEnrollmentCompleted.setCellValueFactory(new PropertyValueFactory<>("enrollmentId"));
    }

    private void refreshPage() {
        loadPendingTable();
        loadCompletedTable();
        loadMethodCmb();
        clearFields();
    }

    private void loadPendingTable() {
        try {
            List<PaymentDTO> pendingPayments = paymentBO.getPendingPayments();
            ObservableList<PaymentTM> paymentTMS = FXCollections.observableArrayList();

            for (PaymentDTO paymentDTO : pendingPayments) {
                PaymentTM paymentTM = new PaymentTM(
                        paymentDTO.getPaymentId(),
                        paymentDTO.getPaymentDate(),
                        paymentDTO.getPayedAmount(),
                        paymentDTO.getMethod(),
                        paymentDTO.getTotalCost(),
                        paymentDTO.getEnrollmentId()
                );
                paymentTMS.add(paymentTM);
            }
            tblPending.setItems(paymentTMS);
        } catch (Exception e) {
            showErrorAlert(e.getMessage());
        }
    }

    private void loadCompletedTable() {
        try {
            List<PaymentDTO> pendingPayments = paymentBO.getCompletedPayments();
            ObservableList<PaymentTM> paymentTMS = FXCollections.observableArrayList();

            for (PaymentDTO paymentDTO : pendingPayments) {
                PaymentTM paymentTM = new PaymentTM(
                        paymentDTO.getPaymentId(),
                        paymentDTO.getPaymentDate(),
                        paymentDTO.getPayedAmount(),
                        paymentDTO.getMethod(),
                        paymentDTO.getTotalCost(),
                        paymentDTO.getEnrollmentId()
                );
                paymentTMS.add(paymentTM);
            }
            tblCompleted.setItems(paymentTMS);
        } catch (Exception e) {
            showErrorAlert(e.getMessage());
        }
    }

    private void clearFields() {
        lblId.setText(null);
        lblPatient.setText(null);
        lblPatientName.setText(null);
        lblEnrollment.setText(null);
        lblRemaining.setText(null);
        lblTotal.setText(null);

        txtAmount.clear();
        txtRemarks.clear();

        datePicker.setValue(null);
        cmbMethod.setValue(null);
    }

    private boolean validateInputs() {
        boolean isValid = true;

        resetFieldStyles();

        if (datePicker.getValue() == null) {
            datePicker.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (txtAmount.getText().isEmpty() || !txtAmount.getText().matches("\\d+(\\.\\d{1,2})?")) {
            txtAmount.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (txtRemarks.getText().isEmpty()) {
            txtRemarks.setStyle("-fx-border-color: red;");
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
        txtRemarks.setStyle(null);
        txtAmount.setStyle(null);
    }

    private void loadMethodCmb() {
        List<String> methods = new ArrayList<>();
        methods.add("CARD");
        methods.add("CASH");
        ObservableList<String> methodsCmb = FXCollections.observableArrayList();

        methodsCmb.addAll(methods);
        cmbMethod.setItems(methodsCmb);
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
    void btnCheckoutOnAction(ActionEvent event) {
        if (!validateInputs()) return;

        if (lblId.getText().isEmpty()) {
            showErrorAlert("Please select a payment.");
            return;
        }

        double amountPaidNow = Double.parseDouble(txtAmount.getText());
        double remainingBalance = Double.parseDouble(lblRemaining.getText());
        double totalCost = Double.parseDouble(lblTotal.getText());

        if (amountPaidNow < remainingBalance) {
            showErrorAlert("You must pay the full remaining balance.");
            return;
        }

        String method = cmbMethod.getValue();
        String remarks = txtRemarks.getText();
        String invoiceId = "INV-" + UUID.randomUUID().toString().substring(0, 8);

        try {
            boolean isUpdated = paymentBO.checkoutPayment(
                    lblId.getText(),
                    amountPaidNow,
                    method,
                    CurrentUser.getCurrentUser().getUserId(),
                    invoiceId,
                    remarks
            );

            if (isUpdated) {
                showSuccessAlert("Payment completed. Invoice generated: " + invoiceId);
                refreshPage();
            } else {
                showErrorAlert("Payment failed.");
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
    void onClickTable(MouseEvent event) {
        PaymentTM selectedItem = tblPending.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            lblId.setText(selectedItem.getPaymentId());
            datePicker.setValue(selectedItem.getDate());
            lblEnrollment.setText(selectedItem.getEnrollmentId());
            cmbMethod.setValue(selectedItem.getMethod());
            lblTotal.setText(String.valueOf(selectedItem.getTotalCost()));

            PatientDTO patientDTO = paymentBO.getPatientFromEnrollment(selectedItem.getEnrollmentId());
            if (patientDTO != null) {
                lblPatient.setText(patientDTO.getPatientId());
                lblPatientName.setText(patientDTO.getFullName());
            }

            double totalCost = selectedItem.getTotalCost();
            double payedAmount = selectedItem.getPayedAmount();
            double remaining = totalCost - payedAmount;
            lblRemaining.setText(String.format("%.2f", remaining));
        }
    }
}
