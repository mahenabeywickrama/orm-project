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
    private double amount;
    private double balance;
    private String method;

    @ManyToOne
    @JoinColumn(name = "processed_by")
    private User processedBy;

    @OneToOne(mappedBy = "payment")
    private Enrollment enrollment;
}
