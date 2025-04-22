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
@Table(name = "users")
public class User {
    @Id
    private String userId;

    private String username;
    private String password;
    private String role;

    @OneToMany(mappedBy = "processedBy")
    private List<Payment> processedPayments;
}
