package antihackerman.backendapp.pki.keystores;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;

import antihackerman.backendapp.dto.CertificateDTO;
import antihackerman.backendapp.pki.data.IssuerData;

public class KeyStoreReader {
    // KeyStore je Java klasa za citanje specijalizovanih datoteka koje se koriste za cuvanje kljuceva
    // Tri tipa entiteta koji se obicno nalaze u ovakvim datotekama su:
    // - Sertifikati koji ukljucuju javni kljuc
    // - Privatni kljucevi
    // - Tajni kljucevi, koji se koriste u simetricnima siframa
    private KeyStore keyStore;

    public KeyStoreReader() {
        try {
            keyStore = KeyStore.getInstance("JKS", "SUN");
        } catch (KeyStoreException | NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    /**
     * Zadatak ove funkcije jeste da ucita podatke o izdavaocu i odgovarajuci privatni kljuc.
     * Ovi podaci se mogu iskoristiti da se novi sertifikati izdaju.
     *
     * @param keyStoreFile - datoteka odakle se citaju podaci
     * @param alias        - alias putem kog se identifikuje sertifikat izdavaoca
     * @param password     - lozinka koja je neophodna da se otvori key store
     * @param keyPass      - lozinka koja je neophodna da se izvuce privatni kljuc
     * @return - podatke o izdavaocu i odgovarajuci privatni kljuc
     */
    public IssuerData readIssuerFromStore(String keyStoreFile, String alias, char[] password, char[] keyPass) {
        try {
            // Datoteka se ucitava
            BufferedInputStream in = new BufferedInputStream(new FileInputStream("./src/main/resources/"+keyStoreFile));
            keyStore.load(in, password);

            // Iscitava se sertifikat koji ima dati alias
            Certificate cert = keyStore.getCertificate(alias);

            // Iscitava se privatni kljuc vezan za javni kljuc koji se nalazi na sertifikatu sa datim aliasom
            PrivateKey privKey = (PrivateKey) keyStore.getKey(alias, keyPass);

            X500Name issuerName = new JcaX509CertificateHolder((X509Certificate) cert).getSubject();
            return new IssuerData(privKey, issuerName);
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException |
                UnrecoverableKeyException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Ucitava sertifikat is KS fajla
     */
    public Certificate readCertificate(String keyStoreFile, String keyStorePass, String alias) {
        try {
            // kreiramo instancu KeyStore
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");

            // ucitavamo podatke
            BufferedInputStream in = new BufferedInputStream(new FileInputStream("./src/main/resources/"+keyStoreFile));
            ks.load(in, keyStorePass.toCharArray());

            if (ks.isKeyEntry(alias)) {
                return ks.getCertificate(alias);
            }
        } catch (KeyStoreException | NoSuchProviderException | NoSuchAlgorithmException |
                CertificateException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Ucitava privatni kljuc is KS fajla
     */
    public PrivateKey readPrivateKey(String keyStoreFile, String keyStorePass, String alias, String pass) {
        try {
            // kreiramo instancu KeyStore
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");

            // ucitavamo podatke
            BufferedInputStream in = new BufferedInputStream(new FileInputStream("./src/main/resources/"+keyStoreFile));
            ks.load(in, keyStorePass.toCharArray());

            if (ks.isKeyEntry(alias)) {
                return (PrivateKey) ks.getKey(alias, pass.toCharArray());
            }
        } catch (KeyStoreException | NoSuchProviderException | NoSuchAlgorithmException |
                CertificateException | IOException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<CertificateDTO> getAllCerts(String keyStoreFile, String keyStorePass) {
    	try {
    		List<CertificateDTO> certs=new ArrayList<CertificateDTO>();
    		KeyStore ks = KeyStore.getInstance("JKS", "SUN");
        	
        	BufferedInputStream in = new BufferedInputStream(new FileInputStream("./src/main/resources/"+keyStoreFile));
            ks.load(in, keyStorePass.toCharArray());
        	
        	Enumeration<String> enumeration = ks.aliases();
            while(enumeration.hasMoreElements()) {
                String alias = enumeration.nextElement();
                System.out.println("alias name: " + alias);
                Certificate certificate = ks.getCertificate(alias);
                BigInteger serial=((X509Certificate)certificate).getSerialNumber();
                certs.add(new CertificateDTO(alias,serial));
                //System.out.println(certificate.toString());

            }
            return certs;
    	}catch(KeyStoreException | NoSuchProviderException | NoSuchAlgorithmException |
                CertificateException | IOException e){
    		e.printStackTrace();
    	}
    	return null;
    	
    }
    
    public String getAliasFromSerial(BigInteger serial,String keyStoreFile, String keyStorePass) {
    	try {
    		KeyStore ks = KeyStore.getInstance("JKS", "SUN");
        	
        	BufferedInputStream in = new BufferedInputStream(new FileInputStream("./src/main/resources/"+keyStoreFile));
            ks.load(in, keyStorePass.toCharArray());
        	
        	Enumeration<String> enumeration = ks.aliases();
            while(enumeration.hasMoreElements()) {
                String alias = enumeration.nextElement();
                Certificate certificate = ks.getCertificate(alias);
                BigInteger serialFound=((X509Certificate)certificate).getSerialNumber();
                if(serial.equals(serialFound)) {
                	return alias;
                }
            }
    	}catch(KeyStoreException | NoSuchProviderException | NoSuchAlgorithmException |
                CertificateException | IOException e){
    		e.printStackTrace();
    	}
    	return null;
    }
    
    public Certificate[] getCertChain(String alias,String keyStoreFile, String keyStorePass) {
    	try {
    		KeyStore ks = KeyStore.getInstance("JKS", "SUN");
        	
        	BufferedInputStream in = new BufferedInputStream(new FileInputStream("./src/main/resources/"+keyStoreFile));
            ks.load(in, keyStorePass.toCharArray());
        	
        	return ks.getCertificateChain(alias);
    	}catch(KeyStoreException | NoSuchProviderException | NoSuchAlgorithmException |
                CertificateException | IOException e){
    		e.printStackTrace();
    	}
    	return null;
    }
}
