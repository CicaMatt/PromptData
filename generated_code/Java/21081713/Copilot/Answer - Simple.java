import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.KeyAgreement;
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

        KeyPair keyPairA = createSpecificKey(p, g);
        KeyPair keyPairB = createSpecificKey(p, g);

        byte[] sharedSecretA = generateSharedSecret(keyPairA.getPrivate(), keyPairB.getPublic());
        byte[] sharedSecretB = generateSharedSecret(keyPairB.getPrivate(), keyPairA.getPublic());

        System.out.println("Shared secret A: " + new BigInteger(sharedSecretA).toString(16));
        System.out.println("Shared secret B: " + new BigInteger(sharedSecretB).toString(16));
    }

    public static KeyPair createSpecificKey(BigInteger p, BigInteger g) throws Exception {
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
