import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;

public class RSAEncryption {
    public static void main(String[] args) throws Exception {
        // Initialize the modulus and public exponent values from the web service team

        // Base64-encoded modulus string and exponent
        String modulus = "..."; 
        String publicExponent = "..."; 

        // Decode the modulus and public exponent values from Base64 to BigIntegers
        BigInteger modulusBigInt = new BigInteger(modulus, 16);
        BigInteger publicExponentBigInt = new BigInteger(publicExponent, 16);

        // Create an RSAKeyFactory instance
        KeyFactory rsaKeyFactory = KeyFactory.getInstance("RSA");

        // Create a RSAPublicKeySpec instance with the modulus and public exponent values
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulusBigInt, publicExponentBigInt);

        // Generate an RSA public key from the public key spec
        RSAPublicKey rsaPublicKey = (RSAPublicKey) rsaKeyFactory.generatePublic(publicKeySpec);

        // Create a Cipher instance with the RSA/ECB/PKCS1Padding transformation
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

        // Initialize the cipher for encryption with the public key
        cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);

        // Encrypt the password using the RSA algorithm
        byte[] encryptedPassword = cipher.doFinal("password".getBytes("UTF-8"));

        // Base64 encode the encrypted password for transmission to the web service
        String base64EncodedEncryptedPassword = new String(Base64.encodeBase64(encryptedPassword));

        // Print the base64 encoded encrypted password
        System.out.println("Encrypted password: " + base64EncodedEncryptedPassword);
    }
}