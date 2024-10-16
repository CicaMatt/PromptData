import java.io.IOException;
import java.math.BigInteger;
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
        int size = 512;
        // Step 1: Select two large prime numbers. Say p and q.
        p = new BigInteger(size, 15, new Random());
        q = new BigInteger(size, 15, new Random());
        // Step 2: Calculate n = p * q
        n = p.multiply(q);
        // Step 3: Calculate φ(n) = (p - 1) * (q - 1)
        phiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        // Step 4: Find e such that gcd(e, φ(n)) = 1 ; 1 < e < φ(n)
        do {
            e = new BigInteger(2 * size, new Random());
        } while (e.compareTo(phiN) >= 0 || !e.gcd(phiN).equals(BigInteger.ONE));
        // Step 5: Calculate d such that e * d ≡ 1 (mod φ(n))
        d = e.modInverse(phiN);
    }

    public BigInteger encrypt(BigInteger plaintext) {
        return plaintext.modPow(e, n);
    }

    public BigInteger decrypt(BigInteger ciphertext) {
        return ciphertext.modPow(d, n);
    }

    public static void main(String[] args) {
        TestRsa app = new TestRsa();

        String plaintext;
        System.out.println("Enter any text : ");
        plaintext = new java.io.BufferedReader(new java.io.InputStreamReader(System.in)).readLine();

        BigInteger bplaintext, bciphertext;
        // Convert the String to a byte array and then to a BigInteger
        bplaintext = new BigInteger(plaintext.getBytes()); 

        bciphertext = app.encrypt(bplaintext);
        System.out.println("Plaintext : " + plaintext); 
        System.out.println("Ciphertext : " + bciphertext.toString());
        bplaintext = app.decrypt(bciphertext);

        // Convert the BigInteger back to a byte array and then to a String
        String decryptedText = new String(bplaintext.toByteArray()); 
        System.out.println("After Decryption Plaintext : " + decryptedText);
    }
}