import java.security.*;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        // create the MessageDigest object for MD5 hash calculation
        MessageDigest md = MessageDigest.getInstance("MD5");

        // input password and salt
        String rawPassword = "mypassword";
        String salt = "*nzFC";

        // combine salt and password to get the final hash value
        byte[] combinedBytes = (salt + rawPassword).getBytes();

        // calculate MD5 hash of the combined bytes
        md.reset();
        byte[] hashedBytes = md.digest(combinedBytes);

        // convert the hashed bytes to a hexadecimal string
        StringBuffer sb = new StringBuffer();
        for (byte b : hashedBytes) {
            sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1,3));
        }

        // print the final hash value
        System.out.println("Final hash value: " + sb.toString());
    }
}