import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IPBoardPasswordChecker {

    // Dedicated exception for password mismatch
    public static class PasswordMismatchException extends Exception {
        public PasswordMismatchException(String message) {
            super(message);
        }
    }

    // Avoid storing passwords in plaintext
    public static void checkPassword(Connection connection, String username, char[] password) 
            throws SQLException, NoSuchAlgorithmException, PasswordMismatchException {

        String query = "SELECT members_pass_hash, members_pass_salt FROM core_members WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String storedHash = resultSet.getString("members_pass_hash");
                    String salt = resultSet.getString("members_pass_salt");

                    // Calculate the expected hash using IPBoard's method
                    String expectedHash = calculateIPBoardHash(salt, password);

                    // Securely compare hashes (avoid timing attacks)
                    if (!MessageDigest.isEqual(storedHash.getBytes(), expectedHash.getBytes())) {
                        throw new PasswordMismatchException("Password mismatch for user: " + username);
                    }
                } else {
                    throw new SQLException("User not found: " + username);
                }
            }
        } finally {
            // Clear the password from memory
            for (int i = 0; i < password.length; i++) {
                password[i] = 0;
            }
        }
    }

    private static String calculateIPBoardHash(String salt, char[] password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        
        // Hash the salt
        byte[] saltHash = md.digest(salt.getBytes());

        // Hash the password
        byte[] passwordHash = md.digest(new String(password).getBytes());

        // Concatenate the salt hash and password hash
        byte[] concatenatedHash = new byte[saltHash.length + passwordHash.length];
        System.arraycopy(saltHash, 0, concatenatedHash, 0, saltHash.length);
        System.arraycopy(passwordHash, 0, concatenatedHash, saltHash.length, passwordHash.length);

        // Hash the concatenated result
        byte[] finalHash = md.digest(concatenatedHash);

        // Convert the final hash to a hexadecimal string
        StringBuilder sb = new StringBuilder();
        for (byte b : finalHash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}