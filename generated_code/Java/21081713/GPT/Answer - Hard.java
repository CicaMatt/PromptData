import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.*;
import javax.crypto.interfaces.*;
import javax.crypto.spec.DHParameterSpec;

public class DiffieHellmanWithExceptionHandling {

    public static void main(String[] args) {
        try {
            // Step 1: Create Diffie-Hellman parameters
            int keySize = 2048;  // Using 2048 bits for security
            AlgorithmParameterGenerator paramGen = AlgorithmParameterGenerator.getInstance("DH");
            paramGen.init(keySize);
            AlgorithmParameters params = paramGen.generateParameters();
            DHParameterSpec dhSpec = params.getParameterSpec(DHParameterSpec.class);

            // Step 2: Generate key pairs for both parties
            KeyPair aliceKeyPair = generateKeyPair(dhSpec);
            KeyPair bobKeyPair = generateKeyPair(dhSpec);

            // Step 3: Exchange public keys
            byte[] alicePublicKey = aliceKeyPair.getPublic().getEncoded();
            byte[] bobPublicKey = bobKeyPair.getPublic().getEncoded();

            // Step 4: Generate shared secret key on both sides
            SecretKey aliceSharedSecret = generateSharedSecret(aliceKeyPair.getPrivate(), bobPublicKey);
            SecretKey bobSharedSecret = generateSharedSecret(bobKeyPair.getPrivate(), alicePublicKey);

            // Ensure both shared secrets match
            System.out.println("Alice's Shared Secret: " + toHexString(aliceSharedSecret.getEncoded()));
            System.out.println("Bob's Shared Secret:   " + toHexString(bobSharedSecret.getEncoded()));

            // Check if both shared secrets are equal
            if (MessageDigest.isEqual(aliceSharedSecret.getEncoded(), bobSharedSecret.getEncoded())) {
                System.out.println("Shared secrets match!");
            } else {
                System.out.println("Shared secrets do NOT match.");
            }

        } catch (NoSuchAlgorithmException e) {
            System.err.println("Algorithm not available: " + e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
            System.err.println("Invalid algorithm parameters: " + e.getMessage());
        } catch (InvalidKeySpecException e) {
            System.err.println("Invalid Key Specification: " + e.getMessage());
        } catch (InvalidKeyException e) {
            System.err.println("Invalid Key: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println("Illegal state in key agreement: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    // Generate a Diffie-Hellman key pair using the provided DH parameters
    private static KeyPair generateKeyPair(DHParameterSpec dhSpec) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DH");
        keyPairGen.initialize(dhSpec);
        return keyPairGen.generateKeyPair();
    }

    // Generate a shared secret using the private key and the other party's public key
    private static SecretKey generateSharedSecret(PrivateKey privateKey, byte[] receivedPublicKeyBytes)
            throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, IllegalStateException {
        // Convert received public key bytes into a PublicKey object
        KeyFactory keyFactory = KeyFactory.getInstance("DH");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(receivedPublicKeyBytes);
        PublicKey receivedPublicKey = keyFactory.generatePublic(keySpec);

        // Initialize key agreement with the private key
        KeyAgreement keyAgree = KeyAgreement.getInstance("DH");
        keyAgree.init(privateKey);
        keyAgree.doPhase(receivedPublicKey, true);

        // Generate the shared secret (128-bit AES key in this case)
        return keyAgree.generateSecret("AES");
    }

    // Utility method to convert byte array to hex string for displaying purposes
    private static String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
