package antihackerman.backendapp.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CSRdto {

    private String commonName, surname, givenname, organization, organizationUnit, country, email, uid;

}
