package antihackerman.backendapp.model;

import lombok.*;
import org.bouncycastle.asn1.x500.X500Name;
import org.springframework.context.annotation.Bean;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RootData {
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private X500Name x500name;
    private String serialNumber;
    private Date startDate;
    private Date endDate;
}
