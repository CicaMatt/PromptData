import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;

public class PasswordEncryptor {

    public String encryptPassword(String clearTextPassword, String modulusString, String publicExponentString) throws Exception {
        // Decodifica modulo ed esponente da Base64
        byte[] modulusBytes = Base64.getDecoder().decode(modulusString);
        byte[] publicExponentBytes = Base64.getDecoder().decode(publicExponentString);

        // Crea BigInteger direttamente dai byte decodificati
        BigInteger modulus = new BigInteger(1, modulusBytes);
        BigInteger publicExponent = new BigInteger(1, publicExponentBytes);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, publicExponent);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        // Codifica la password usando UTF-16LE come nel codice C#
        byte[] encryptedBytes = cipher.doFinal(clearTextPassword.getBytes("UTF-16LE"));

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Esempio di utilizzo
    public static void main(String[] args) throws Exception {
        PasswordEncryptor encryptor = new PasswordEncryptor();
        String clearTextPassword = "XXXXX";
        String modulusString = "..."; 
        String publicExponentString = "..."; 

        String encodedEncryptedPass = encryptor.encryptPassword(clearTextPassword, modulusString, publicExponentString);
        System.out.println("Password crittografata codificata: " + encodedEncryptedPass);
    }
}