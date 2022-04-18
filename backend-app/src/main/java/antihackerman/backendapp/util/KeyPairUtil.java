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
import java.security.spec.InvalidKeySpecException;
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


    public static void writeEncryptedPrivateKeyToFile(String uniqueFilename, PrivateKey pk){
        Key aesKey = new SecretKeySpec(encryptKey.getBytes(), "AES");
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES");

            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(pk.getEncoded());

            FileWriter pw = new FileWriter(PK_FILE_PATH,true);
            String s = java.util.Base64.getEncoder().encodeToString(encrypted);

            pw.append(uniqueFilename + ","+s+"\n");
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
                    byte[] dekriptBase64 = java.util.Base64.getDecoder().decode(encryptedStr.trim());
                    byte[] decrypted = cipher.doFinal(dekriptBase64);

                    KeyFactory kf = KeyFactory.getInstance("RSA");
                    PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(decrypted));
                    System.out.println(privateKey.getEncoded());
                    return privateKey;

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
