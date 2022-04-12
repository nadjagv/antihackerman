package antihackerman.backendapp.service;

import antihackerman.backendapp.pki.data.IssuerData;
import antihackerman.backendapp.pki.data.RootData;
import antihackerman.backendapp.pki.data.SubjectData;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Service
public class CertificateService {

    private KeyStore keyStore;

    public void createNewKeyStore(String fileName, char[] password) {
        // TODO: Upotrebom klasa iz primeri/pki paketa, implementirati funkciju gde korisnik unosi ime keystore datoteke i ona se kreira
        try {
            if (fileName != null) {
                keyStore.load(new FileInputStream(fileName), password);
            } else {
                // Ako je cilj kreirati novi KeyStore poziva se i dalje load, pri cemu je prvi parametar null
                keyStore.load(null, password);
            }
        } catch (NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
        }
    }

    public void showKeyStoreContent() {
        // TODO: Upotrebom klasa iz primeri/pki paketa, prikazati sadrzaj keystore-a, gde korisnik unosi ime i lozinku
        // Dozvoljeno je menjati klase iz primeri/pki paketa

    }

    public X509Certificate createNewSelfSignedCertificate() {
        // TODO: Upotrebom klasa iz primeri/pki paketa, prikazati sadrzaj keystore-a, gde korisnik unosi sve potrebne podatke
        // Radi ustede hardkodovati podatke vezane za subjekta sertifikata
        try {
            RootData rootData = generateRootData();

            JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");

            builder = builder.setProvider("BC");

            ContentSigner contentSigner = builder.build(rootData.getPrivateKey());

            // Postavljaju se podaci za generisanje sertifiakta
            X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(rootData.getX500name(),
                    new BigInteger(rootData.getSerialNumber()),
                    rootData.getStartDate(),
                    rootData.getEndDate(),
                    rootData.getX500name(),
                    rootData.getPublicKey());
            // Generise se sertifikat
            X509CertificateHolder certHolder = certGen.build(contentSigner);

            // Builder generise sertifikat kao objekat klase X509CertificateHolder
            // Nakon toga je potrebno certHolder konvertovati u sertifikat, za sta se koristi certConverter
            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider("BC");

            // Konvertuje objekat u sertifikat
            return certConverter.getCertificate(certHolder);
        } catch (IllegalArgumentException | IllegalStateException | OperatorCreationException | CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createNewIssuedCertificate() {
        // TODO: Upotrebom klasa iz primeri/pki paketa, prikazati sadrzaj keystore-a, gde korisnik unosi sve potrebne podatke
        // Radi ustede vremena hardkodovati podatke vezane za subjekta sertifikata
    }

    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }


    private RootData generateRootData() {
        try {
            KeyPair keyPairSubject = generateKeyPair();

            // Datumi od kad do kad vazi sertifikat
            LocalDate start = LocalDate.now().minusYears(1);
            SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = iso8601Formater.parse(start.toString());
            Date endDate = iso8601Formater.parse(start.plusYears(5).toString());

            // Serijski broj sertifikata
            String sn = "1";

            // klasa X500NameBuilder pravi X500Name objekat koji predstavlja podatke o vlasniku
            X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
            builder.addRDN(BCStyle.CN, "Antihackerman Root");
            builder.addRDN(BCStyle.SURNAME, "Antihackerman");
            builder.addRDN(BCStyle.GIVENNAME, "Root");
            builder.addRDN(BCStyle.O, "BSEP");
            builder.addRDN(BCStyle.OU, "Antihackerman");
            builder.addRDN(BCStyle.C, "RS");
            builder.addRDN(BCStyle.E, "gvozdenac.nadja@uns.ac.rs");
            builder.addRDN(BCStyle.UID, "654321");

            // Kreiraju se podaci za sertifikat, sto ukljucuje:
            // - javni kljuc koji se vezuje za sertifikat
            // - podatke o vlasniku
            // - serijski broj sertifikata
            // - od kada do kada vazi sertifikat
            return new RootData(keyPairSubject.getPublic(), keyPairSubject.getPrivate(), builder.build(), sn, startDate, endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}