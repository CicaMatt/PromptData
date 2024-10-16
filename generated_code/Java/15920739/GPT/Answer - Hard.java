import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class TestRsa {

    private BigInteger p, q;
    private BigInteger n;
    private BigInteger PhiN;
    private BigInteger e, d;

    public TestRsa() {
        initialize();
    }

    // Initialize the RSA key pair (p, q, n, PhiN, e, d)
    public void initialize() {
        int SIZE = 512;
        /* Step 1: Select two large prime numbers p and q */
        p = new BigInteger(SIZE, 15, new Random());
        q = new BigInteger(SIZE, 15, new Random());
        /* Step 2: Calculate n = p * q */
        n = p.multiply(q);
        /* Step 3: Calculate Phi(n) = (p - 1) * (q - 1) */
        PhiN = p.subtract(BigInteger.valueOf(1)).multiply(q.subtract(BigInteger.valueOf(1)));
        /* Step 4: Find e such that gcd(e, PhiN) = 1 and 1 < e < PhiN */
        do {
            e = new BigInteger(2 * SIZE, new Random());
        } while (e.compareTo(PhiN) >= 0 || !e.gcd(PhiN).equals(BigInteger.ONE));
        /* Step 5: Calculate d such that e * d = 1 mod Phi(n) */
        d = e.modInverse(PhiN);
    }

    // Encrypt a plaintext message (converted to a BigInteger)
    public BigInteger encrypt(BigInteger plaintext) {
        return plaintext.modPow(e, n);
    }

    // Decrypt the ciphertext message (converted to a BigInteger)
    public BigInteger decrypt(BigInteger ciphertext) {
        return ciphertext.modPow(d, n);
    }

    // Convert a string to a BigInteger using UTF-8 encoding
    public BigInteger stringToBigInteger(String input) {
        return new BigInteger(input.getBytes(StandardCharsets.UTF_8));
    }

    // Convert a BigInteger back to a string using UTF-8 decoding
    public String bigIntegerToString(BigInteger input) {
        return new String(input.toByteArray(), StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws IOException {
        TestRsa app = new TestRsa();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter a message to encrypt: ");
        String inputMessage = reader.readLine();

        // Convert the input message to BigInteger for encryption
        BigInteger plaintext = app.stringToBigInteger(inputMessage);
        BigInteger ciphertext = app.encrypt(plaintext);

        System.out.println("Plaintext (as BigInteger): " + plaintext);
        System.out.println("Ciphertext (as BigInteger): " + ciphertext);

        // Decrypt the message
        BigInteger decryptedPlaintext = app.decrypt(ciphertext);
        String decryptedMessage = app.bigIntegerToString(decryptedPlaintext);

        System.out.println("Decrypted message: " + decryptedMessage);
    }
}
