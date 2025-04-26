package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.UserDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.util.CurrentUser;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainLayoutController implements Initializable {

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnEnrollment;

    @FXML
    private Button btnLogout;

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
    private Label lblDate;

    @FXML
    private Label lblRole;

    @FXML
    private Label lblTime;

    @FXML
    private Label lblUser;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private AnchorPane contentPane;

    private Button activeButton = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadUserDetails();
        applyHoversForButtons();
        loadDateAndTime();
    }

    private void applyHoversForButtons() {
        applyHoverEffect(btnPatient);
        applyHoverEffect(btnEnrollment);
        applyHoverEffect(btnPayment);
        applyHoverEffect(btnTherapist);
        applyHoverEffect(btnProgram);
        applyHoverEffect(btnSession);
        applyHoverEffect(btnUser);
    }

    private void loadUserDetails() {
        UserDTO currentUser = CurrentUser.getCurrentUser();
        lblUser.setText(currentUser.getUsername());
        lblRole.setText(currentUser.getRole());

        if (!lblRole.getText().equals("ADMIN")) {
            btnTherapist.setVisible(false);
            btnProgram.setVisible(false);
            btnUser.setVisible(false);
        }
    }

    private void loadDateAndTime() {
        Timeline time = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    lblDate.setText(LocalDateTime.now().format(dateFormatter));
                    lblTime.setText(LocalDateTime.now().format(timeFormatter));
                }),
                new KeyFrame(Duration.seconds(1))
        );
        time.setCycleCount(Animation.INDEFINITE);
        time.play();
    }

    @FXML
    void btnEditOnAction(ActionEvent event) {
        try {
            Node node = FXMLLoader.load(getClass().getResource("/view/AccountView.fxml"));
            contentPane.getChildren().clear();
            contentPane.getChildren().setAll(node);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnEnrollmentOnAction(ActionEvent event) {
        highlightButton(btnEnrollment);

        try {
            Node node = FXMLLoader.load(getClass().getResource("/view/EnrollmentView.fxml"));
            contentPane.getChildren().clear();
            contentPane.getChildren().setAll(node);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnLogoutOnAction(ActionEvent event) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> optionalButtonType = alert.showAndWait();

            if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {
                CurrentUser.clearUser();
                Node node = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
                mainPane.getChildren().clear();
                mainPane.getChildren().setAll(node);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnPatientOnAction(ActionEvent event) {
        highlightButton(btnPatient);

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
        highlightButton(btnPayment);

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
        highlightButton(btnProgram);

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
        highlightButton(btnSession);

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
        highlightButton(btnTherapist);

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
        highlightButton(btnUser);

        try {
            Node node = FXMLLoader.load(getClass().getResource("/view/UserView.fxml"));
            contentPane.getChildren().clear();
            contentPane.getChildren().setAll(node);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void applyHoverEffect(Button button) {
        button.setOnMouseEntered(e -> {
            if (button != activeButton) {
                button.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-background-radius: 10px; -fx-border-color: black; -fx-border-radius: 10px;");
            }
        });

        button.setOnMouseExited(e -> {
            if (button != activeButton) {
                button.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-background-radius: 10px;");
            }
        });
    }

    private void highlightButton(Button clickedButton) {
        if (activeButton != null) {
            activeButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-background-radius: 10px;");
        }

        clickedButton.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-background-radius: 10px; -fx-border-color: black; -fx-border-radius: 10px;");

        activeButton = clickedButton;
    }

}
