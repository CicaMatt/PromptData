import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;

public class RSAEncryptor {
    public static void main(String[] args) throws Exception {
        // Set the clear text password to be encrypted
        String clearTextPassword = "XXXXX";

        // Set the modulus and public exponent values provided by the web service team
        String modulusString = "...";
        String publicExponentString = "...";

        // Decode the modulus and public exponent from Base64 to BigInteger format
        Base64 base64Encoder = new Base64();
        byte[] modulusBytes = base64Encoder.decode(modulusString);
        byte[] publicExponentBytes = base64Encoder.decode(publicExponentString);
        BigInteger modulus = new BigInteger(modulusBytes);
        BigInteger publicExponent = new BigInteger(publicExponentBytes);

        // Create an RSA public key from the modulus and public exponent values
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, publicExponent);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        // Encrypt the clear text password using the RSA public key and Base64 encoding
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(clearTextPassword.getBytes("UTF-8"));
        String encodedEncryptedPassword = new String(base64Encoder.encode(encryptedBytes));

        // Print the encrypted password
        System.out.println("Encrypted Password: " + encodedEncryptedPassword);
    }
}