package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "enrollments")
public class Enrollment implements SuperEntity {
    @Id
    private String enrollmentId;
    private LocalDate enrollmentDate;
    private String enrollmentStatus;
    private double totalCost;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToMany
    @JoinTable(
            name = "enrollment_therapy_programs", // Join table name
            joinColumns = @JoinColumn(name = "enrollment_id"),
            inverseJoinColumns = @JoinColumn(name = "program_id")
    )
    private List<TherapyProgram> therapyPrograms;

    @OneToOne
    @JoinColumn(name = "payment_id", referencedColumnName = "paymentId", nullable = false)
    private Payment payment;
}
