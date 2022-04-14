package antihackerman.backendapp.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bouncycastle.asn1.x509.CRLNumber;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.x509.X509V2CRLGenerator;
import org.bouncycastle.x509.extension.AuthorityKeyIdentifierStructure;

import antihackerman.backendapp.pki.data.IssuerData;
import antihackerman.backendapp.pki.data.X509CRLEntryWrapper;
import antihackerman.backendapp.pki.keystores.KeyStoreReader;

public class PKIUtil {
	
	private KeyStoreReader keyStoreReader;
	
	private static final byte[] CRLF = new byte[] {'\r', '\n'};
	
	private byte[] getPemEncoded(Object obj) throws IOException { 
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); 
        OutputStreamWriter oswriter = new OutputStreamWriter(byteArrayOutputStream); 
        PEMWriter writer = new PEMWriter(oswriter); 
        writer.writeObject(obj); 
        writer.close(); 
        return byteArrayOutputStream.toByteArray(); 
    }
	
	public X509CRL createCRL(List<X509CRLEntryWrapper> entries) {
		keyStoreReader=new KeyStoreReader();
		X509Certificate cert=(X509Certificate) keyStoreReader.readCertificate("./keystore", "","antihackerman root");
		IssuerData id=keyStoreReader.readIssuerFromStore("./keystore","antihackerman root", new char[0], new char[0]);
		X509V2CRLGenerator generator = new X509V2CRLGenerator();
		generator.setIssuerDN(cert.getIssuerX500Principal()); 
		Date today=new Date();
		Date tommorow=new Date();
		tommorow.setDate(tommorow.getDate()+1);
        generator.setThisUpdate(today); 
        generator.setNextUpdate(tommorow); 
        generator.setSignatureAlgorithm("SHA1WITHRSA"); 
        for (X509CRLEntryWrapper entry : entries) { 
            generator.addCRLEntry(entry.getSerialNumber(), entry.getRevocationDate(), 
                entry.getReason().ordinal()); 
        }
        try {
			generator.addExtension(X509Extensions.AuthorityKeyIdentifier, false, new AuthorityKeyIdentifierStructure(cert));
		} catch (CertificateParsingException e1) {
			e1.printStackTrace();
		} 
        generator.addExtension(X509Extensions.CRLNumber, false, new CRLNumber(new BigInteger("1"))); 
        try {
			return generator.generate(id.getPrivateKey());
		} catch (InvalidKeyException | CRLException | IllegalStateException | NoSuchAlgorithmException
				| SignatureException e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	public byte[] writeCRLFile(File file, X509CRL crl) 
	        throws CRLException, CertificateException, IOException { 
	 
	        byte[] encoded = getPemEncoded(crl); 
	        ByteArrayOutputStream stream = new ByteArrayOutputStream(); 
	 
	        try { 
	            stream.write(encoded); 
	            FileUtils.writeByteArrayToFile(file, stream.toByteArray()); 
	        } 
	        finally { 
	            if (stream != null) { 
	                try { 
	                    stream.close(); 
	                } 
	                catch (IOException e) { 
	                }
	                   
	            } 
	        } 
	 
	        return encoded; 
	    } 
	
	public X509CRL readCRLFile(File file) 
	        throws CRLException, CertificateException, IOException { 
	 
	        FileInputStream in = null; 
	        try { 
	            if (file.exists() && file.length() > 0) { 
	                in = new FileInputStream(file); 
	            } 
	            X509CRL x509crl = null; 
	            if (in != null) { 
	                x509crl = (X509CRL) CertificateFactory.getInstance("X.509") 
	                    .generateCRL(in); 
	            } 
	 
	            return x509crl; 
	        } 
	        finally { 
	            if (in != null) { 
	                try { 
	                    in.close(); 
	                } 
	                catch (IOException e) { 
	                } 
	            } 
	        } 
	    }
	
	public void printCRL(X509CRL crl) {
		System.out.println("-----BEGIN X509 CRL-----");
		try {
			System.out.println(Base64.getMimeEncoder(64, CRLF).encodeToString(crl.getEncoded()));
		} catch (CRLException e) {
			e.printStackTrace();
		}
		System.out.println("-----END X509 CRL-----");
	}

}
