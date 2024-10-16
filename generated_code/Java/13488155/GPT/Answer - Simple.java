import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Base64;

public class FileEncryptor {

    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 128;

    // This is a hardcoded secret key (for simplicity), but in a real-world scenario, store it securely.
    private static final String SECRET_KEY = "mySuperSecretKey123";

    public static void main(String[] args) throws Exception {
        // Path to the directory containing HTML files
        String folderPath = "C:/htmlFiles";
        
        // Encrypt files in the folder
        encryptFolder(folderPath);
        
        // Decrypt a file for use in the application
        String encryptedFilePath = "C:/htmlFiles/encryptedFile.html";
        String decryptedContent = decryptFile(encryptedFilePath);
        System.out.println("Decrypted Content: \n" + decryptedContent);
    }

    // Method to encrypt all files in a folder
    public static void encryptFolder(String folderPath) throws Exception {
        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().endsWith(".html")) {
                    encryptFile(file.getPath(), file.getPath() + ".enc");
                    file.delete();  // Remove original file after encryption
                }
            }
        }
    }

    // Method to encrypt a single file
    public static void encryptFile(String inputFilePath, String outputFilePath) throws Exception {
        byte[] fileData = Files.readAllBytes(Paths.get(inputFilePath));
        byte[] encryptedData = encrypt(fileData, SECRET_KEY);
        try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
            fos.write(encryptedData);
        }
        System.out.println("Encrypted file: " + outputFilePath);
    }

    // Method to decrypt a single file and return its content as a string
    public static String decryptFile(String filePath) throws Exception {
        byte[] fileData = Files.readAllBytes(Paths.get(filePath));
        byte[] decryptedData = decrypt(fileData, SECRET_KEY);
        return new String(decryptedData);
    }

    // AES encryption method
    private static byte[] encrypt(byte[] data, String secretKey) throws Exception {
        SecretKey key = generateKey(secretKey);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    // AES decryption method
    private static byte[] decrypt(byte[] data, String secretKey) throws Exception {
        SecretKey key = generateKey(secretKey);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    // Generate AES key from the secret string
    private static SecretKey generateKey(String secret) throws Exception {
        byte[] key = secret.getBytes();
        return new SecretKeySpec(key, 0, KEY_SIZE / 8, ALGORITHM);
    }

    // Securely generate random AES key (Optional, not used in this example)
    public static SecretKey generateRandomKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(KEY_SIZE, new SecureRandom());
        return keyGen.generateKey();
    }
}
