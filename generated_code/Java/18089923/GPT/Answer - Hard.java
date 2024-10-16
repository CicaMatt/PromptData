import javax.crypto.Cipher;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

public class RSAEncryption {
    public static void main(String[] args) {
        // Clear text password to encrypt
        String clearTextPassword = "XXXXX";

        // These are the actual values provided by the web service team
        String modulusString = "hm2oRCtP6usJKYpq7o1K20uUuL11j5xRrbV4FCQhn/JeXLT21laKK9901P69YUS3bLo64x8G1PkCfRtjbbZCIaa1Ci/BCQX8nF2kZVfrPyzcmeAkq4wsDthuZ+jPInknzUI3TQPAzdj6gim97E731i6WP0MHFqW6ODeQ6Dsp8pc=";
        String publicExponentString = "AQAB";

        // Decode the Base64 encoded modulus and exponent
        BigInteger modulus = new BigInteger(1, Base64.getDecoder().decode(modulusString));
        BigInteger publicExponent = new BigInteger(1, Base64.getDecoder().decode(publicExponentString));

        // Create RSA public key from modulus and public exponent
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, publicExponent);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        // Initialize cipher with RSA and PKCS1Padding
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        // Encrypt the clear text password
        byte[] encryptedBytes = cipher.doFinal(clearTextPassword.getBytes(StandardCharsets.UTF_8));

        // Encode the encrypted password to Base64
        String encodedEncryptedPassword = Base64.getEncoder().encodeToString(encryptedBytes);

        // Print the encrypted password
        System.out.println("Encrypted Password: " + encodedEncryptedPassword);
    }
}
