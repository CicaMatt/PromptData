import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DiffieHellmanExample {
    public static void main(String[] args) throws Exception {
        // Generate the parameters for Diffie-Hellman
        int bitLength = 512; // You can use 2048 for stronger security
        SecureRandom rnd = new SecureRandom();
        BigInteger p = BigInteger.probablePrime(bitLength, rnd);
        BigInteger g = BigInteger.probablePrime(bitLength, rnd);

        // Alice creates a key pair
        KeyPairGenerator aliceKpg = KeyPairGenerator.getInstance("DiffieHellman");
        DHParameterSpec dhSpec = new DHParameterSpec(p, g);
        aliceKpg.initialize(dhSpec);
        KeyPair aliceKp = aliceKpg.generateKeyPair();

        // Bob creates a key pair
        KeyPairGenerator bobKpg = KeyPairGenerator.getInstance("DiffieHellman");
        bobKpg.initialize(dhSpec);
        KeyPair bobKp = bobKpg.generateKeyPair();

        // Alice initializes the key agreement protocol
        KeyAgreement aliceKeyAgree = KeyAgreement.getInstance("DiffieHellman");
        aliceKeyAgree.init(aliceKp.getPrivate());

        // Bob initializes the key agreement protocol
        KeyAgreement bobKeyAgree = KeyAgreement.getInstance("DiffieHellman");
        bobKeyAgree.init(bobKp.getPrivate());

        // Alice generates Bob's public key from the encoded form
        byte[] bobPublicKeyEncoded = bobKp.getPublic().getEncoded();
        KeyFactory aliceKeyFactory = KeyFactory.getInstance("DiffieHellman");
        X509EncodedKeySpec x509Spec = new X509EncodedKeySpec(bobPublicKeyEncoded);
        PublicKey bobPublicKey = aliceKeyFactory.generatePublic(x509Spec);

        // Bob generates Alice's public key from the encoded form
        byte[] alicePublicKeyEncoded = aliceKp.getPublic().getEncoded();
        KeyFactory bobKeyFactory = KeyFactory.getInstance("DiffieHellman");
        X509EncodedKeySpec x509SpecBob = new X509EncodedKeySpec(alicePublicKeyEncoded);
        PublicKey alicePublicKey = bobKeyFactory.generatePublic(x509SpecBob);

        // Alice and Bob perform phase one of the key agreement protocol
        aliceKeyAgree.doPhase(bobPublicKey, true);
        bobKeyAgree.doPhase(alicePublicKey, true);

        // Both parties compute the shared secret
        byte[] aliceSharedSecret = aliceKeyAgree.generateSecret();
        byte[] bobSharedSecret = bobKeyAgree.generateSecret();

        // Both Alice and Bob should have the same secret at this point
        System.out.println("Alice's shared secret: " + new BigInteger(1, aliceSharedSecret).toString(16));
        System.out.println("Bob's shared secret: " + new BigInteger(1, bobSharedSecret).toString(16));

        // Optionally, generate a secret key for AES encryption from the shared secret
        SecretKey aliceAesKey = new SecretKeySpec(aliceSharedSecret, 0, 16, "AES");
        SecretKey bobAesKey = new SecretKeySpec(bobSharedSecret, 0, 16, "AES");

        System.out.println("AES Key (Alice): " + new BigInteger(1, aliceAesKey.getEncoded()).toString(16));
        System.out.println("AES Key (Bob): " + new BigInteger(1, bobAesKey.getEncoded()).toString(16));
    }
}
