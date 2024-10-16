import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Random;

public class TestRsa {

    private BigInteger p, q;
    private BigInteger n;
    private BigInteger PhiN;
    private BigInteger e, d;

    public TestRsa() {
        initialize();
    }

    public void initialize() {
        int SIZE = 512;
        /* Step 1: Select two large prime numbers. Say p and q. */
        p = new BigInteger(SIZE, 15, new Random());
        q = new BigInteger(SIZE, 15, new Random());
        /* Step 2: Calculate n = p.q */
        n = p.multiply(q);
        /* Step 3: Calculate ø(n) = (p - 1).(q - 1) */
        PhiN = p.subtract(BigInteger.valueOf(1));
        PhiN = PhiN.multiply(q.subtract(BigInteger.valueOf(1)));
        /* Step 4: Find e such that gcd(e, ø(n)) = 1 ; 1 < e < ø(n) */
        do {
            e = new BigInteger(2 * SIZE, new Random());
        } while ((e.compareTo(PhiN) != 1)
                || (e.gcd(PhiN).compareTo(BigInteger.valueOf(1)) != 0));
        /* Step 5: Calculate d such that e.d = 1 (mod ø(n)) */
        d = e.modInverse(PhiN);
    }

    public BigInteger encrypt(BigInteger plaintext) {
        return plaintext.modPow(e, n);
    }

    public BigInteger decrypt(BigInteger ciphertext) {
        return ciphertext.modPow(d, n);
    }

    public static void main(String[] args) throws IOException {
        TestRsa app = new TestRsa();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        // Get the input plaintext string from the user
        System.out.println("Enter a plaintext message: ");
        String plaintext = reader.readLine();
        
        // Encrypting and decrypting each character
        StringBuilder ciphertext = new StringBuilder();
        StringBuilder decryptedText = new StringBuilder();

        System.out.println("Encrypting the message...");

        for (char c : plaintext.toCharArray()) {
            // Encrypt each character (convert to BigInteger first)
            BigInteger bplaintext = BigInteger.valueOf((int) c);
            BigInteger bciphertext = app.encrypt(bplaintext);
            
            // Store encrypted message
            ciphertext.append(bciphertext.toString()).append(" ");

            // Decrypt the ciphertext back to original character
            BigInteger decryptedValue = app.decrypt(bciphertext);
            decryptedText.append((char) decryptedValue.intValue());
        }

        // Display the results
        System.out.println("Original Plaintext: " + plaintext);
        System.out.println("Ciphertext: " + ciphertext.toString().trim());
        System.out.println("Decrypted Plaintext: " + decryptedText.toString());
    }
}
