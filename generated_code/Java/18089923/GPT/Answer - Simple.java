import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;

public class RSAEncryption {

    public static void main(String[] args) {
        try {
            // Clear text password
            String clearTextPassword = "XXXXX";

            // Provided by the web service team
            String modulusString = "hm2oRCtP6usJKYpq7o1K20uUuL11j5xRrbV4FCQhn/JeXLT21laKK9901P69YUS3bLo64x8G1PkCfRtjbbZCIaa1Ci/BCQX8nF2kZVfrPyzcmeAkq4wsDthuZ+jPInknzUI3TQPAzdj6gim97E731i6WP0MHFqW6ODeQ6Dsp8pc=";
            String publicExponentString = "AQAB";

            // Decode the modulus and public exponent
            BigInteger modulus = new BigInteger(1, Base64.decodeBase64(modulusString));
            BigInteger publicExponent = new BigInteger(1, Base64.decodeBase64(publicExponentString));

            // Create RSA public key specification
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, publicExponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            // Initialize the cipher with RSA/ECB/PKCS1Padding
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            // Encrypt the clear text password
            byte[] plainBytes = clearTextPassword.getBytes(StandardCharsets.UTF_16LE); // Use UTF-16LE as in the C# code
            byte[] encryptedBytes = cipher.doFinal(plainBytes);

            // Base64 encode the encrypted bytes
            String encodedEncryptedPassword = Base64.encodeBase64String(encryptedBytes);
            System.out.println("Encrypted password: " + encodedEncryptedPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
