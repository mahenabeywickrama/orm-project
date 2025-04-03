package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "therapy_program")
public class TherapyProgram {
    @Id
    private String id;
    private String name;
    private BigDecimal fee;

    @Column(name = "duration_(weeks)")
    private int duration;

    @ManyToOne
    @JoinTable(name = "therapist_id")
    private Therapist therapist;

    @OneToMany(mappedBy = "therapy_program")
    private List<Enrollment> enrollments;
}
