package antihackerman.backendapp.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDTO {
    private String username, email, password;
    private Integer groupId;
    private List <Integer> realestate_ids; //lista id realestateova gde je tenant
    private List<String> roles;
}
