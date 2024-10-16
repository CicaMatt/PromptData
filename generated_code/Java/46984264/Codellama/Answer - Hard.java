import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

// Eccezioni personalizzate
class EncryptionException extends Exception {
    public EncryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}

class DecryptionException extends Exception {
    public DecryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}

public class CrossDatabaseEncryption {
    private static final String ALGORITHM = "AES/CBC/NoPadding";  
    private static final int KEY_SIZE = 256;
    private static final int BLOCK_SIZE = 16;  

    // Chiave AES generata una sola volta
    private static SecretKey secretKey;

    static {
        try {
            secretKey = generateKey();
        } catch (Exception e) {
            throw new RuntimeException("Errore durante la generazione della chiave AES", e);
        }
    }

    // Genera una chiave AES
    private static SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(KEY_SIZE);
        return keyGen.generateKey();
    }

    // Genera un IV casuale
    private static byte[] generateIV() throws Exception {
        byte[] iv = new byte[BLOCK_SIZE];
        SecureRandom.getInstanceStrong().nextBytes(iv);
        return iv;
    }

    // Funzione per fare il padding del testo a multipli di BLOCK_SIZE
    private static byte[] pad(String plainText) {
        byte[] plainBytes = plainText.getBytes(StandardCharsets.UTF_8);
        int paddingLength = BLOCK_SIZE - (plainBytes.length % BLOCK_SIZE);
        byte[] paddedBytes = new byte[plainBytes.length + paddingLength];
        System.arraycopy(plainBytes, 0, paddedBytes, 0, plainBytes.length);
        // Riempie con byte di valore uguale al numero di byte di padding aggiunti
        for (int i = plainBytes.length; i < paddedBytes.length; i++) {
            paddedBytes[i] = (byte) paddingLength;
        }
        return paddedBytes;
    }

    // Funzione per rimuovere il padding durante la decifratura
    private static byte[] unpad(byte[] paddedData) {
        int paddingLength = paddedData[paddedData.length - 1];
        byte[] unpaddedData = new byte[paddedData.length - paddingLength];
        System.arraycopy(paddedData, 0, unpaddedData, 0, unpaddedData.length);
        return unpaddedData;
    }

    public static String encrypt(String plainText) throws EncryptionException {
        try {
            byte[] iv = generateIV();
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            // Pad the plainText to be a multiple of BLOCK_SIZE
            byte[] paddedPlainText = pad(plainText);
            byte[] encryptedData = cipher.doFinal(paddedPlainText);

            // Combina IV e dati cifrati
            byte[] combinedData = new byte[iv.length + encryptedData.length];
            System.arraycopy(iv, 0, combinedData, 0, iv.length);
            System.arraycopy(encryptedData, 0, combinedData, iv.length, encryptedData.length);

            return Base64.getEncoder().encodeToString(combinedData);
        } catch (Exception e) {
            throw new EncryptionException("Errore durante la cifratura del testo", e);
        }
    }

    public static String decrypt(String cipherText) throws DecryptionException {
        try {
            byte[] combinedData = Base64.getDecoder().decode(cipherText);
            byte[] iv = new byte[BLOCK_SIZE];
            byte[] encryptedData = new byte[combinedData.length - BLOCK_SIZE];

            // Estrae IV e dati cifrati
            System.arraycopy(combinedData, 0, iv, 0, iv.length);
            System.arraycopy(combinedData, BLOCK_SIZE, encryptedData, 0, encryptedData.length);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            byte[] paddedPlainText = cipher.doFinal(encryptedData);

            // Rimuove il padding
            byte[] plainText = unpad(paddedPlainText);
            return new String(plainText, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new DecryptionException("Errore durante la decifratura del testo", e);
        }
    }
}
