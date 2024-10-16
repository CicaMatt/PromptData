import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.util.Base64;

public class HTMLFileEncryptor {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding"; 
    private static final int KEY_SIZE = 256; 
    private static final int IV_SIZE = 16; 

    // Replace with your actual secret key (keep this VERY secure)
    private static final String SECRET_KEY = "ThisIsASecretKeyForEncryption";

    public static void encryptDirectory(Path sourceDirectory, Path encryptedDirectory) throws Exception {
        // Generate a secure key (ideally, you'd store and manage this key securely)
        SecretKeySpec secretKeySpec = generateSecretKey(SECRET_KEY);

        // Create an initialization vector (IV) for CBC mode
        IvParameterSpec ivParameterSpec = generateIv();

        // Initialize the cipher
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        // Recursively encrypt files in the directory
        Files.walk(sourceDirectory)
                .filter(Files::isRegularFile)
                .forEach(sourceFile -> {
                    try {
                        Path relativePath = sourceDirectory.relativize(sourceFile);
                        Path encryptedFile = encryptedDirectory.resolve(relativePath);
                        Files.createDirectories(encryptedFile.getParent());

                        try (InputStream inputStream = Files.newInputStream(sourceFile);
                             OutputStream outputStream = Files.newOutputStream(encryptedFile)) {

                            // Write the IV to the encrypted file (needed for decryption)
                            outputStream.write(ivParameterSpec.getIV());

                            // Encrypt and write the file contents
                            CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);
                            byte[] buffer = new byte[8192];
                            int bytesRead;
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                cipherOutputStream.write(buffer, 0, bytesRead);
                            }
                            cipherOutputStream.close(); 
                        }
                    } catch (Exception e) {
                        // Handle exceptions appropriately (e.g., log, display error message)
                        e.printStackTrace();
                    }
                });
    }

    public static void decryptDirectory(Path encryptedDirectory, Path decryptedDirectory) throws Exception {
        SecretKeySpec secretKeySpec = generateSecretKey(SECRET_KEY);

        Files.walk(encryptedDirectory)
                .filter(Files::isRegularFile)
                .forEach(encryptedFile -> {
                    try {
                        Path relativePath = encryptedDirectory.relativize(encryptedFile);
                        Path decryptedFile = decryptedDirectory.resolve(relativePath);
                        Files.createDirectories(decryptedFile.getParent());

                        try (InputStream inputStream = Files.newInputStream(encryptedFile);
                             OutputStream outputStream = Files.newOutputStream(decryptedFile)) {

                            // Read the IV from the encrypted file
                            byte[] iv = new byte[IV_SIZE];
                            inputStream.read(iv);
                            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

                            // Initialize the cipher for decryption
                            Cipher cipher = Cipher.getInstance(ALGORITHM);
                            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

                            // Decrypt and write the file contents
                            CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);
                            byte[] buffer = new byte[8192];
                            int bytesRead;
                            while ((bytesRead = cipherInputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, bytesRead);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private static SecretKeySpec generateSecretKey(String secretKey) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = sha.digest(secretKey.getBytes("UTF-8"));
        key = Arrays.copyOf(key, KEY_SIZE / 8); 
        return new SecretKeySpec(key, "AES");
    }

    private static IvParameterSpec generateIv() throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstanceStrong();
        byte[] iv = new byte[IV_SIZE];
        random.nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public static void main(String[] args) {
        try {
            Path sourceDirectory = Paths.get("path/to/your/html/files");
            Path encryptedDirectory = Paths.get("path/to/encrypted/folder");
            Path decryptedDirectory = Paths.get("path/to/decrypted/folder");

            encryptDirectory(sourceDirectory, encryptedDirectory);
            decryptDirectory(encryptedDirectory, decryptedDirectory);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}