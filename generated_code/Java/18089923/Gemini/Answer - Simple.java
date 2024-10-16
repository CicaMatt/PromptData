import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;

public class PasswordEncryption {

    public static void main(String[] args) throws Exception {
        // Your clear text password
        String clearTextPassword = "XXXXX";

        // Base64-encoded modulus and exponent (provided by the web service team)
        String modulusString = "hm2oRCtP6usJKYpq7o1K20uUuL11j5xRrbV4FCQhn/JeXLT21laKK9901P69YUS3bLo64x8G1PkCfRtjbbZCIaa1Ci/BCQX8nF2kZVfrPyzcmeAkq4wsDthuZ+jPInknzUI3TQPAzdj6gim97E731i6WP0MHFqW6ODeQ6Dsp8pc=";
        String publicExponentString = "AQAB";

        // Decode Base64 to byte arrays
        byte[] modulusBytes = Base64.getDecoder().decode(modulusString);
        byte[] exponentBytes = Base64.getDecoder().decode(publicExponentString);

        // Create BigInteger objects from the byte arrays
        BigInteger modulus = new BigInteger(1, modulusBytes);
        BigInteger publicExponent = new BigInteger(1, exponentBytes);

        // Generate the public key
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, publicExponent);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        // Encrypt the password
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(clearTextPassword.getBytes("UTF-16LE")); 

        // Encode the encrypted bytes to Base64
        String encodedEncryptedPassword = Base64.getEncoder().encodeToString(encryptedBytes);

        System.out.println("Encoded Encrypted Password: " + encodedEncryptedPassword);
    }
}