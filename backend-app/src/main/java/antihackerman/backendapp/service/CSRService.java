package antihackerman.backendapp.service;

import antihackerman.backendapp.dto.CSRdto;
import antihackerman.backendapp.model.Extension;
import antihackerman.backendapp.model.RootData;
import antihackerman.backendapp.model.SubjectData;
import antihackerman.backendapp.util.KeyPairUtil;
import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.pkcs.Attribute;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.ContentVerifierProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.PKCSException;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Service
public class CSRService {


    public PKCS10CertificationRequest isValidSigned(String csr, Boolean renew) throws Exception {
        PEMParser pm = new PEMParser(new StringReader(csr));
        PKCS10CertificationRequest certReq = (PKCS10CertificationRequest) pm.readObject();

        ContentVerifierProvider prov = null;
        prov = new JcaContentVerifierProviderBuilder().build(certReq.getSubjectPublicKeyInfo());

        if (!certReq.isSignatureValid(prov)) {
            throw new Exception("CSR renewal is not valid");
        }
        return certReq;
    }

    public void addRequest(String csr) throws Exception {
        PKCS10CertificationRequest certReq = isValidSigned(csr, false);
        writeCSRToFileBase64Encoded(certReq, ".\\src\\main\\resources\\csr\\nesto.csr");
    }

    static void writeCSRToFileBase64Encoded(PKCS10CertificationRequest csr, String fileName) throws Exception {
        FileOutputStream certificateOut = new FileOutputStream(fileName);
        certificateOut.write("-----BEGIN CERTIFICATE-----".getBytes());
        certificateOut.write(Base64.getEncoder().encode(csr.getEncoded()));
        certificateOut.write("-----END CERTIFICATE-----".getBytes());
        certificateOut.close();
    }

    public PKCS10CertificationRequest generateCSR(SubjectData subjectData, ArrayList<Extension> extensions) throws Exception {

        PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(subjectData.getX500name(), subjectData.getPublicKey());

        for(Extension extension : extensions) {
            ExtensionsGenerator extGen = new ExtensionsGenerator();
            extGen.addExtension(extension.getOid(), extension.isCritical(), extension.getValue());
            p10Builder.addAttribute(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest, extGen.generate());

        }



        JcaContentSignerBuilder contentSignerBuilder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
        contentSignerBuilder = contentSignerBuilder.setProvider("BC");
        ContentSigner contentSigner = contentSignerBuilder.build(subjectData.getPrivateKey());
        PKCS10CertificationRequest csr = p10Builder.build(contentSigner);

        writeCSRToFileBase64Encoded(csr, ".\\src\\main\\resources\\csr\\testttt.csr");
        return csr;
    }

    public SubjectData generateSubjectDataCSR(CSRdto dto){
        KeyPair keyPairSubject = KeyPairUtil.generateKeyPair();

        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, dto.getCommonName());
        builder.addRDN(BCStyle.SURNAME, dto.getSurname());
        builder.addRDN(BCStyle.GIVENNAME, dto.getGivenname());
        builder.addRDN(BCStyle.O, dto.getOrganization());
        builder.addRDN(BCStyle.OU, dto.getOrganizationUnit());
        builder.addRDN(BCStyle.C, dto.getCountry());
        builder.addRDN(BCStyle.E, dto.getEmail());
        builder.addRDN(BCStyle.UID, dto.getUid());
        return new SubjectData(keyPairSubject.getPublic(), keyPairSubject.getPrivate(), builder.build());
    }

}
