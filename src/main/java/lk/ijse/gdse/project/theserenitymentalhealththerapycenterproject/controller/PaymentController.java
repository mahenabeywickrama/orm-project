package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class PaymentController {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private ComboBox<?> cmbEnrollment;

    @FXML
    private ComboBox<?> cmbMethod;

    @FXML
    private TableColumn<?, ?> colAmount;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colEnrollment;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colMethod;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label lblId;

    @FXML
    private Label lblPatient;

    @FXML
    private Label lblPatientName;

    @FXML
    private Label lblTotalCost;

    @FXML
    private TableView<?> tblPayment;

    @FXML
    private TextField txtAmount;

    @FXML
    void btnDeleteOnAction(ActionEvent event) {

    }

    @FXML
    void btnResetOnAction(ActionEvent event) {

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {

    }

    @FXML
    void cmbEnrollmentOnAction(ActionEvent event) {

    }

    @FXML
    void onClickTable(MouseEvent event) {

    }

}
