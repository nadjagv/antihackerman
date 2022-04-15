package antihackerman.backendapp.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CSR {

    private String commonName, surname, givenname, organization, organizationUnit, country, email, uid, uniqueFilename;

}
