package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "therapy_programs")
public class TherapyProgram implements SuperEntity {
    @Id
    private String programId;

    private String name;
    private int durationInWeeks;
    private double fee;

    @ManyToOne
    @JoinColumn(name = "therapist_id")
    private Therapist therapist;

    @ManyToMany(mappedBy = "therapyPrograms")
    private List<Enrollment> enrollments;
}
