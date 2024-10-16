import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.*;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class SecureFileEncrypterDecrypter {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int KEY_SIZE = 256;
    private static final int IV_SIZE = 12;
    private static final int TAG_LENGTH = 128;
    private static final int ITERATION_COUNT = 65536;
    private static final int SALT_SIZE = 16;
    private SecretKey secretKey;

    public SecureFileEncrypterDecrypter(String password) throws Exception {
        this.secretKey = deriveKeyFromPassword(password);
    }

    private SecretKey deriveKeyFromPassword(String password) throws Exception {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_SIZE];
        random.nextBytes(salt);

        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_SIZE);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, "AES");
    }

    public void encryptFile(File inputFile, File outputFile) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        byte[] iv = new byte[IV_SIZE];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH, iv);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(outputFile);
             CipherOutputStream cos = new CipherOutputStream(fos, cipher)) {
            fos.write(iv); 
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                cos.write(buffer, 0, bytesRead);
            }
        }
    }

    public void decryptFile(File inputFile, File outputFile) throws Exception {
        try (FileInputStream fis = new FileInputStream(inputFile)) {
            byte[] iv = new byte[IV_SIZE];
            fis.read(iv); 
            GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH, iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
            try (CipherInputStream cis = new CipherInputStream(fis, cipher);
                 FileOutputStream fos = new FileOutputStream(outputFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = cis.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            String password = "your_secure_password";
            SecureFileEncrypterDecrypter encrypterDecrypter = new SecureFileEncrypterDecrypter(password);
            File inputFile = new File("path/to/html/folder/input.html");
            File encryptedFile = new File("path/to/encrypted/folder/encrypted.html");
            File decryptedFile = new File("path/to/decrypted/folder/decrypted.html");

            encrypterDecrypter.encryptFile(inputFile, encryptedFile);
            System.out.println("File encrypted successfully.");

            encrypterDecrypter.decryptFile(encryptedFile, decryptedFile);
            System.out.println("File decrypted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
