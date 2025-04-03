package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "therapy_session")
public class TherapySession {
    @Id
    private String id;
    private String notes;

    @Column(name = "date")
    private Date date;

    @ManyToOne
    @JoinTable(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinTable(name = "therapist_id")
    private Therapist therapist;
}
