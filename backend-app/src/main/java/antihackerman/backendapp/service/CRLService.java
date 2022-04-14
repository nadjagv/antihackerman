package antihackerman.backendapp.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.cert.CRLException;
import java.security.cert.CRLReason;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v2CRLBuilder;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import antihackerman.backendapp.pki.data.X509CRLEntryWrapper;
import antihackerman.backendapp.util.PKIUtil;

@Service
public class CRLService {
	
	private PKIUtil pkiUtil=new PKIUtil();
	
	public void createCRL() {
		
		X509CRL crl=pkiUtil.createCRL(new ArrayList<X509CRLEntryWrapper>());
		Set<? extends X509CRLEntry> test=crl.getRevokedCertificates();
		pkiUtil.printCRL(crl);
		try {
			pkiUtil.writeCRLFile(new File("./src/main/resources/"+"CRL"), crl);
		} catch (CRLException | CertificateException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void revokeCert(BigInteger serialNumber) {
		try {
			X509CRL crl = pkiUtil.readCRLFile(new File("./src/main/resources/"+"CRL"));
			List<X509CRLEntryWrapper> enteries=new ArrayList<X509CRLEntryWrapper>();
			Set<? extends X509CRLEntry> test=crl.getRevokedCertificates();
			if(test!=null) {
				for(X509CRLEntry entry: test) {
					enteries.add(new X509CRLEntryWrapper(entry.getSerialNumber(),entry.getRevocationDate(),entry.getRevocationReason()));
				}
			}
			enteries.add(new X509CRLEntryWrapper(serialNumber,new Date(),CRLReason.AA_COMPROMISE));
			crl=pkiUtil.createCRL(enteries);
			test=crl.getRevokedCertificates();
			if(test!=null) {
				for(X509CRLEntry entry: test) {
					System.out.println(entry.getSerialNumber()+" "+entry.getRevocationDate()+" "+entry.getRevocationReason());
				}
				System.out.println(test.size());
			}
			pkiUtil.printCRL(crl);
			try {
				pkiUtil.writeCRLFile(new File("./src/main/resources/"+"CRL"), crl);
			} catch (CRLException | CertificateException | IOException e) {
				e.printStackTrace();
			}
		} catch (CRLException | CertificateException | IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public boolean checkRevocation(BigInteger serialNumber) {
		try {
			X509CRL crl = pkiUtil.readCRLFile(new File("./src/main/resources/"+"CRL"));
			if(crl.getRevokedCertificate(serialNumber)!=null) {
				return true;
			}
		} catch (CRLException | CertificateException | IOException e1) {
			e1.printStackTrace();
		}
		return false;
	}
	
}