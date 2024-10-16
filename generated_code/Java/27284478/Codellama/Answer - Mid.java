import java.security.SecureRandom;
import java.util.Arrays;

public class PasswordGenerator {
  public static void main(String[] args) throws Exception {
    // Generate a random password using SecureRandom
    char[] password = new char[10];
    for (int i = 0; i < password.length; i++) {
      password[i] = (char) SecureRandom.getInstanceStrong().nextInt(128);
    }

    // Use the random password to authenticate with the backend
    Authenticator authenticator = new Authenticator();
    String token = authenticator.authenticate(new String(password));

    // Clean up the memory used by the random password
    Arrays.fill(password, '0');
  }
}