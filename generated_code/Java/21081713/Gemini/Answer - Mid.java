import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.SecureRandom;
import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPublicKeySpec;

public class DiffieHellmanKeyExchange {

    public static void main(String[] args) {
        SecureRandom rnd = new SecureRandom();
        BigInteger p = BigInteger.probablePrime(bitLength, rnd);
        BigInteger g = BigInteger.probablePrime(bitLength, rnd);

        // Create KeyPairs for Alice and Bob
        KeyPair aliceKeyPair = createKeyPair(p, g);
        KeyPair bobKeyPair = createKeyPair(p, g);

        // Perform the key agreement
        KeyAgreement aliceKeyAgreement = KeyAgreement.getInstance("DH");
        aliceKeyAgreement.init(aliceKeyPair.getPrivate());
        aliceKeyAgreement.doPhase(bobKeyPair.getPublic(), true);

        KeyAgreement bobKeyAgreement = KeyAgreement.getInstance("DH");
        bobKeyAgreement.init(bobKeyPair.getPrivate());
        bobKeyAgreement.doPhase(aliceKeyPair.getPublic(), true);

        // Generate the shared secret
        byte[] aliceSharedSecret = aliceKeyAgreement.generateSecret();
        byte[] bobSharedSecret = bobKeyAgreement.generateSecret();

        // Verify that the shared secrets are equal
        if (java.util.Arrays.equals(aliceSharedSecret, bobSharedSecret)) {
            System.out.println("Shared secrets are equal!");
        } else {
            System.out.println("Shared secrets are NOT equal!");
        }
    }

    public static KeyPair createKeyPair(BigInteger p, BigInteger g) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("DiffieHellman");
        DHParameterSpec param = new DHParameterSpec(p, g);
        kpg.initialize(param);
        return kpg.generateKeyPair();
    }
}