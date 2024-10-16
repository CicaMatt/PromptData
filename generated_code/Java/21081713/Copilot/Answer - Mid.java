import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPublicKeySpec;

public class DiffieHellmanExample {
    public static void main(String[] args) throws Exception {
        // Generate parameters for Diffie-Hellman
        int bitLength = 512; // 512 bits
        SecureRandom rnd = new SecureRandom();
        BigInteger p = BigInteger.probablePrime(bitLength, rnd);
        BigInteger g = BigInteger.probablePrime(bitLength, rnd);

        // Generate key pairs for two parties
        KeyPair keyPairA = generateKeyPair(p, g);
        KeyPair keyPairB = generateKeyPair(p, g);

        // Generate shared secret keys
        byte[] sharedSecretA = generateSharedSecret(keyPairA.getPrivate(), keyPairB.getPublic());
        byte[] sharedSecretB = generateSharedSecret(keyPairB.getPrivate(), keyPairA.getPublic());

        // Verify that both shared secrets are the same
        System.out.println("Shared secret A: " + new BigInteger(sharedSecretA).toString(16));
        System.out.println("Shared secret B: " + new BigInteger(sharedSecretB).toString(16));
    }

    public static KeyPair generateKeyPair(BigInteger p, BigInteger g) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("DiffieHellman");
        DHParameterSpec param = new DHParameterSpec(p, g);
        kpg.initialize(param);
        return kpg.generateKeyPair();
    }

    public static byte[] generateSharedSecret(PrivateKey privateKey, PublicKey publicKey) throws Exception {
        KeyAgreement keyAgree = KeyAgreement.getInstance("DiffieHellman");
        keyAgree.init(privateKey);
        keyAgree.doPhase(publicKey, true);
        return keyAgree.generateSecret();
    }
}
