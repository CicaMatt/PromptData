import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

public class Main {

  public static void main(String[] args) throws Exception {
    int bitLength = 512; // 512 bits
    SecureRandom rnd = new SecureRandom();
    
    // Generate p and g
    BigInteger p = BigInteger.probablePrime(bitLength, rnd);
    BigInteger g = BigInteger.probablePrime(bitLength, rnd);

    System.out.println("p: " + p);
    System.out.println("g: " + g);
    
    // Generate key pair for Alice
    KeyPairGenerator aliceKeyPairGen = KeyPairGenerator.getInstance("DiffieHellman");
    DHParameterSpec dhSpec = new DHParameterSpec(p, g);
    aliceKeyPairGen.initialize(dhSpec);
    KeyPair aliceKeyPair = aliceKeyPairGen.generateKeyPair();

    // Generate key pair for Bob
    KeyPairGenerator bobKeyPairGen = KeyPairGenerator.getInstance("DiffieHellman");
    bobKeyPairGen.initialize(dhSpec);
    KeyPair bobKeyPair = bobKeyPairGen.generateKeyPair();

    // Alice and Bob exchange their public keys
    PublicKey alicePubKey = aliceKeyPair.getPublic();
    PublicKey bobPubKey = bobKeyPair.getPublic();

    System.out.println("Alice Public Key: " + alicePubKey);
    System.out.println("Bob Public Key: " + bobPubKey);
    
    // Alice computes the shared secret using her private key and Bob's public key
    KeyAgreement aliceKeyAgree = KeyAgreement.getInstance("DiffieHellman");
    aliceKeyAgree.init(aliceKeyPair.getPrivate());
    aliceKeyAgree.doPhase(bobPubKey, true);
    byte[] aliceSharedSecret = aliceKeyAgree.generateSecret();

    // Bob computes the shared secret using his private key and Alice's public key
    KeyAgreement bobKeyAgree = KeyAgreement.getInstance("DiffieHellman");
    bobKeyAgree.init(bobKeyPair.getPrivate());
    bobKeyAgree.doPhase(alicePubKey, true);
    byte[] bobSharedSecret = bobKeyAgree.generateSecret();

    // Compare the shared secrets to make sure they are the same
    System.out.println("Alice Shared Secret: " + new BigInteger(1, aliceSharedSecret).toString(16));
    System.out.println("Bob Shared Secret: " + new BigInteger(1, bobSharedSecret).toString(16));

    if (MessageDigest.isEqual(aliceSharedSecret, bobSharedSecret)) {
      System.out.println("Shared secrets are identical!");
    } else {
      System.out.println("Shared secrets are different!");
    }
  }
}
