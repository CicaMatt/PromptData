import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.SecureRandom;
import java.security.interfaces.DHPublicKey;
import java.security.interfaces.DHSecretKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPublicKeySpec;
import javax.crypto.spec.DHSecretKeySpec;

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

    DHPublicKey pubA = createPublicKey(p, g, Xa);
    DHSecretKey privA = createPrivateKey(p, g, Xa);
    DHPublicKey pubB = createPublicKey(p, g, Xb);
    DHSecretKey privB = createPrivateKey(p, g, Xb);

    // Alice and Bob agree on the shared secret key
    BigInteger sharedSecretA = agreeSharedSecret(pubB, privA);
    BigInteger sharedSecretB = agreeSharedSecret(pubA, privB);

    System.out.println("Shared secret A: " + sharedSecretA);
    System.out.println("Shared secret B: " + sharedSecretB);
  }

  public static DHPublicKey createPublicKey(BigInteger p, BigInteger g, BigInteger X) throws Exception {
    KeyFactory keyFactory = KeyFactory.getInstance("DiffieHellman");
    DHParameterSpec param = new DHParameterSpec(p, g);
    DHPublicKeySpec keySpec = new DHPublicKeySpec(X, param);
    return (DHPublicKey) keyFactory.generatePublic(keySpec);
  }

  public static DHSecretKey createPrivateKey(BigInteger p, BigInteger g, BigInteger X) throws Exception {
    KeyFactory keyFactory = KeyFactory.getInstance("DiffieHellman");
    DHParameterSpec param = new DHParameterSpec(p, g);
    DHPublicKeySpec keySpec = new DHPublicKeySpec(X, param);
    return (DHSecretKey) keyFactory.generatePrivate(keySpec);
  }

  public static BigInteger agreeSharedSecret(DHPublicKey pub, DHSecretKey priv) throws Exception {
    KeyAgreement ka = KeyAgreement.getInstance("DiffieHellman");
    ka.init(priv);
    ka.doPhase(pub, true);
    return ka.generateSecret();
  }
}