import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.util.Base64;

public class SecureHtmlFileManager {

    // Hardcoded AES key (128-bit). Ideally, this should be securely managed.
    private static final String AES_KEY = "1234567890123456";
    private static final String ALGORITHM = "AES";

    public static void main(String[] args) throws Exception {
        // Folder paths
        String inputFolderPath = "path/to/html/folder";
        String encryptedFolderPath = "path/to/encrypted/folder";
        String decryptedFolderPath = "path/to/decrypted/folder";

        // Encrypt folder
        encryptFolder(inputFolderPath, encryptedFolderPath);

        // Decrypt folder (use this for viewing or merging HTML files)
        decryptFolder(encryptedFolderPath, decryptedFolderPath);
    }

    // Encrypt the entire folder
    public static void encryptFolder(String inputFolderPath, String encryptedFolderPath) throws Exception {
        File folder = new File(inputFolderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new IllegalArgumentException("Invalid folder path");
        }

        File encryptedFolder = new File(encryptedFolderPath);
        if (!encryptedFolder.exists()) {
            encryptedFolder.mkdirs();
        }

        for (File file : folder.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".html")) {
                Path sourceFile = Paths.get(file.getAbsolutePath());
                Path encryptedFile = Paths.get(encryptedFolderPath, file.getName() + ".enc");
                encryptFile(sourceFile, encryptedFile);
                System.out.println("Encrypted: " + file.getName());
            }
        }
    }

    // Decrypt the entire folder
    public static void decryptFolder(String encryptedFolderPath, String decryptedFolderPath) throws Exception {
        File folder = new File(encryptedFolderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new IllegalArgumentException("Invalid folder path");
        }

        File decryptedFolder = new File(decryptedFolderPath);
        if (!decryptedFolder.exists()) {
            decryptedFolder.mkdirs();
        }

        for (File file : folder.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".enc")) {
                Path sourceFile = Paths.get(file.getAbsolutePath());
                String originalFileName = file.getName().replace(".enc", "");
                Path decryptedFile = Paths.get(decryptedFolderPath, originalFileName);
                decryptFile(sourceFile, decryptedFile);
                System.out.println("Decrypted: " + originalFileName);
            }
        }
    }

    // Encrypt a single file
    public static void encryptFile(Path sourceFile, Path encryptedFile) throws Exception {
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
        byte[] fileBytes = Files.readAllBytes(sourceFile);
        byte[] encryptedBytes = cipher.doFinal(fileBytes);

        Files.write(encryptedFile, encryptedBytes);
    }

    // Decrypt a single file
    public static void decryptFile(Path encryptedFile, Path decryptedFile) throws Exception {
        Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
        byte[] encryptedBytes = Files.readAllBytes(encryptedFile);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        Files.write(decryptedFile, decryptedBytes);
    }

    // Get the AES Cipher in the specified mode (Encrypt or Decrypt)
    private static Cipher getCipher(int mode) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(AES_KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(mode, keySpec);
        return cipher;
    }
}
