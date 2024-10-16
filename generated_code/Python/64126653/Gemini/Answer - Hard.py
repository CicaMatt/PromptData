from Crypto.Cipher import AES
from Crypto.Protocol.KDF import PBKDF2
import binascii

def decrypt_openssl_file(encrypted_filename, password):
    """Decrypts a file encrypted with OpenSSL using AES-256-CBC, PBKDF2, and a salt.

    Args:
        encrypted_filename: The name of the encrypted file.
        password: The password used for encryption.

    Returns:
        The decrypted data as bytes.
    """

    with open(encrypted_filename, 'rb') as encrypted_file:
        # Read the encrypted file's header to extract salt and encrypted data
        salt = encrypted_file.read(16)  # OpenSSL uses a 16-byte salt
        encrypted_data = encrypted_file.read()

    # Derive the AES key from the password and salt using PBKDF2
    key = PBKDF2(password, salt, 32, 100000)  # 32 bytes for AES-256, 100000 iterations

    # Extract the initialization vector (IV) from the beginning of the encrypted data
    iv = encrypted_data[:16]
    ciphertext = encrypted_data[16:]

    # Create the AES cipher object and decrypt the data
    cipher = AES.new(key, AES.MODE_CBC, iv)
    decrypted_data = cipher.decrypt(ciphertext)

    # Remove padding (OpenSSL uses PKCS#7 padding by default)
    decrypted_data = _unpad_pkcs7(decrypted_data)

    return decrypted_data

def _unpad_pkcs7(padded_data):
    """Removes PKCS#7 padding from the data.

    Args:
        padded_data: The padded data.

    Returns:
        The unpadded data.
    """

    padding_length = padded_data[-1]
    if padding_length < 1 or padding_length > 32:
        raise ValueError("Invalid PKCS#7 padding")
    return padded_data[:-padding_length]

# Example usage
decrypted_data = decrypt_openssl_file("crypt.txt", "mypassword")

# Convert decrypted data to a string if it was originally text
try:
    decrypted_text = decrypted_data.decode('utf-8')
    print(decrypted_text)
except UnicodeDecodeError:
    # Handle the case where the decrypted data is not text
    print("Decrypted data is not text. It might be binary data.")

# Write the decrypted data to a file if needed
with open("out.txt", 'wb') as outfile:
    outfile.write(decrypted_data)