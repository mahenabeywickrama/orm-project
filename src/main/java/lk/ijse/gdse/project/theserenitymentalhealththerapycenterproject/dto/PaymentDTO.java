package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentDTO {
    private String paymentId;
    private LocalDate paymentDate;
    private double totalCost;
    private double payedAmount;
    private double balance;
    private String method;
    private String status;
    private String invoiceNumber;
    private String remarks;
    private String enrollmentId;
    private String userId;
}
