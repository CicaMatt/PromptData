import java.io.File;
import java.io.IOException;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class EncryptFolder {
    public static void main(String[] args) throws IOException, DataLengthException, InvalidCipherTextException {
        // Get the folder path to encrypt
        String folderPath = "path/to/folder";

        // Create a new AES engine
        AESEngine engine = new AESEngine();

        // Set the encryption key and IV
        KeyParameter key = new KeyParameter(new byte[16]); // Replace with your own key
        ParametersWithIV iv = new ParametersWithIV(key, new byte[16]); // Replace with your own IV

        // Initialize the GCMBlockCipher
        GCMBlockCipher gcm = new GCMBlockCipher(engine);
        gcm.init(true, iv);

        // Loop through all files in the folder and encrypt them
        File folder = new File(folderPath);
        for (File file : folder.listFiles()) {
            if (file.isFile()) {
                // Read the file contents
                byte[] data = Files.readAllBytes(file.toPath());

                // Encrypt the data using GCMBlockCipher
                byte[] encryptedData = gcm.processBytes(data, 0, data.length);

                // Write the encrypted data to a new file with the same name and a .enc extension
                File encFile = new File(folderPath + "/" + file.getName() + ".enc");
                Files.write(encFile.toPath(), encryptedData);
            }
        }
    }
}

public class DecryptFolder {
    public static void main(String[] args) throws IOException, DataLengthException, InvalidCipherTextException {
        // Get the folder path to decrypt
        String folderPath = "path/to/folder";

        // Create a new AES engine
        AESEngine engine = new AESEngine();

        // Set the decryption key and IV
        KeyParameter key = new KeyParameter(new byte[16]); // Replace with your own key
        ParametersWithIV iv = new ParametersWithIV(key, new byte[16]); // Replace with your own IV

        // Initialize the GCMBlockCipher
        GCMBlockCipher gcm = new GCMBlockCipher(engine);
        gcm.init(false, iv);

        // Loop through all files in the folder and decrypt them
        File folder = new File(folderPath);
        for (File file : folder.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".enc")) {
                // Read the encrypted file contents
                byte[] data = Files.readAllBytes(file.toPath());

                // Decrypt the data using GCMBlockCipher
                byte[] decryptedData = gcm.processBytes(data, 0, data.length);

                // Write the decrypted data to a new file with the same name but without the .enc extension
                File decFile = new File(folderPath + "/" + file.getName().substring(0, file.getName().lastIndexOf(".")));
                Files.write(decFile.toPath(), decryptedData);
            }
        }
    }
}