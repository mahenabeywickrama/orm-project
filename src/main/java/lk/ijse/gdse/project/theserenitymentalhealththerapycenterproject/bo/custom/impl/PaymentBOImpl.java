package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom.impl;

import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.bo.custom.PaymentBO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.config.FactoryConfiguration;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.DAOFactory;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.DAOTypes;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.custom.EnrollmentDAO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.custom.PatientDAO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dao.custom.PaymentDAO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.PatientDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.PaymentDTO;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.Enrollment;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.Patient;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.Payment;
import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaymentBOImpl implements PaymentBO {
    PaymentDAO paymentDAO = DAOFactory.getInstance().getDAO(DAOTypes.PAYMENT);
    EnrollmentDAO enrollmentDAO = DAOFactory.getInstance().getDAO(DAOTypes.ENROLLMENT);
    PatientDAO patientDAO = DAOFactory.getInstance().getDAO(DAOTypes.PATIENT);

    FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public List<PaymentDTO> getPendingPayments() {
        List<Payment> payments = paymentDAO.getAll();
        List<PaymentDTO> paymentDTOs = new ArrayList<PaymentDTO>();

        for (Payment payment : payments) {
            if (!payment.getStatus().equals("COMPLETED")) {
                PaymentDTO paymentDTO = new PaymentDTO();
                paymentDTO.setPaymentId(payment.getPaymentId());
                paymentDTO.setPaymentDate(payment.getPaymentDate());
                paymentDTO.setBalance(payment.getBalance());
                paymentDTO.setStatus(payment.getStatus());
                paymentDTO.setMethod(payment.getMethod());
                paymentDTO.setRemarks(payment.getRemarks());
                paymentDTO.setPayedAmount(payment.getPayedAmount());
                paymentDTO.setTotalCost(payment.getTotalCost());
                paymentDTO.setInvoiceNumber(payment.getInvoiceNumber());
                paymentDTO.setEnrollmentId(payment.getEnrollment().getEnrollmentId());
                paymentDTOs.add(paymentDTO);
            }
        }
        return paymentDTOs;
    }

    @Override
    public List<PaymentDTO> getCompletedPayments() {
        List<Payment> payments = paymentDAO.getAll();
        List<PaymentDTO> paymentDTOs = new ArrayList<PaymentDTO>();

        for (Payment payment : payments) {
            if (payment.getStatus().equals("COMPLETED")) {
                PaymentDTO paymentDTO = new PaymentDTO();
                paymentDTO.setPaymentId(payment.getPaymentId());
                paymentDTO.setPaymentDate(payment.getPaymentDate());
                paymentDTO.setBalance(payment.getBalance());
                paymentDTO.setStatus(payment.getStatus());
                paymentDTO.setMethod(payment.getMethod());
                paymentDTO.setRemarks(payment.getRemarks());
                paymentDTO.setPayedAmount(payment.getPayedAmount());
                paymentDTO.setTotalCost(payment.getTotalCost());
                paymentDTO.setInvoiceNumber(payment.getInvoiceNumber());
                paymentDTO.setEnrollmentId(payment.getEnrollment().getEnrollmentId());
                paymentDTOs.add(paymentDTO);
            }
        }
        return paymentDTOs;
    }

    @Override
    public PatientDTO getPatientFromEnrollment(String id) {
        Optional<Enrollment> enrollment = enrollmentDAO.findById(id);
        if (enrollment.isPresent()) {
            String patientId = enrollment.get().getPatient().getPatientId();
            Optional<Patient> patientById = patientDAO.findById(patientId);
            if (patientById.isPresent()) {
                Patient patient = patientById.get();
                PatientDTO patientDTO = new PatientDTO();
                patientDTO.setPatientId(patient.getPatientId());
                patientDTO.setFullName(patient.getFullName());
                patientDTO.setEmail(patient.getEmail());
                patientDTO.setPhoneNumber(patient.getPhoneNumber());
                patientDTO.setMedicalHistory(patient.getMedicalHistory());
                return patientDTO;
            }
        }
        return null;
    }

    @Override
    public boolean checkoutPayment(String paymentId, double paidAmount, String method, String processedByUserId, String invoiceId, String remarks) {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            Payment payment = session.get(Payment.class, paymentId);
            if (payment == null) {
                return false;
            }

            double updatedPaidAmount = payment.getPayedAmount() + paidAmount;
            payment.setPayedAmount(updatedPaidAmount);

            double updatedBalance = updatedPaidAmount - payment.getTotalCost();
            if (updatedBalance < 0) {
                updatedBalance = 0;
            }
            payment.setBalance(updatedBalance);

            if (updatedBalance >= 0) {
                payment.setStatus("COMPLETED");
            } else {
                payment.setStatus("UPFRONT");
            }

            payment.setMethod(method);
            payment.setPaymentDate(LocalDate.now());
            payment.setInvoiceNumber(invoiceId);
            payment.setRemarks(remarks);

            User processedBy = session.get(User.class, processedByUserId);
            payment.setProcessedBy(processedBy);

            session.update(payment);
            transaction.commit();
            return true;

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) session.close();
        }
    }

    public PaymentDTO getLastPayment() {
        Payment lastPayment = paymentDAO.getLastPayment();
        if (lastPayment != null) {
            return new PaymentDTO(
                    lastPayment.getPaymentId(),
                    lastPayment.getPaymentDate(),
                    lastPayment.getTotalCost(),
                    lastPayment.getPayedAmount(),
                    lastPayment.getBalance(),
                    lastPayment.getMethod(),
                    lastPayment.getStatus(),
                    lastPayment.getInvoiceNumber(),
                    lastPayment.getRemarks(),
                    lastPayment.getEnrollment().getEnrollmentId(),
                    lastPayment.getProcessedBy().getUsername()
            );
        }
        return null;
    }

}















