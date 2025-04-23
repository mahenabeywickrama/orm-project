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
@Table(name = "therapists")
public class Therapist implements SuperEntity {
    @Id
    private String therapistId;

    private String name;
    private String specialization;
    private String contactNumber;

    @OneToMany(mappedBy = "therapist", cascade = CascadeType.ALL)
    private List<TherapyProgram> programs;

    @OneToMany(mappedBy = "therapist", cascade = CascadeType.ALL)
    private List<TherapySession> sessions;
}
