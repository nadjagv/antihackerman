package antihackerman.backendapp.pki.data;

import java.math.BigInteger;
import java.security.cert.CRLReason;
import java.util.Date;


public class X509CRLEntryWrapper { 
    private BigInteger serialNumber; 
    private Date revocationDate; 
    private CRLReason reason;
 
    public X509CRLEntryWrapper(BigInteger serialNumber, Date revocationDate, CRLReason crlReason) { 
        this.serialNumber = serialNumber; 
        this.revocationDate = revocationDate; 
        this.reason=crlReason;
    } 
 
    public BigInteger getSerialNumber() { 
        return this.serialNumber; 
    } 
 
    public Date getRevocationDate() { 
        return this.revocationDate; 
    }

	public CRLReason getReason() {
		return reason;
	}

    
}
