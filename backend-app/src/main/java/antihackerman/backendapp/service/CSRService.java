package antihackerman.backendapp.service;

import antihackerman.backendapp.dto.CSRdto;
import antihackerman.backendapp.dto.ExtensionDTO;
import antihackerman.backendapp.model.CSR;
import antihackerman.backendapp.model.SubjectData;
import antihackerman.backendapp.util.FileUtil;
import antihackerman.backendapp.util.KeyPairUtil;
import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.ContentVerifierProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CSRService {

    private static String CSR_DIR_PATH = ".\\src\\main\\resources\\csr\\";

    @Autowired
    CertificateService certificateService;


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
        String uniqueFilename = generateUniqueId();
        writeCSRToFileBase64Encoded(certReq, CSR_DIR_PATH + uniqueFilename + ".csr");
    }


    static void writeCSRToFileBase64Encoded(PKCS10CertificationRequest csr, String fileName) throws Exception {
        PemObject pemObject = new PemObject("CERTIFICATE REQUEST", csr.getEncoded());
        FileWriter fw = new FileWriter(fileName);
        PemWriter pemWriter = new PEMWriter(fw);
        pemWriter.writeObject(pemObject);
        pemWriter.close();
        fw.close();
    }

    public PKCS10CertificationRequest generateCSR(SubjectData subjectData) throws Exception {

        PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(subjectData.getX500name(), subjectData.getPublicKey());


        JcaContentSignerBuilder contentSignerBuilder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
        contentSignerBuilder = contentSignerBuilder.setProvider("BC");
        ContentSigner contentSigner = contentSignerBuilder.build(subjectData.getPrivateKey());
        PKCS10CertificationRequest csr = p10Builder.build(contentSigner);
        String uniqueFilename = generateUniqueId();
        writeCSRToFileBase64Encoded(csr, CSR_DIR_PATH + uniqueFilename + ".csr");

        KeyPairUtil.writeEncryptedPrivateKeyToFile(uniqueFilename, subjectData.getPrivateKey());

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

    public List<CSR> readAllCSRs() throws Exception {
        ArrayList<CSR> result = new ArrayList<>();
        File dir = new File(CSR_DIR_PATH);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File f : directoryListing) {
                CSR csr = readCSRFile(f);
                result.add(csr);
            }
        } else {
            throw new Exception("No CSRs found.");
        }


        return result;
    }

    public CSR readSingleCSR(String filename) throws Exception {
        String path = CSR_DIR_PATH + filename;
        if (!Files.exists(Paths.get(path))){
            throw new Exception("File does not exist.");
        }
        File file = new File(path);
        return readCSRFile(file);
    }



    public CSR readCSRFile(File file) throws Exception {
        String fileContent = FileUtil.readFile(file);

        PKCS10CertificationRequest csr = getPKSCFromContent(fileContent);

        X500Name subjectData = csr.getSubject();

        CSR result = new CSR();
        result.setCommonName(subjectData.getRDNs(BCStyle.CN)[0].getFirst().getValue().toString());
        result.setSurname(subjectData.getRDNs(BCStyle.SURNAME)[0].getFirst().getValue().toString());
        result.setGivenname(subjectData.getRDNs(BCStyle.GIVENNAME)[0].getFirst().getValue().toString());
        result.setOrganization(subjectData.getRDNs(BCStyle.O)[0].getFirst().getValue().toString());
        result.setOrganizationUnit(subjectData.getRDNs(BCStyle.OU)[0].getFirst().getValue().toString());
        result.setCountry(subjectData.getRDNs(BCStyle.C)[0].getFirst().getValue().toString());
        result.setEmail(subjectData.getRDNs(BCStyle.E)[0].getFirst().getValue().toString());
        result.setUid(subjectData.getRDNs(BCStyle.UID)[0].getFirst().getValue().toString());

        result.setUniqueFilename(file.getName());

        return result;
    }

    private PKCS10CertificationRequest getPKSCFromContent(String fileContent) throws IOException {

        fileContent = fileContent.replace("-----BEGIN CERTIFICATE REQUEST-----", "");

        fileContent = fileContent.replace("-----END CERTIFICATE REQUEST-----", "");

        fileContent = fileContent.trim();
        PemObject pemObject = new PemObject("CERTIFICATE REQUEST", Base64.decodeBase64(fileContent));

        return new PKCS10CertificationRequest(pemObject.getContent());
    }

    public void rejectCSR(String filename) throws Exception {
        String path = CSR_DIR_PATH + filename;
        if (!Files.exists(Paths.get(path))){
            throw new Exception("File does not exist.");
        }
        Files.delete(Paths.get(path));
    }

    public void approveCSR(String filename, ArrayList<ExtensionDTO> extensionDTOS) throws Exception {
        String path = CSR_DIR_PATH + filename;
        if (!Files.exists(Paths.get(path))){
            throw new Exception("File does not exist.");
        }
        File file = new File(path);
        String fileContent = FileUtil.readFile(file);
        PKCS10CertificationRequest csr = getPKSCFromContent(fileContent);

        SubjectPublicKeyInfo pkInfo = csr.getSubjectPublicKeyInfo();
        RSAKeyParameters rsa = (RSAKeyParameters) PublicKeyFactory.createKey(pkInfo);
        RSAPublicKeySpec rsaSpec = new RSAPublicKeySpec(rsa.getModulus(), rsa.getExponent());
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey pubKey = kf.generatePublic(rsaSpec);

        PrivateKey privKey = KeyPairUtil.getEncryptedPrivateKeyFromFile(filename);

        SubjectData subjectData = new SubjectData(pubKey, privKey, csr.getSubject());

        certificateService.generateCertificate(subjectData, extensionDTOS);
        Files.delete(Paths.get(path));
    }

    public String generateUniqueId(){
        return UUID.randomUUID().toString();
    }

}
