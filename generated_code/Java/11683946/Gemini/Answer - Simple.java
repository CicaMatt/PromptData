import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;

public class AESWordEncrypt {

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

        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);

        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        String[] words = message.split(" ");
        StringBuilder encryptedString = new StringBuilder();

        for (String word : words) {
            byte[] encrypted = cipher.doFinal(word.getBytes());
            encryptedString.append(asHex(encrypted)).append(" ");
        }

        System.out.println("Original string -> " + message);
        System.out.println("Encrypted string -> " + encryptedString.toString().trim());
    }
}