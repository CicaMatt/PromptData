import javax.crypto.*;
import javax.crypto.spec.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

public class OpenSslEncryption {

    private static final int SALT_LENGTH = 16;
    private static final int IV_LENGTH = 12;
    private static final int KEY_LENGTH = 256;
    private static final int ITERATIONS = 10000;

    public static void main(String[] args) {
        try {
            String password = System.getenv("ENCRYPTION_PASSWORD");
            if (password == null || password.isEmpty()) {
                throw new IllegalStateException("Password not set in environment");
            }

            SecureRandom secureRandom = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            secureRandom.nextBytes(salt);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKey secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

            byte[] iv = new byte[IV_LENGTH];
            secureRandom.nextBytes(iv);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec);
            String message = "AMOUNT=10&TID=#19:23&CURRENCY=EUR&LANGUAGE=DE&SUCCESS_URL=http://some.url/success" + 
                             "&ERROR_URL=http://some.url/error&CONFIRMATION_URL=http://some.url/confirm&NAME=customer full name";
            byte[] cipherText = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));

            String encryptedData = Base64.getEncoder().encodeToString(salt) + ":" +
                                   Base64.getEncoder().encodeToString(iv) + ":" +
                                   Base64.getEncoder().encodeToString(cipherText);

            System.out.println("Encrypted Data: " + encryptedData);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
