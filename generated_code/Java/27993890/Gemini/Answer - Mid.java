import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class IPBoardPasswordChecker {

    public static void main(String[] args) {
        // Replace with the actual username and password
        String username = "your_username"; 
        String rawPassword = "your_password";

        try (Connection connection = DriverManager.getConnection("your_database_url", "your_database_user", "your_database_password")) {
            String query = "SELECT members_pass_salt, members_pass_hash FROM core_members WHERE name = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String salt = resultSet.getString("members_pass_salt");
                        String expectedHash = resultSet.getString("members_pass_hash");

                        // Use bcrypt for password verification if available in your IPBoard version
                        if (isBcryptHash(expectedHash)) {
                            if (verifyBcryptHash(rawPassword, expectedHash)) {
                                System.out.println("Password matches (bcrypt)!");
                            } else {
                                System.out.println("Password does not match (bcrypt).");
                            }
                        } else { 
                            // Fallback to legacy MD5 verification
                            String calculatedHash = calculateIPBoardHash(salt, rawPassword);
                            if (calculatedHash.equals(expectedHash)) {
                                System.out.println("Password matches (legacy MD5)!");
                                // Consider upgrading the user's password to a more secure hash
                            } else {
                                System.out.println("Password does not match (legacy MD5).");
                            }
                        }

                    } else {
                        System.out.println("User not found.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ... (rest of the code, including calculateIPBoardHash and md5 functions)

    public static boolean isBcryptHash(String hash) {
        return hash.startsWith("$2y$") || hash.startsWith("$2a$"); 
    }

    public static boolean verifyBcryptHash(String password, String hash) {
        try {
            return BCrypt.checkpw(password, hash); 
        } catch (IllegalArgumentException e) {
            return false; 
        }
    }

    // Add BCrypt library dependency to your project (e.g., jBCrypt)
}