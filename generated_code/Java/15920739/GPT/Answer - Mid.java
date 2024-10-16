import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class TestRsa {

    private BigInteger p, q;
    private BigInteger n;
    private BigInteger phiN;
    private BigInteger e, d;

    public TestRsa() {
        initialize();
    }

    public void initialize() {
        int SIZE = 512;
        // Step 1: Select two large prime numbers. Say p and q.
        p = new BigInteger(SIZE, 15, new Random());
        q = new BigInteger(SIZE, 15, new Random());
        // Step 2: Calculate n = p.q
        n = p.multiply(q);
        // Step 3: Calculate ø(n) = (p - 1).(q - 1)
        phiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        // Step 4: Find e such that gcd(e, ø(n)) = 1 ; 1 < e < ø(n)
        do {
            e = new BigInteger(2 * SIZE, new Random());
        } while (e.compareTo(phiN) >= 0 || !e.gcd(phiN).equals(BigInteger.ONE));
        // Step 5: Calculate d such that e.d = 1 (mod ø(n))
        d = e.modInverse(phiN);
    }

    // Encrypts a BigInteger value (representing plaintext in bytes)
    public BigInteger encrypt(BigInteger plaintext) {
        return plaintext.modPow(e, n);
    }

    // Decrypts a BigInteger value (representing ciphertext in bytes)
    public BigInteger decrypt(BigInteger ciphertext) {
        return ciphertext.modPow(d, n);
    }

    // Method to encrypt a string message
    public BigInteger[] encryptMessage(String message) {
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        BigInteger[] encrypted = new BigInteger[bytes.length];

        for (int i = 0; i < bytes.length; i++) {
            encrypted[i] = encrypt(BigInteger.valueOf(bytes[i]));
        }

        return encrypted;
    }

    // Method to decrypt the encrypted BigInteger array and return the original string
    public String decryptMessage(BigInteger[] encrypted) {
        byte[] bytes = new byte[encrypted.length];

        for (int i = 0; i < encrypted.length; i++) {
            bytes[i] = decrypt(encrypted[i]).byteValue();
        }

        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws IOException {
        TestRsa app = new TestRsa();

        // Input message
        String plaintext = "Hello RSA!";
        System.out.println("Original Plaintext: " + plaintext);

        // Encrypt the message
        BigInteger[] encryptedMessage = app.encryptMessage(plaintext);
        System.out.println("Encrypted Message:");
        for (BigInteger part : encryptedMessage) {
            System.out.print(part.toString() + " ");
        }
        System.out.println();

        // Decrypt the message
        String decryptedMessage = app.decryptMessage(encryptedMessage);
        System.out.println("Decrypted Message: " + decryptedMessage);
    }
}
