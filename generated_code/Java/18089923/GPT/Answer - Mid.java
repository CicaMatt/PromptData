import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;

public class RSAEncryption {

    public static void main(String[] args) {
        // The clear text password you want to encrypt
        String clearTextPassword = "XXXXX";

        // These are the actual values provided by the web service team
        String modulusString = "hm2oRCtP6usJKYpq7o1K20uUuL11j5xRrbV4FCQhn/JeXLT21laKK9901P69YUS3bLo64x8G1PkCfRtjbbZCIaa1Ci/BCQX8nF2kZVfrPyzcmeAkq4wsDthuZ+jPInknzUI3TQPAzdj6gim97E731i6WP0MHFqW6ODeQ6Dsp8pc=";
        String publicExponentString = "AQAB";

        // Decode the Base64 encoded modulus and exponent
        Base64 base64 = new Base64();
        BigInteger modulus = new BigInteger(1, base64.decode(modulusString));
        BigInteger publicExponent = new BigInteger(1, base64.decode(publicExponentString));

        // Generate the public key spec using the modulus and exponent
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, publicExponent);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        // Initialize the Cipher with the RSA algorithm and PKCS1Padding (matches C# default)
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        // Encrypt the password
        byte[] encryptedPassword = cipher.doFinal(clearTextPassword.getBytes(StandardCharsets.UTF_8));

        // Encode the encrypted password to Base64 (this is the output expected by the web service)
        String encodedEncryptedPassword = base64.encodeToString(encryptedPassword);

        // Print the encrypted password
        System.out.println("Encrypted Password: " + encodedEncryptedPassword);
    }
}
