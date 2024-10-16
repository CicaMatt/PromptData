from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives.kdf.pbkdf2 import PBKDF2HMAC
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes
from base64 import b64decode
import os

def decrypt_file(openssl_encrypted_file, output_file, password):
    # Open the encrypted file and read its contents
    with open(openssl_encrypted_file, 'rb') as f:
        data = f.read()
    
    # OpenSSL format places the salt after "Salted__"
    if data[:8] != b"Salted__":
        raise ValueError("Not a valid OpenSSL encrypted file")
    
    salt = data[8:16]  # The next 8 bytes are the salt
    ciphertext = data[16:]  # The rest is the ciphertext

    # Derive the key and IV from the password using PBKDF2
    backend = default_backend()
    kdf = PBKDF2HMAC(
        algorithm=hashes.SHA256(),
        length=32 + 16,  # 32 bytes for key, 16 bytes for IV
        salt=salt,
        iterations=100000,
        backend=backend
    )
    key_iv = kdf.derive(password.encode())  # key_iv contains key + IV
    key = key_iv[:32]  # First 32 bytes are the key
    iv = key_iv[32:]  # Next 16 bytes are the IV

    # Decrypt using AES-256-CBC
    cipher = Cipher(algorithms.AES(key), modes.CBC(iv), backend=backend)
    decryptor = cipher.decryptor()
    decrypted_padded = decryptor.update(ciphertext) + decryptor.finalize()

    # Remove PKCS7 padding
    padding_length = decrypted_padded[-1]
    decrypted = decrypted_padded[:-padding_length]

    # Write the decrypted content to the output file
    with open(output_file, 'wb') as f:
        f.write(decrypted)

# Example usage
decrypt_file("crypt.txt", "out.txt", "mypassword")
