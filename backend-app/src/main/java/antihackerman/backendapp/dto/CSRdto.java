package antihackerman.backendapp.dto;

import antihackerman.backendapp.model.CSR;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CSRdto {

    private String commonName, surname, givenname, organization, organizationUnit, country, email, uid, uniqueFilename;

    public CSRdto (CSR csr){
        this.commonName = csr.getCommonName();
        this.surname = csr.getSurname();
        this.givenname = csr.getGivenname();
        this.organization = csr.getOrganization();
        this.organizationUnit = csr.getOrganizationUnit();
        this.country = csr.getCountry();
        this.email = csr.getEmail();
        this.uid = csr.getUid();
        this.uniqueFilename = csr.getUniqueFilename();
    }
}
