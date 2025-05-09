package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "payments")
public class Payment implements SuperEntity {
    @Id
    private String paymentId;

    private LocalDate paymentDate;
    private double totalCost;
    private double payedAmount;
    private double balance;
    private String method;

    private String status;
    private String invoiceNumber;
    private String remarks;

    @ManyToOne
    @JoinColumn(name = "processed_by")
    private User processedBy;

    @OneToOne(mappedBy = "payment")
    private Enrollment enrollment;
}
