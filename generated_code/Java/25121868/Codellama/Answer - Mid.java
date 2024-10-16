import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;

public class CMSHashManager {
    private static final String _salt = "3D5900AE-111A-45BE-96B3-D9E4606CA793";
    private static final int _hashIterationsMax = 10;

    public static String GetPasswordHash(String plaintextPassword) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] hashData = digest.digest(plaintextPassword.getBytes("UTF-16LE"));
        return new String(hashData);
    }

    public static boolean VerifyHashedPassword(String plaintextPassword, String encryptedPassword) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] hashData = digest.digest(plaintextPassword.getBytes("UTF-16LE"));
        return encryptedPassword.equals(new String(hashData));
    }
}