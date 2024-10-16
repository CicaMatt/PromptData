import base64
import os
from cryptography.hazmat.primitives.kdf.pbkdf2 import PBKDF2HMAC
from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes

def decrypt_file(encrypted_file_path, password, iterations=100000):
    # Read the entire encrypted file (base64 encoded content)
    with open(encrypted_file_path, 'rb') as f:
        encrypted_data = f.read()
    
    # Base64 decode the data (OpenSSL uses -a flag for base64 encoding)
    encrypted_data = base64.b64decode(encrypted_data)
    
    # Extract the salt (first 8 bytes)
    salt = encrypted_data[:8]
    
    # Extract the IV (next 16 bytes)
    iv = encrypted_data[8:24]
    
    # The actual encrypted data follows after the salt and IV
    ciphertext = encrypted_data[24:]
    
    # Derive the AES key using PBKDF2 and SHA256
    kdf = PBKDF2HMAC(
        algorithm=hashes.SHA256(),
        length=32,  # AES-256 key size
        salt=salt,
        iterations=iterations,
        backend=default_backend()
    )
    
    # Convert the password to bytes and derive the key
    key = kdf.derive(password.encode())

    # Create the AES cipher in CBC mode using the derived key and IV
    cipher = Cipher(algorithms.AES(key), modes.CBC(iv), backend=default_backend())
    decryptor = cipher.decryptor()
    
    # Decrypt the ciphertext
    plaintext_padded = decryptor.update(ciphertext) + decryptor.finalize()

    # OpenSSL uses PKCS7 padding, we need to remove the padding manually
    padding_len = plaintext_padded[-1]
    plaintext = plaintext_padded[:-padding_len]

    return plaintext

# Example usage
password = "mypassword"
encrypted_file_path = "crypt.txt"
output_file_path = "out.txt"

# Decrypt the file
decrypted_data = decrypt_file(encrypted_file_path, password)

# Write the decrypted data to the output file
with open(output_file_path, 'wb') as f:
    f.write(decrypted_data)

print(f"Decryption complete. Output saved to {output_file_path}.")
