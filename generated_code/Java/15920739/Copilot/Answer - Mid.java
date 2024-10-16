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

    public void initialize() {
        int size = 512;
        /* Step 1: Select two large prime numbers. Say p and q. */
        p = new BigInteger(size, 15, new Random());
        q = new BigInteger(size, 15, new Random());
        /* Step 2: Calculate n = p.q */
        n = p.multiply(q);
        /* Step 3: Calculate ø(n) = (p - 1).(q - 1) */
        PhiN = p.subtract(BigInteger.valueOf(1));
        PhiN = PhiN.multiply(q.subtract(BigInteger.valueOf(1)));
        /* Step 4: Find e such that gcd(e, ø(n)) = 1 ; 1 < e < ø(n) */
        do {
            e = new BigInteger(2 * size, new Random());
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

    public static void main(String[] args) {
        TestRsa app = new TestRsa();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter any text: ");
        String inputText = reader.readLine();
        byte[] plaintextBytes = inputText.getBytes(StandardCharsets.UTF_8);
        BigInteger plaintext = new BigInteger(plaintextBytes);
        BigInteger ciphertext = app.encrypt(plaintext);
        System.out.println("Plaintext: " + inputText);
        System.out.println("Ciphertext: " + ciphertext.toString());
        BigInteger decryptedText = app.decrypt(ciphertext);
        String decryptedString = new String(decryptedText.toByteArray(), StandardCharsets.UTF_8);
        System.out.println("After Decryption Plaintext: " + decryptedString);
    }
}
