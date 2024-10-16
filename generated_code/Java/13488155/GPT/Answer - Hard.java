import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class SecureHTMLFileApp extends JFrame {
    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static final int AES_KEY_SIZE = 256; 
    private static final int GCM_TAG_LENGTH = 128;
    private static final int GCM_IV_LENGTH = 12; 
    private static final int SALT_LENGTH = 16;
    private static final int PBKDF2_ITERATIONS = 65536;

    public SecureHTMLFileApp() {
        // Initialize Swing components
        setTitle("Secure HTML File Application");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JButton encryptButton = new JButton("Encrypt Folder");
        JButton decryptButton = new JButton("Decrypt File");

        encryptButton.addActionListener(e -> encryptFolder());
        decryptButton.addActionListener(e -> decryptFile());

        add(encryptButton);
        add(decryptButton);
    }

    // Generate a key from a user-provided password using PBKDF2
    private SecretKey deriveKey(String password, byte[] salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, PBKDF2_ITERATIONS, AES_KEY_SIZE);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), ENCRYPTION_ALGORITHM);
    }

    // Encrypt the entire folder containing HTML files
    private void encryptFolder() {
        JFileChooser folderChooser = new JFileChooser();
        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = folderChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File folder = folderChooser.getSelectedFile();
            String password = getPasswordFromUser("Enter password to encrypt:");
            if (password == null) return;

            for (File file : folder.listFiles((dir, name) -> name.endsWith(".html"))) {
                try {
                    byte[] fileBytes = Files.readAllBytes(file.toPath());

                    // Generate random salt and IV for each file
                    byte[] salt = new byte[SALT_LENGTH];
                    byte[] iv = new byte[GCM_IV_LENGTH];
                    SecureRandom random = new SecureRandom();
                    random.nextBytes(salt);
                    random.nextBytes(iv);

                    SecretKey key = deriveKey(password, salt);
                    byte[] encryptedBytes = encrypt(fileBytes, key, iv);

                    // Prepend salt and IV to the encrypted file
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    outputStream.write(salt);
                    outputStream.write(iv);
                    outputStream.write(encryptedBytes);

                    Files.write(file.toPath(), outputStream.toByteArray());
                    JOptionPane.showMessageDialog(this, "Files encrypted successfully!");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error encrypting file: " + file.getName());
                }
            }
        }
    }

    // Decrypt a selected file for client access (on-demand decryption)
    private void decryptFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String password = getPasswordFromUser("Enter password to decrypt:");
            if (password == null) return;

            try {
                byte[] fileBytes = Files.readAllBytes(file.toPath());

                // Extract salt and IV from the file
                byte[] salt = new byte[SALT_LENGTH];
                byte[] iv = new byte[GCM_IV_LENGTH];
                System.arraycopy(fileBytes, 0, salt, 0, SALT_LENGTH);
                System.arraycopy(fileBytes, SALT_LENGTH, iv, 0, GCM_IV_LENGTH);

                SecretKey key = deriveKey(password, salt);
                byte[] encryptedBytes = new byte[fileBytes.length - SALT_LENGTH - GCM_IV_LENGTH];
                System.arraycopy(fileBytes, SALT_LENGTH + GCM_IV_LENGTH, encryptedBytes, 0, encryptedBytes.length);

                byte[] decryptedBytes = decrypt(encryptedBytes, key, iv);
                String decryptedContent = new String(decryptedBytes, StandardCharsets.UTF_8);

                // Show the decrypted content (or use it in your application)
                JTextArea textArea = new JTextArea(decryptedContent);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(380, 150));

                JFrame outputFrame = new JFrame("Decrypted HTML Content");
                outputFrame.setSize(400, 200);
                outputFrame.add(scrollPane);
                outputFrame.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error decrypting file: " + file.getName());
            }
        }
    }

    // Encryption method with AES-GCM
    private byte[] encrypt(byte[] data, SecretKey key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        return cipher.doFinal(data);
    }

    // Decryption method with AES-GCM
    private byte[] decrypt(byte[] data, SecretKey key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        return cipher.doFinal(data);
    }

    // Helper method to get password input from user
    private String getPasswordFromUser(String message) {
        JPasswordField pf = new JPasswordField();
        int okCxl = JOptionPane.showConfirmDialog(null, pf, message, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (okCxl == JOptionPane.OK_OPTION) {
            return new String(pf.getPassword());
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SecureHTMLFileApp app = new SecureHTMLFileApp();
            app.setVisible(true);
        });
    }
}
