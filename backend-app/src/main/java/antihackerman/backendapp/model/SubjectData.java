package antihackerman.backendapp.model;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500Name;

public class SubjectData {

    private PublicKey publicKey;
    private PrivateKey privateKey;
    private X500Name x500name;
    private String serialNumber;
    private Date startDate;
    private Date endDate;

    public SubjectData() {

    }

    public SubjectData(PublicKey publicKey, PrivateKey privateKey, X500Name x500name, String serialNumber, Date startDate, Date endDate) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.x500name = x500name;
        this.serialNumber = serialNumber;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public SubjectData(PublicKey publicKey, PrivateKey privateKey, X500Name x500name) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.x500name = x500name;
    }

    public X500Name getX500name() {
        return x500name;
    }

    public void setX500name(X500Name x500name) {
        this.x500name = x500name;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}

