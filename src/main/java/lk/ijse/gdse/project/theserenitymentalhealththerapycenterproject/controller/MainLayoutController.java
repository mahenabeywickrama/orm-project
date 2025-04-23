package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainLayoutController implements Initializable {

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnEnrollment;

    @FXML
    private Button btnPatient;

    @FXML
    private Button btnPayment;

    @FXML
    private Button btnProgram;

    @FXML
    private Button btnSession;

    @FXML
    private Button btnTherapist;

    @FXML
    private Button btnUser;

    @FXML
    private AnchorPane contentPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void btnDashboardOnAction(ActionEvent event) {

    }

    @FXML
    void btnEnrollmentOnAction(ActionEvent event) {
        try {
            Node node = FXMLLoader.load(getClass().getResource("/view/EnrollmentView.fxml"));
            contentPane.getChildren().clear();
            contentPane.getChildren().setAll(node);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnPatientOnAction(ActionEvent event) {
        try {
            Node node = FXMLLoader.load(getClass().getResource("/view/PatientView.fxml"));
            contentPane.getChildren().clear();
            contentPane.getChildren().setAll(node);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnPaymentOnAction(ActionEvent event) {
        try {
            Node node = FXMLLoader.load(getClass().getResource("/view/PaymentView.fxml"));
            contentPane.getChildren().clear();
            contentPane.getChildren().setAll(node);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnProgramOnAction(ActionEvent event) {
        try {
            Node node = FXMLLoader.load(getClass().getResource("/view/TherapyProgramView.fxml"));
            contentPane.getChildren().clear();
            contentPane.getChildren().setAll(node);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnSessionOnAction(ActionEvent event) {
        try {
            Node node = FXMLLoader.load(getClass().getResource("/view/TherapySessionView.fxml"));
            contentPane.getChildren().clear();
            contentPane.getChildren().setAll(node);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnTherapistOnAction(ActionEvent event) {
        try {
            Node node = FXMLLoader.load(getClass().getResource("/view/TherapistView.fxml"));
            contentPane.getChildren().clear();
            contentPane.getChildren().setAll(node);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnUserOnAction(ActionEvent event) {

    }
}
