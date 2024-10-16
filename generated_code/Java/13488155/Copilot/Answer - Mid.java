import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.*;
import java.util.Base64;

public class FileEncryptorDecryptor {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    public static void main(String[] args) {
        try {
            SecretKey secretKey = generateKey();
            String key = Base64.getEncoder().encodeToString(secretKey.getEncoded());

            // Encrypt the folder
            encryptFolder("path/to/html/folder", key);

            // Decrypt a specific file for demonstration
            decryptFile("path/to/encrypted/file.enc", "path/to/decrypted/file.html", key);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(256);
        return keyGen.generateKey();
    }

    public static void encryptFolder(String folderPath, String key) throws Exception {
        Files.walk(Paths.get(folderPath)).filter(Files::isRegularFile).forEach(filePath -> {
            try {
                encryptFile(filePath.toString(), filePath.toString() + ".enc", key);
                Files.delete(filePath); 
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void encryptFile(String inputFile, String outputFile, String key) throws Exception {
        doCrypto(Cipher.ENCRYPT_MODE, inputFile, outputFile, key);
    }

    public static void decryptFile(String inputFile, String outputFile, String key) throws Exception {
        doCrypto(Cipher.DECRYPT_MODE, inputFile, outputFile, key);
    }

    private static void doCrypto(int cipherMode, String inputFile, String outputFile, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(cipherMode, secretKey);

        try (FileInputStream inputStream = new FileInputStream(inputFile);
             FileOutputStream outputStream = new FileOutputStream(outputFile)) {

            byte[] inputBytes = new byte[(int) new File(inputFile).length()];
            inputStream.read(inputBytes);

            byte[] outputBytes = cipher.doFinal(inputBytes);

            outputStream.write(outputBytes);
        }
    }
}
