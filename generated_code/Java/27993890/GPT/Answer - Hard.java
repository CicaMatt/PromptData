import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PasswordHashChecker {

    public static void main(String[] args) {
        // Example username and password
        String username = "testUser"; 
        String rawPassword = "examplePassword"; 
        String saltFromDatabase = "*nzFC"; 
        String hashFromDatabase = "6bac5cba673134ea084e481b57921134"; 

        // Compute the hash using the provided method
        String generatedHash = computePasswordHash(saltFromDatabase, rawPassword);

        if (generatedHash != null && generatedHash.equals(hashFromDatabase)) {
            System.out.println("Password matches.");
        } else {
            System.out.println("Password does not match.");
        }
    }

    /**
     * Computes the MD5 hash following the MD5(MD5(salt) + MD5(password)) logic.
     * @param salt The salt value from the database.
     * @param password The raw password input.
     * @return The computed hash or null if there was an error.
     */
    public static String computePasswordHash(String salt, String password) {
        try {
            // Generate MD5 of the salt
            String saltHash = MD5(salt);
            if (saltHash == null) return null; 

            // Generate MD5 of the raw password
            String passwordHash = MD5(password);
            if (passwordHash == null) return null;

            // Combine the two MD5 hashes and generate final MD5
            String combinedHashInput = saltHash + passwordHash;
            return MD5(combinedHashInput);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Computes the MD5 hash of a given input string.
     * @param input The input string to hash.
     * @return The MD5 hash in hexadecimal format or null if an error occurs.
     */
    public static String MD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array to hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("MD5 algorithm not found.");
            return null;
        }
    }

    /**
     * Checks if the provided username and password match the stored password hash in the database.
     * @param conn The SQL connection to the database.
     * @param username The username provided by the user.
     * @param rawPassword The raw password provided by the user.
     * @return True if the password matches, false otherwise.
     */
    public static boolean checkPassword(Connection conn, String username, String rawPassword) {
        String query = "SELECT members_pass_salt, members_pass_hash FROM forum_members WHERE name=?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String salt = rs.getString("members_pass_salt");
                String storedHash = rs.getString("members_pass_hash");

                // Compute the hash using the salt and the raw password
                String computedHash = computePasswordHash(salt, rawPassword);
                return storedHash.equals(computedHash);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
