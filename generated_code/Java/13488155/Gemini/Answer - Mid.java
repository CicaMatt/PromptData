import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class HTMLFileEncryptor {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int KEY_SIZE = 256;
    private static final int IV_SIZE = 16;

    // WARNING: Hardcoded key for demonstration ONLY. In production, use secure key management!
    private static final String HARDCODED_KEY = "ThisIsASecretKeyForEncryptionOnly";

    public static void main(String[] args) {
        String originalFolderPath = "path/to/original/html/files";
        String encryptedFolderPath = "path/to/encrypted/html/files";

        try {
            encryptFolder(originalFolderPath, encryptedFolderPath);
            // ... later, when you need to access an HTML file
            String decryptedHTMLContent = decryptFile(encryptedFolderPath, "filename.html");
            // Use decryptedHTMLContent in your Swing application
        } catch (Exception e) {
            e.printStackTrace(); // Handle errors appropriately in production
        }
    }

    private static void encryptFolder(String originalFolderPath, String encryptedFolderPath) throws Exception {
        File originalFolder = new File(originalFolderPath);
        File encryptedFolder = new File(encryptedFolderPath);

        if (!originalFolder.exists() || !originalFolder.isDirectory()) {
            throw new IllegalArgumentException("Original folder not found or is not a directory.");
        }

        if (!encryptedFolder.exists()) {
            encryptedFolder.mkdirs();
        }

        for (File file : originalFolder.listFiles()) {
            if (file.isFile()) {
                encryptFile(file, new File(encryptedFolderPath, file.getName()));
            }
        }
    }

    private static void encryptFile(File inputFile, File outputFile) throws Exception {
        SecretKeySpec secretKey = generateSecretKey(HARDCODED_KEY);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, generateIv());

        try (FileInputStream inputStream = new FileInputStream(inputFile);
             FileOutputStream outputStream = new FileOutputStream(outputFile)) {

            byte[] buffer = new byte[64];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byte[] output = cipher.update(buffer, 0, bytesRead);
                if (output != null) {
                    outputStream.write(output);
                }
            }
            byte[] outputBytes = cipher.doFinal();
            if (outputBytes != null) {
                outputStream.write(outputBytes);
            }
        }
    }

    private static String decryptFile(String encryptedFolderPath, String filename) throws Exception {
        SecretKeySpec secretKey = generateSecretKey(HARDCODED_KEY);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        File encryptedFile = new File(encryptedFolderPath, filename);

        if (!encryptedFile.exists() || !encryptedFile.isFile()) {
            throw new IllegalArgumentException("Encrypted file not found or is not a file.");
        }

        try (FileInputStream inputStream = new FileInputStream(encryptedFile)) {
            byte[] iv = new byte[IV_SIZE];
            inputStream.read(iv);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[64];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byte[] output = cipher.update(buffer, 0, bytesRead);
                if (output != null) {
                    outputStream.write(output);
                }
            }
            byte[] outputBytes = cipher.doFinal();
            if (outputBytes != null) {
                outputStream.write(outputBytes);
            }

            return new String(outputStream.toByteArray());
        }
    }

    private static SecretKeySpec generateSecretKey(String key) {
        byte[] keyBytes = key.getBytes();
        return new SecretKeySpec(keyBytes, "AES");
    }

    private static IvParameterSpec generateIv() {
        byte[] iv = new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
}