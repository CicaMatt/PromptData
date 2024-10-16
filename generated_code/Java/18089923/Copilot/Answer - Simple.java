import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class RSAEncryption {

    public static void main(String[] args) {
        try {
            // Clear text password
            String clearTextPassword = "XXXXX";

            // These are the actual values provided by the web service team
            String modulusString = "hm2oRCtP6usJKYpq7o1K20uUuL11j5xRrbV4FCQhn/JeXLT21laKK9901P69YUS3bLo64x8G1PkCfRtjbbZCIaa1Ci/BCQX8nF2kZVfrPyzcmeAkq4wsDthuZ+jPInknzUI3TQPAzdj6gim97E731i6WP0MHFqW6ODeQ6Dsp8pc=";
            String publicExponentString = "AQAB";

            // Decode the modulus and exponent from Base64
            BigInteger modulus = new BigInteger(1, Base64.decodeBase64(modulusString));
            BigInteger publicExponent = new BigInteger(1, Base64.decodeBase64(publicExponentString));

            // Generate the public key
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, publicExponent);
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            // Encrypt the password
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedBytes = cipher.doFinal(clearTextPassword.getBytes(StandardCharsets.UTF_8));

            // Encode the encrypted password to Base64
            String encodedEncryptedPassword = Base64.encodeBase64String(encryptedBytes);

            System.out.println("Encoded Encrypted Password: " + encodedEncryptedPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
