import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class CMSHashManager {

    private static final String SALT = "3D5900AE-111A-45BE-96B3-D9E4606CA793";
    private static final int HASH_ITERATIONS = 10;

    // Method to generate the hashed password with the salt and iterations
    public static String getPasswordHash(String plaintextPassword) throws NoSuchAlgorithmException {
        String hashData = plaintextPassword;

        // Perform the hash with the salt and the iterations
        for (int hashLimit = 0; hashLimit < HASH_ITERATIONS; hashLimit++) {
            hashData = getHash(SALT + hashData);
        }

        return hashData;
    }

    // Method to verify the hashed password
    public static boolean verifyHashedPassword(String plaintextPassword, String encryptedPassword) throws NoSuchAlgorithmException {
        String hashedPassword = getPasswordHash(plaintextPassword);
        return encryptedPassword.equals(hashedPassword);
    }

    // Helper method to get the hash of the input data using SHA-512
    private static String getHash(String data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] hashData = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hashData); // Base64 encoding the hash
    }

    // Test the code
    public static void main(String[] args) {
        try {
            // Test input
            String plaintextPassword = "12345";
            String encryptedPassword = "NgkuakH7UsCQwGHMQOhVXI3nW6M+1AtREY4Qx35osQo87p/whZIzy8cZU7+R7XnmyzgMzLWSvX+rTiWzfGTPsA==";

            // Get the hash of the password
            String hashedPassword = getPasswordHash(plaintextPassword);
            System.out.println("Hashed Password: " + hashedPassword);

            // Verify the password
            boolean isPasswordValid = verifyHashedPassword(plaintextPassword, encryptedPassword);
            System.out.println("Password is valid: " + isPasswordValid);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
