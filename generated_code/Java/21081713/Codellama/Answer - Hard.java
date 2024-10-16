import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.SecureRandom;
import java.security.spec.DHParameterSpec;
import java.security.spec.DHPublicKeySpec;

public class DiffieHellman {
  private static final int P_VALUE = 47;
  private static final int G_VALUE = 71;

  public static void main(String[] args) {
    BigInteger p = new BigInteger(Integer.toString(P_VALUE));
    BigInteger g = new BigInteger(Integer.toString(G_VALUE));

    int bitLength = 512; 
    SecureRandom rnd = new SecureRandom();
    p = BigInteger.probablePrime(bitLength, rnd);
    g = BigInteger.probablePrime(bitLength, rnd);

    createSpecificKey(p, g);
  }

  public static void createSpecificKey(BigInteger p, BigInteger g) throws Exception {
    KeyPairGenerator kpg = KeyPairGenerator.getInstance("DiffieHellman");
    DHParameterSpec param = new DHParameterSpec(p, g);
    kpg.initialize(param);
    KeyPair kp = kpg.generateKeyPair();

    // Extract the public key from the key pair
    BigInteger publicKey = kp.getPublic().toString(16);

    // Create a DHKeyExchange object with the given public key
    DHKeyExchange exchange = new DHKeyExchange(publicKey, p, g);

    // Generate a shared secret key using the Diffie-Hellman algorithm
    BigInteger sharedSecretKey = exchange.generateSharedSecret();

    System.out.println("Shared secret key: " + sharedSecretKey);
  }
}