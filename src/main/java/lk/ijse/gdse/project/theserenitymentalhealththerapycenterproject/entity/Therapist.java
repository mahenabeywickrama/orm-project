package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "therapist")
public class Therapist {
    @Id
    private String id;
    private String name;
    private String specialization;
    private String contact;

    @OneToMany(mappedBy = "therapist")
    private List<TherapyProgram> programs;

    @OneToMany(mappedBy = "therapist")
    private List<TherapySession> therapySessions;
}
