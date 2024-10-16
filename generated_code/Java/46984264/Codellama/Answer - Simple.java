import java.util.Base64;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;

public class EncryptDecrypt {
    public static void main(String[] args) throws Exception {
        String plaintext = "This is my secret message";
        StandardPBEEncryptor encryptor = new StandardPBEEncryptor();
        EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
        config.setAlgorithm("AES-256");
        config.setKeySize(256);
        encryptor.init(config, "your_salt".getBytes());
        String encryptedText = encryptor.encrypt(plaintext);
        System.out.println("Encrypted text: " + encryptedText);
    }
}