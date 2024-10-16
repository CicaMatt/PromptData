import java.security.KeyGenerator;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

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
        // Get the KeyGenerator
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128); 

        // Generate the secret key specs.
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

        // Print out the AES key in hexadecimal format
        System.out.println("Key: " + asHex(raw));

        // Instantiate the cipher
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        // Input string to be encrypted word by word
        String inputString = "Test text!";
        if (args.length > 0) {
            inputString = args[0]; 
        }

        // Split the input string into words
        String[] words = inputString.split(" ");

        // Encrypt each word separately
        for (String word : words) {
            byte[] encryptedWord = cipher.doFinal(word.getBytes());
            System.out.println("Encrypted word: " + asHex(encryptedWord));
        }
    }
}
