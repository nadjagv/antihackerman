package antihackerman.backendapp.model;

import lombok.*;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Extension {

    ASN1ObjectIdentifier oid;
    ASN1Encodable value;
    boolean isCritical;
}
