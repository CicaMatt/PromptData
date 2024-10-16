import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;

public class AESEncryptWordByWord {

    /**
     * Turns array of bytes into string
     * 
     * @param buf
     *            Array of bytes to convert to hex string
     * @return Generated hex string
     */
    public static String asHex(byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        for (int i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10)
                strbuf.append("0");
            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
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
        String[] words = message.split("\\s+");

        System.out.println("Original string -> " + message);
        System.out.print("Encrypted string -> ");

        // Encrypt each word separately and print the encrypted string
        for (String word : words) {
            byte[] encrypted = cipher.doFinal(word.getBytes());
            System.out.print(asHex(encrypted) + " ");
        }

        System.out.println(); // for a new line
    }
}
