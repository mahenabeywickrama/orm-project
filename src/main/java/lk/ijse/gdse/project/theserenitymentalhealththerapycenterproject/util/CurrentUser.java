package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.util;

import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

public class CurrentUser {
    @Getter
    @Setter
    private static UserDTO currentUser;

    public static void clearUser() {
        currentUser = null;
    }
}
