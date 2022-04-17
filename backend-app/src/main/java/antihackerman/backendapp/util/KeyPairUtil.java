package antihackerman.backendapp.util;

import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;

public class KeyPairUtil {


    private static String PK_FILE_PATH = ".\\src\\main\\resources\\pk.csv";
    private static String encryptKey = "Bar12345Bar12345"; // 128 bit key

    public static KeyPair generateKeyPair() {
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

    public static void writeKeyFileEncoded(PrivateKey pk, String fileName) throws Exception {
        Key aesKey = new SecretKeySpec(encryptKey.getBytes(), "AES");
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES");

            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(pk.getEncoded());
            PemObject pemObject = new PemObject("PRIVATE KEY", encrypted);
            FileWriter fw = new FileWriter(fileName);
            PemWriter pemWriter = new PEMWriter(fw);
            pemWriter.writeObject(pemObject);
            pemWriter.close();
            fw.close();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static PrivateKey getStoredPrivateKey(String uniqueFilename) throws Exception {
        File file = new File(".\\src\\main\\resources\\pk\\" + uniqueFilename + ".txt");
        String fileContent = FileUtil.readFile(file);

        System.out.println(fileContent);
        fileContent = fileContent.replace("-----BEGIN CERTIFICATE REQUEST-----", "");

        fileContent = fileContent.replace("-----END CERTIFICATE REQUEST-----", "");

        fileContent = fileContent.trim();
        System.out.println(fileContent);

        byte [] encoded = Base64.decodeBase64(fileContent);

        // extract the private key

        Key aesKey = new SecretKeySpec(encryptKey.getBytes(), "AES");
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES");

            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            byte[] decrypted = cipher.doFinal(encoded);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decrypted);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey privKey = kf.generatePrivate(keySpec);
            System.out.println(privKey.getEncoded());

            return privKey;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;

    }

    public static void writeEncryptedPrivateKeyToFile(String uniqueFilename, PrivateKey pk){
        Key aesKey = new SecretKeySpec(encryptKey.getBytes(), "AES");
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES");

            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(pk.getEncoded());

            FileWriter pw = new FileWriter(PK_FILE_PATH,true);
            pw.append(uniqueFilename + ","+encrypted+"\n");
            pw.close();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static PrivateKey getEncryptedPrivateKeyFromFile(String uniqueFilename){
        Key aesKey = new SecretKeySpec(encryptKey.getBytes(), "AES");
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES");

            cipher.init(Cipher.DECRYPT_MODE, aesKey);

            BufferedReader br = new BufferedReader(new FileReader(PK_FILE_PATH));
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].contentEquals(uniqueFilename)){
                    String encryptedStr = values[1];
                    byte[] decrypted = cipher.doFinal(encryptedStr.getBytes(StandardCharsets.UTF_8));
                    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decrypted);
                    KeyFactory kf = KeyFactory.getInstance("RSA");
                    PrivateKey privKey = kf.generatePrivate(keySpec);
                    System.out.println(privKey.getEncoded());
                    return privKey;

                }
            }
//
//            FileWriter pw = new FileWriter(PK_FILE_PATH,true);
//            pw.append(uniqueFilename + ","+encrypted);
//            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
