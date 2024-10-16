import java.security.KeyGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

import java.util.Base64;

public class SecureAESEncryptWithGCM {

    public static void main(String[] args) throws Exception {
        String originalString = "Test text!";
        String[] words = originalString.split("\\s+");

        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = new SecureRandom();
        keyGen.init(256, secureRandom);
        SecretKey secretKey = keyGen.generateKey();

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        byte[] iv = new byte[12]; 
        secureRandom.nextBytes(iv);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv); 

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);

        System.out.println("Original string -> " + originalString);
        System.out.print("Encrypted string -> ");

        for (String word : words) {
            byte[] encryptedBytes = cipher.doFinal(word.getBytes());
            String encryptedWord = Base64.getEncoder().encodeToString(iv) + ":" +
                    Base64.getEncoder().encodeToString(encryptedBytes);
            System.out.print(encryptedWord + " ");
        }
    }
}