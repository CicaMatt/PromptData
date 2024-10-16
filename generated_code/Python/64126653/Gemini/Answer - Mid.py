from Crypto.Cipher import AES
from Crypto.Protocol.KDF import PBKDF2
import binascii

def decrypt_openssl_file(encrypted_filename, password):
    """Decrypts a file encrypted using OpenSSL AES-256-CBC with PBKDF2.

    Args:
        encrypted_filename: The name of the encrypted file.
        password: The password used for encryption.

    Returns:
        The decrypted data as bytes.
    """

    with open(encrypted_filename, 'rb') as f:
        # Read the salt and encrypted data from the file
        salt = f.read(16)  # OpenSSL uses a 16-byte salt
        encrypted_data = f.read()

    # Derive the key from the password and salt using PBKDF2
    key = PBKDF2(password, salt, 32, 100000)  # 32 bytes = 256 bits

    # Extract the IV (Initialization Vector) from the beginning of the encrypted data
    iv = encrypted_data[:16]
    ciphertext = encrypted_data[16:]

    # Create the AES cipher object and decrypt the data
    cipher = AES.new(key, AES.MODE_CBC, iv)
    decrypted_data = cipher.decrypt(ciphertext)

    # Remove padding (OpenSSL uses PKCS#7 padding by default)
    padding_length = decrypted_data[-1]
    decrypted_data = decrypted_data[:-padding_length]

    return decrypted_data

# Example usage
encrypted_filename = 'crypt.txt'
password = 'mypassword'

decrypted_data = decrypt_openssl_file(encrypted_filename, password)

# Write the decrypted data to a file or process it further
with open('out.txt', 'wb') as f:
    f.write(decrypted_data)

print("Decryption successful!")