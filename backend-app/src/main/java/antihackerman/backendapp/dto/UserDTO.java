package antihackerman.backendapp.dto;

import antihackerman.backendapp.model.Role;
import antihackerman.backendapp.model.User;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Integer id;
    private String username, email;
    private List<String> roles;
    private Timestamp lastPasswordResetDate;

    public UserDTO(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.lastPasswordResetDate = user.getLastPasswordResetDate();

        this.roles = new ArrayList<String>();
        for (Role role: user.getRoles()) {
            this.roles.add(role.getRole());
        }

    }
}
