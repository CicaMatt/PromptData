import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.*;

public class AESEncryptWordByWord {

    /**
     * Turns array of bytes into string (hex format)
     * 
     * @param buf Array of bytes to convert to hex string
     * @return Generated hex string
     */
    public static String asHex(byte buf[]) {
        StringBuilder strbuf = new StringBuilder(buf.length * 2);
        for (int i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10)
                strbuf.append("0");

            strbuf.append(Integer.toString((int) buf[i] & 0xff, 16));
        }
        return strbuf.toString();
    }

    public static void main(String[] args) throws Exception {
        String message = "Test text!";

        // Get the KeyGenerator
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128); // 192 and 256 bits may not be available

        // Generate the secret key specs.
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();

        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        System.out.println("Key: " + asHex(raw));

        // Instantiate the cipher
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        // Split the message into words
        String[] words = message.split(" ");
        StringBuilder encryptedMessage = new StringBuilder();

        // Encrypt each word individually
        for (String word : words) {
            byte[] encryptedWord = cipher.doFinal(word.getBytes());
            encryptedMessage.append(asHex(encryptedWord)).append(" ");
        }

        // Print the encrypted message
        System.out.println("Original string: " + message);
        System.out.println("Encrypted string: " + encryptedMessage.toString().trim());
    }
}
