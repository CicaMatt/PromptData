import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.spec.RSAPublicKeySpec;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import org.apache.commons.codec.binary.Base64;

// The class where you want to encrypt the password
public class Encryption {
    public static String encrypt(String clearTextPass) throws Exception {
        // These values are provided by the web service team
        String modulusString = "...";
        String publicExponentString = "...";
        String encoding = "UTF-8";
        Base64 base64Encoder = new Base64();

        // convert hex string to BigInteger
        String modulusHex = new String(Hex.encodeHex(modulusString.getBytes(encoding)));
        String publicExponentHex = new String(Hex.encodeHex(publicExponentString.getBytes(encoding)));

        BigInteger modulus = new BigInteger(modulusHex, 16);
        BigInteger publicExponent = new BigInteger(publicExponentHex);

        // generate RSA key pair
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, publicExponent);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        // encrypt the clear text password using the RSA key pair
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        String encodedEncryptedPass = new String(base64Encoder.encode(cipher.doFinal(clearTextPass.getBytes(encoding))));

        // return the encrypted password in Base64 encoding
        return encodedEncryptedPass;
    }
}