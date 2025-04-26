package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentTM {
    private String paymentId;
    private LocalDate date;
    private double payedAmount;
    private String method;
    private double totalCost;
    private String enrollmentId;
}
