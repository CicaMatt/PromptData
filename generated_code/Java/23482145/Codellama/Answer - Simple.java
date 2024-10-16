import java.util.Date;

public class UniqueIdentifier {
    public static void main(String[] args) {
        String email = "johndoe@example.com";
        long timestamp = new Date().getTime();

        // Generate a unique alphanumeric string
        String uniqueId = generateUniqueId(email, timestamp);

        System.out.println("Unique ID: " + uniqueId);
    }

    private static String generateUniqueId(String email, long timestamp) {
        // Use the email and current timestamp to generate a unique alphanumeric string
        return email + "-" + Long.toString(timestamp);
    }
}