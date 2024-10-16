import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

public class PasswordEncryption {

    public static void main(String[] args) throws Exception {

        // my clear text password
        String clearTextPassword = "XXXXX";

        // these are the actual values I get from the web service team
        String modulusString = "hm2oRCtP6usJKYpq7o1K20uUuL11j5xRrbV4FCQhn/JeXLT21laKK9901P69YUS3bLo64x8G1PkCfRtjbbZCIaa1Ci/BCQX8nF2kZVfrPyzcmeAkq4wsDthuZ+jPInknzUI3TQPAzdj6gim97E731i6WP0MHFqW6ODeQ6Dsp8pc=";
        String publicExponentString = "AQAB";

        // Decode the modulus and exponent from Base64
        byte[] modulusBytes = Base64.getDecoder().decode(modulusString);
        byte[] exponentBytes = Base64.getDecoder().decode(publicExponentString);

        // Create BigIntegers from the byte arrays
        BigInteger modulus = new BigInteger(1, modulusBytes);
        BigInteger publicExponent = new BigInteger(1, exponentBytes);

        // Create an RSA public key spec
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, publicExponent);

        // Generate the public key
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        // Create a Cipher instance and initialize it for encryption using OAEP padding
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey, new OAEPParameterSpec("SHA-1", "MGF1", MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT));

        // Encrypt the password
        byte[] encryptedBytes = cipher.doFinal(clearTextPassword.getBytes(StandardCharsets.UTF_8));

        // Encode the encrypted bytes to Base64
        String encodedEncryptedPassword = Base64.getEncoder().encodeToString(encryptedBytes);

        System.out.println("Encoded Encrypted Password: " + encodedEncryptedPassword);
    }
}