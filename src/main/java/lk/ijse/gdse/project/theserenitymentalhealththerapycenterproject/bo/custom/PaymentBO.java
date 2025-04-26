package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom;

import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.SuperBO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.PatientDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.PaymentDTO;

import java.util.List;

public interface PaymentBO extends SuperBO {
    List<PaymentDTO> getPendingPayments();
    List<PaymentDTO> getCompletedPayments();
    PatientDTO getPatientFromEnrollment(String id);
    boolean checkoutPayment(String paymentId, double paidAmount, String method, String processedByUserId, String invoiceId, String remarks);
    PaymentDTO getLastPayment();
}
