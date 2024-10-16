import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        // Salt for the database
        String salt = "*nzFC";

        // Raw password provided by the user
        String rawPassword = "password123";

        // Generate a hash using the MD5 algorithm
        byte[] hash = generateMD5Hash(salt, rawPassword);

        // Print the hash to the console
        System.out.println("Generated hash: " + bytesToHex(hash));
    }

    private static byte[] generateMD5Hash(String salt, String rawPassword) throws NoSuchAlgorithmException {
        // Combine the salt and the raw password into a single string
        String combinedString = salt + rawPassword;

        // Use the MD5 algorithm to generate a hash from the combined string
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hash = md.digest(combinedString.getBytes());

        return hash;
    }

    private static String bytesToHex(byte[] bytes) {
        // Convert the byte array to a hexadecimal string
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}