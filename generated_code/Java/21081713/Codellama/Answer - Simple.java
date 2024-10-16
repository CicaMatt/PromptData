import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPublicKeySpec;
public class Main {
  public final static int pValue = 47;

  public final static int gValue = 71;

  public final static int XaValue = 9;

  public final static int XbValue = 14;

  public static void main(String[] args) throws Exception {
    BigInteger p = new BigInteger(Integer.toString(pValue));
    BigInteger g = new BigInteger(Integer.toString(gValue));
    BigInteger Xa = new BigInteger(Integer.toString(XaValue));
    BigInteger Xb = new BigInteger(Integer.toString(XbValue));

    int bitLength = 512; // 512 bits
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

    KeyFactory kfactory = KeyFactory.getInstance("DiffieHellman");

    DHPublicKeySpec kspec = (DHPublicKeySpec) kfactory.getKeySpec(kp.getPublic(),
        DHPublicKeySpec.class);

    // This is the public key that you need to share with your partner
    byte[] publicKeyBytes = kspec.getY().toByteArray();

    // This is the secret value that you need to keep private and not share it with anyone
    BigInteger sharedSecretValue = kspec.getX().add(kp.getPrivate()).modPow(p, g);

    System.out.println("Public key: " + new String(publicKeyBytes));
    System.out.println("Shared secret value: " + sharedSecretValue.toString());
  }
}