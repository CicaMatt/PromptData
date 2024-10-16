import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class EncryptFile {
    public static void main(String[] args) throws Exception {
        String inputPath = "path/to/input";
        String outputPath = "path/to/output";
        File inputFile = new File(inputPath);
        File outputFile = new File(outputPath);
        byte[] keyBytes = DatatypeConverter.parseBase64Binary("YOUR_ENCRYPTION_KEY");
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        FileInputStream inputStream = new FileInputStream(inputFile);
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        byte[] buffer = new byte[1024];
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            byte[] encryptedData = cipher.doFinal(buffer, 0, read);
            outputStream.write(encryptedData);
        }
        inputStream.close();
        outputStream.close();
    }
}

public class DecryptFile {
    public static void main(String[] args) throws Exception {
        String inputPath = "path/to/input";
        String outputPath = "path/to/output";
        File inputFile = new File(inputPath);
        File outputFile = new File(outputPath);
        byte[] keyBytes = DatatypeConverter.parseBase64Binary("YOUR_ENCRYPTION_KEY");
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        FileInputStream inputStream = new FileInputStream(inputFile);
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        byte[] buffer = new byte[1024];
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            byte[] decryptedData = cipher.doFinal(buffer, 0, read);
            outputStream.write(decryptedData);
        }
        inputStream.close();
        outputStream.close();
    }
}
