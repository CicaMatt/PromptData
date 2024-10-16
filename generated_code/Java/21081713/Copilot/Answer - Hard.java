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

public class DiffieHellmanExample {
    public static void main(String[] args) throws Exception {
        // Generazione dei parametri p e g
        int bitLength = 512; 
        SecureRandom rnd = new SecureRandom();
        BigInteger p = BigInteger.probablePrime(bitLength, rnd);
        BigInteger g = BigInteger.probablePrime(bitLength, rnd);

        // Generazione delle chiavi per Alice
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("DiffieHellman");
        DHParameterSpec param = new DHParameterSpec(p, g);
        kpg.initialize(param);
        KeyPair kpAlice = kpg.generateKeyPair();

        // Generazione delle chiavi per Bob
        KeyPair kpBob = kpg.generateKeyPair();

        // Estrazione delle chiavi pubbliche
        byte[] alicePubKeyEnc = kpAlice.getPublic().getEncoded();
        byte[] bobPubKeyEnc = kpBob.getPublic().getEncoded();

        // Scambio delle chiavi pubbliche
        KeyFactory keyFactory = KeyFactory.getInstance("DiffieHellman");
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(bobPubKeyEnc);
        PublicKey bobPubKey = keyFactory.generatePublic(x509KeySpec);

        x509KeySpec = new X509EncodedKeySpec(alicePubKeyEnc);
        PublicKey alicePubKey = keyFactory.generatePublic(x509KeySpec);

        // Generazione della chiave segreta condivisa per Alice
        KeyAgreement aliceKeyAgree = KeyAgreement.getInstance("DiffieHellman");
        aliceKeyAgree.init(kpAlice.getPrivate());
        aliceKeyAgree.doPhase(bobPubKey, true);
        byte[] aliceSharedSecret = aliceKeyAgree.generateSecret();

        // Generazione della chiave segreta condivisa per Bob
        KeyAgreement bobKeyAgree = KeyAgreement.getInstance("DiffieHellman");
        bobKeyAgree.init(kpBob.getPrivate());
        bobKeyAgree.doPhase(alicePubKey, true);
        byte[] bobSharedSecret = bobKeyAgree.generateSecret();

        // Verifica che le chiavi segrete condivise siano uguali
        System.out.println("Alice's shared secret: " + new BigInteger(1, aliceSharedSecret).toString(16));
        System.out.println("Bob's shared secret: " + new BigInteger(1, bobSharedSecret).toString(16));
    }
}
