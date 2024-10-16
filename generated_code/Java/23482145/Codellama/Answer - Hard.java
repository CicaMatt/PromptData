import java.util.Random;

public class UniqueIdentifier {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int MAX_LENGTH = 11;

    public static String generateUniqueIdentifier(String userEmail, long currentTimestamp) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(MAX_LENGTH);
        for (int i = 0; i < MAX_LENGTH; i++) {
            int index = random.nextInt(ALPHABET.length());
            sb.append(ALPHABET.charAt(index));
        }
        return userEmail + "|" + currentTimestamp + "|" + sb.toString();
    }
}