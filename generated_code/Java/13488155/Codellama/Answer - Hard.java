import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.xml.bind.DatatypeConverter;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.SecureRandom;

public class EncryptFile {
    public static void main(String[] args) throws Exception {
        String inputPath = "path/to/input";
        String outputPath = "path/to/output";
        File inputFile = new File(inputPath);
        File outputFile = new File(outputPath);
        
        // Generate a random IV
        byte[] iv = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        
        // The encryption key (base64 encoded)
        byte[] keyBytes = DatatypeConverter.parseBase64Binary("YOUR_ENCRYPTION_KEY");
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

        // Use AES with CBC mode
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        FileInputStream inputStream = new FileInputStream(inputFile);
        FileOutputStream outputStream = new FileOutputStream(outputFile);

        // Write the IV to the output file (this will be needed for decryption)
        outputStream.write(iv);
        
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byte[] encryptedData = cipher.doFinal(buffer, 0, bytesRead);
            outputStream.write(encryptedData);
        }

        inputStream.close();
        outputStream.close();
    }
}
