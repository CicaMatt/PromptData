import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

import javax.crypto.KeyAgreement;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPublicKeySpec;

public class Main {
    // ... (existing code) ...

    public static void main(String[] args) throws Exception {
        // ... (existing code) ...

        // Simulate two parties (Alice and Bob)
        KeyPair keyPairAlice = createSpecificKey(p, g);
        KeyPair keyPairBob = createSpecificKey(p, g);

        // Perform the key agreement
        KeyAgreement keyAgreementAlice = KeyAgreement.getInstance("DiffieHellman");
        keyAgreementAlice.init(keyPairAlice.getPrivate());
        keyAgreementAlice.doPhase(keyPairBob.getPublic(), true);

        KeyAgreement keyAgreementBob = KeyAgreement.getInstance("DiffieHellman");
        keyAgreementBob.init(keyPairBob.getPrivate());
        keyAgreementBob.doPhase(keyPairAlice.getPublic(), true);

        // Generate the shared secret key
        byte[] sharedSecretAlice = keyAgreementAlice.generateSecret();
        byte[] sharedSecretBob = keyAgreementBob.generateSecret();

        // Verify that both parties have the same shared secret
        if (java.util.Arrays.equals(sharedSecretAlice, sharedSecretBob)) {
            System.out.println("Shared secret key established successfully!");
            // You can now use the 'sharedSecretAlice' or 'sharedSecretBob' for further encryption/decryption
        } else {
            System.out.println("Key exchange failed!");
        }
    }

    // ... (existing code) ...
}