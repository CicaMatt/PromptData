import java.math.BigInteger;
import java.security.*;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPublicKeySpec;

public class Main {

    public static void main(String[] args) throws Exception {
        int bitLength = 512; // 512 bits
        SecureRandom rnd = new SecureRandom();
        BigInteger p = BigInteger.probablePrime(bitLength, rnd);
        BigInteger g = BigInteger.probablePrime(bitLength, rnd);

        // Create KeyPairs for Alice and Bob
        KeyPair aliceKeyPair = createKeyPair(p, g);
        KeyPair bobKeyPair = createKeyPair(p, g);

        // Perform the Key Agreement
        KeyAgreement aliceKeyAgree = KeyAgreement.getInstance("DiffieHellman");
        aliceKeyAgree.init(aliceKeyPair.getPrivate());
        aliceKeyAgree.doPhase(bobKeyPair.getPublic(), true);

        KeyAgreement bobKeyAgree = KeyAgreement.getInstance("DiffieHellman");
        bobKeyAgree.init(bobKeyPair.getPrivate());
        bobKeyAgree.doPhase(aliceKeyPair.getPublic(), true);

        // Generate the shared secret
        byte[] aliceSharedSecret = aliceKeyAgree.generateSecret();
        byte[] bobSharedSecret = bobKeyAgree.generateSecret();

        // Verify that the shared secrets are the same
        System.out.println(aliceSharedSecret.equals(bobSharedSecret)); 
    }

    public static KeyPair createKeyPair(BigInteger p, BigInteger g) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("DiffieHellman");
        DHParameterSpec param = new DHParameterSpec(p, g);
        kpg.initialize(param);
        return kpg.generateKeyPair();
    }
}