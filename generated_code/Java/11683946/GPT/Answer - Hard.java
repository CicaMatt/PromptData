import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.StringTokenizer;

public class AESEncryptWordByWord {

    /**
     * Turns array of bytes into string
     * 
     * @param buf Array of bytes to convert to hex string
     * @return Generated hex string
     */
    public static String asHex(byte[] buf) {
        StringBuilder strbuf = new StringBuilder(buf.length * 2);
        for (byte b : buf) {
            if ((b & 0xff) < 0x10) strbuf.append("0");
            strbuf.append(Integer.toString(b & 0xff, 16));
        }
        return strbuf.toString();
    }

    public static void main(String[] args) throws Exception {

        // Input string to be encrypted word by word
        String message = "Test text!";
        if (args.length > 0) {
            message = args[0];
        }

        // Split the string into words
        StringTokenizer tokenizer = new StringTokenizer(message, " ");
        StringBuilder encryptedMessage = new StringBuilder();

        // Get the KeyGenerator and generate the secret key
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128); // Using AES-128 for compatibility
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();

        // Create a SecretKeySpec for the AES key
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

        // Instantiate the AES Cipher
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        // Process each word separately
        while (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken();
            byte[] encrypted = cipher.doFinal(word.getBytes());
            encryptedMessage.append(asHex(encrypted)).append(" ");
        }

        // Output the encrypted string
        System.out.println("Original string -> " + message);
        System.out.println("Encrypted string -> " + encryptedMessage.toString().trim());
    }
}
