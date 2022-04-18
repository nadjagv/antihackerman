package antihackerman.backendapp.service;

import antihackerman.backendapp.dto.CertificateDTO;
import antihackerman.backendapp.dto.ExtensionDTO;
import antihackerman.backendapp.model.RootData;
import antihackerman.backendapp.model.SubjectData;
import antihackerman.backendapp.pki.data.IssuerData;
import antihackerman.backendapp.pki.keystores.KeyStoreReader;

import antihackerman.backendapp.pki.keystores.KeyStoreWriter;
import antihackerman.backendapp.util.KeyPairUtil;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.stereotype.Service;

import java.security.cert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CertificateService {

    private KeyStore keyStore;
    private KeyStoreReader keyStoreReader;
    private static String keyPass = "";

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
    
    public List<CertificateDTO> getAllCerts() {
    	keyStoreReader=new KeyStoreReader();
    	return keyStoreReader.getAllCerts("./keystore", "");
    }
    
    public Certificate  getCertificateByAlias(String alias) {
    	keyStoreReader=new KeyStoreReader();
    	return keyStoreReader.readCertificate("./keystore", "", alias);
    }
    
    public boolean checkValidityForCertificate(String alias) {
    	keyStoreReader=new KeyStoreReader();
    	Certificate[] chain=keyStoreReader.getCertChain(alias,"./keystore", "");
    	try {
    		for(Certificate c: chain) {
    			((X509Certificate)c).checkValidity();
    		}
		} catch (CertificateExpiredException e) {
			return false;
		} catch (CertificateNotYetValidException e) {
			return false;
		}
    	return true;
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

    public X509Certificate generateCertificate(SubjectData subjectData, ArrayList<ExtensionDTO> extensionDTOS) {
        try {
            JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");

            builder = builder.setProvider("BC");

            // root
            keyStoreReader=new KeyStoreReader();
            IssuerData issuerData = keyStoreReader.readIssuerFromStore("keystore", "antihackerman root", keyPass.toCharArray(), keyPass.toCharArray() );

            ContentSigner contentSigner = builder.build(issuerData.getPrivateKey());

            LocalDate start = LocalDate.now();
            SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = iso8601Formater.parse(start.toString());
            Date endDate = iso8601Formater.parse(start.plusYears(1).toString());

            X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(issuerData.getX500name(),
                    new BigInteger(32, new Random()),
                    startDate,
                    endDate,
                    subjectData.getX500name(),
                    subjectData.getPublicKey());

            //ekstenzije
            addExtensions(certGen, extensionDTOS, subjectData);

            //potpisivanje
            X509CertificateHolder certHolder = certGen.build(contentSigner);

            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider("BC");

            // Konvertuje objekat u sertifikat

            X509Certificate generatedCertificate =  certConverter.getCertificate(certHolder);

            Certificate ROOT = getCertificateByAlias("antihackerman root");
            KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
            keyStoreWriter.loadKeyStore(".\\src\\main\\resources\\keystore", keyPass.toCharArray());
            keyStoreWriter.write
                    (subjectData.getX500name().getRDNs(BCStyle.CN)[0].getFirst().getValue().toString(), issuerData.getPrivateKey(), keyPass.toCharArray(), generatedCertificate, ROOT);
            keyStoreWriter.saveKeyStore(".\\src\\main\\resources\\keystore", keyPass.toCharArray());
            return generatedCertificate;
        } catch (IllegalArgumentException | IllegalStateException | OperatorCreationException | CertificateException | ParseException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertIOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addExtensions(X509v3CertificateBuilder certGen, ArrayList<ExtensionDTO> extensionDTOS, SubjectData subjectData) throws NoSuchAlgorithmException, CertificateEncodingException, CertIOException {
        JcaX509ExtensionUtils utils = new JcaX509ExtensionUtils();
        for (ExtensionDTO extDTO :extensionDTOS) {
            switch (extDTO.getType()){
                case AUTHORITY_KEY_IDENTIFIER:{
                    certGen.addExtension(Extension.authorityKeyIdentifier, extDTO.isCritical(),
                        utils.createAuthorityKeyIdentifier((X509Certificate) getCertificateByAlias("antihackerman root")));
                    break;
                }
                case BASIC_CONSTRAINTS:{
                    certGen.addExtension(Extension.basicConstraints, extDTO.isCritical(), new BasicConstraints(false));
                    break;
                }
                case KEY_USAGE:{
                    certGen.addExtension(Extension.keyUsage, extDTO.isCritical(),
                            new KeyUsage( KeyUsage.keyEncipherment | KeyUsage.digitalSignature));
                    break;
                }
                case EXTENDED_KEY_USAGE:{
                    KeyPurposeId[] keyPurposeIds = new KeyPurposeId[2];
                    keyPurposeIds[0] = KeyPurposeId.id_kp_clientAuth;
                    keyPurposeIds[1] = KeyPurposeId.id_kp_serverAuth;

                    certGen.addExtension(Extension.extendedKeyUsage , extDTO.isCritical(),
                            new ExtendedKeyUsage(keyPurposeIds));
                    break;
                }
                case SUBJECT_ALTERNATIVE_NAME:{
                    GeneralName altName = new GeneralName(GeneralName.dNSName, "name");
                    GeneralNames subjectAltName = new GeneralNames(altName);
                    certGen.addExtension(Extension.subjectAlternativeName, extDTO.isCritical(), subjectAltName);
                    break;
                }
                case SUBJECT_KEY_IDENTIFIER:{
                    byte[] subjectKeyIdentifier = utils
                            .createSubjectKeyIdentifier(subjectData.getPublicKey()).getKeyIdentifier();

                    certGen.addExtension(Extension.subjectKeyIdentifier, extDTO.isCritical(),
                            new SubjectKeyIdentifier(subjectKeyIdentifier));
                    break;
                }
            }
        }
    }



    private RootData generateRootData() {
        try {
            KeyPair keyPairSubject = KeyPairUtil.generateKeyPair();

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
