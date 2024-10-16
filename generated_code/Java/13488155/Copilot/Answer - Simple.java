import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileEncrypterDecrypter {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private SecretKey secretKey;
    private Cipher cipher;

    public FileEncrypterDecrypter(SecretKey secretKey) throws Exception {
        this.secretKey = secretKey;
        this.cipher = Cipher.getInstance(ALGORITHM);
    }

    public void encryptFile(String inputFilePath, String outputFilePath) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] iv = cipher.getIV();
        try (FileInputStream fis = new FileInputStream(inputFilePath);
             FileOutputStream fos = new FileOutputStream(outputFilePath);
             CipherOutputStream cos = new CipherOutputStream(fos, cipher)) {
            fos.write(iv);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                cos.write(buffer, 0, bytesRead);
            }
        }
    }

    public void decryptFile(String inputFilePath, String outputFilePath) throws Exception {
        try (FileInputStream fis = new FileInputStream(inputFilePath)) {
            byte[] iv = new byte[16];
            fis.read(iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
            try (CipherInputStream cis = new CipherInputStream(fis, cipher);
                 FileOutputStream fos = new FileOutputStream(outputFilePath)) {
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
            SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();
            FileEncrypterDecrypter fileEncrypterDecrypter = new FileEncrypterDecrypter(secretKey);

            // Encrypt the HTML files
            Path inputDir = Paths.get("path/to/html/files");
            Path encryptedDir = Paths.get("path/to/encrypted/files");
            Files.createDirectories(encryptedDir);

            Files.list(inputDir).forEach(file -> {
                try {
                    String encryptedFilePath = encryptedDir.resolve(file.getFileName().toString() + ".enc").toString();
                    fileEncrypterDecrypter.encryptFile(file.toString(), encryptedFilePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            // Decrypt a specific HTML file for demonstration
            String encryptedFilePath = "path/to/encrypted/files/example.html.enc";
            String decryptedFilePath = "path/to/decrypted/files/example.html";
            fileEncrypterDecrypter.decryptFile(encryptedFilePath, decryptedFilePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
