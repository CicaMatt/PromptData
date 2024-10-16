import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;

public class WordByWordAESEncrypt {

        /**
     * Turns array of bytes into string
     * 
     * @param buf Array of bytes to convert to hex string
     * @return Generated hex string
     */
    public static String asHex(byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        for (int i = 0; i < buf.length; i++) {
            if ((buf[i] & 0xff) < 0x10)
                strbuf.append("0");
            strbuf.append(Long.toString(buf[i] & 0xff, 16));
        }
        return strbuf.toString();
    }

    public static void main(String[] args) throws Exception {

        String message = "Test text!";

        // ... (Key generation and cipher initialization remain the same)

        String[] words = message.split("\\s+"); 
        StringBuilder encryptedMessage = new StringBuilder();

        for (String word : words) {
            byte[] encryptedWord = cipher.doFinal(word.getBytes());
            encryptedMessage.append(asHex(encryptedWord)).append(" "); 
        }

        System.out.println("Original string -> " + message);
        System.out.println("Encrypted string -> " + encryptedMessage.toString().trim()); 
    }
}