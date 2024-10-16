import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordHasher {

    // Define the salt and the number of iterations, matching the C# code
    private static final String SALT = "3D5900AE-111A-45BE-96B3-D9E4606CA793";
    private static final int HASH_ITERATIONS = 10;

    // Method to get the password hash, similar to the C# GetPasswordHash
    public static String getPasswordHash(String plaintextPassword) throws NoSuchAlgorithmException {
        String hashData = plaintextPassword;

        for (int i = 0; i < HASH_ITERATIONS; i++) {
            hashData = getHash(SALT + hashData);
        }

        return hashData;
    }

    // Method to verify the hashed password, similar to the C# VerifyHashedPassword
    public static boolean verifyHashedPassword(String plaintextPassword, String encryptedPassword) throws NoSuchAlgorithmException {
        String hashData = getPasswordHash(plaintextPassword);
        return encryptedPassword.equals(hashData);
    }

    // Private method to perform the SHA-512 hashing and Base64 encoding, similar to C# GetHash
    private static String getHash(String unhashedData) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] hashData = digest.digest(unhashedData.getBytes(StandardCharsets.UTF_8));

        // Convert the byte array to Base64 string (like in the C# code)
        return Base64.getEncoder().encodeToString(hashData);
    }

    // Test function to verify the implementation
    public static void main(String[] args) {
        try {
            // Test the hashing and verification process
            String plaintext = "12345";
            String expectedEncrypted = "NgkuakH7UsCQwGHMQOhVXI3nW6M+1AtREY4Qx35osQo87p/whZIzy8cZU7+R7XnmyzgMzLWSvX+rTiWzfGTPsA==";

            // Hash the plaintext password
            String hashedPassword = getPasswordHash(plaintext);
            System.out.println("Hashed Password: " + hashedPassword);

            // Verify the password
            boolean isMatch = verifyHashedPassword(plaintext, expectedEncrypted);
            System.out.println("Password Match: " + isMatch);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
