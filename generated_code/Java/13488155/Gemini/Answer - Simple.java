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
    private static final String KEY_ALGORITHM = "AES";
    private static final int KEY_SIZE = 256;
    private static final int IV_SIZE = 16;

    // HARDCODED KEY AND IV FOR DECRYPTION (KEEP THIS SECURE)
    private static final String HARDCODED_KEY = "YOUR_HARDCODED_KEY_HERE"; // Replace with your actual key (32 bytes)
    private static final String HARDCODED_IV = "YOUR_HARDCODED_IV_HERE";   // Replace with your actual IV (16 bytes)

    public static void main(String[] args) throws Exception {

        String folderPath = "path/to/your/html/folder";

        // Encrypt the folder
        encryptFolder(folderPath);

        // Decrypt the folder (for demonstration, you'd likely do this in your Java application)
        decryptFolder(folderPath + "_encrypted");
    }

    private static void encryptFolder(String folderPath) throws Exception {
        Path folder = Paths.get(folderPath);

        // Generate a secure key and IV
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(KEY_SIZE);
        SecretKey secretKey = keyGenerator.generateKey();

        byte[] iv = new byte[IV_SIZE];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

        Files.walk(folder)
                .filter(Files::isRegularFile)
                .forEach(file -> {
                    try {
                        byte[] fileContent = Files.readAllBytes(file);
                        byte[] encryptedContent = cipher.doFinal(fileContent);

                        // Store the encrypted file with a new extension
                        String encryptedFilePath = file.toString() + "_encrypted";
                        Files.write(Paths.get(encryptedFilePath), encryptedContent);

                        // Optionally, delete the original file
                        // Files.delete(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        // Store the key and IV (in a real application, you'd need a secure way to store these)
        storeKeyAndIV(secretKey, iv, folderPath);
    }

    private static void decryptFolder(String encryptedFolderPath) throws Exception {
        Path encryptedFolder = Paths.get(encryptedFolderPath);

        // Retrieve the hardcoded key and IV (in your Java application)
        byte[] keyBytes = Base64.getDecoder().decode(HARDCODED_KEY);
        byte[] ivBytes = Base64.getDecoder().decode(HARDCODED_IV);

        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        Files.walk(encryptedFolder)
                .filter(Files::isRegularFile)
                .forEach(encryptedFile -> {
                    try {
                        byte[] encryptedContent = Files.readAllBytes(encryptedFile);
                        byte[] decryptedContent = cipher.doFinal(encryptedContent);

                        // Store the decrypted file, removing the "_encrypted" extension
                        String decryptedFilePath = encryptedFile.toString().replace("_encrypted", "");
                        Files.write(Paths.get(decryptedFilePath), decryptedContent);

                        // Optionally, delete the encrypted file
                        // Files.delete(encryptedFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    // In a real application, you'd need a secure way to store the key and IV
    private static void storeKeyAndIV(SecretKey key, byte[] iv, String folderPath) throws IOException {
        String keyFilePath = folderPath + "/key.txt";
        String ivFilePath = folderPath + "/iv.txt";

        Files.write(Paths.get(keyFilePath), Base64.getEncoder().encode(key.getEncoded()));
        Files.write(Paths.get(ivFilePath), Base64.getEncoder().encode(iv));
    }
}