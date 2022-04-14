package antihackerman.backendapp.pki.data;

import java.math.BigInteger;
import java.util.Date;

public class X509CRLEntryWrapper { 
    private BigInteger serialNumber; 
    private Date revocationDate; 
 
    /**
     * Instantiates a new simple crl entry. 
     * 
     * @param serialNumber the serial number 
     * @param revocationDate the revocation date 
     */ 
    public X509CRLEntryWrapper(BigInteger serialNumber, Date revocationDate) { 
        this.serialNumber = serialNumber; 
        this.revocationDate = revocationDate; 
    } 
 
    public BigInteger getSerialNumber() { 
        return this.serialNumber; 
    } 
 
    public Date getRevocationDate() { 
        return this.revocationDate; 
    } 
}
