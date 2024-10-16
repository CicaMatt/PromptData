import base64
import hashlib
from Crypto.Cipher import AES
from Crypto.Protocol.KDF import PBKDF2

def decrypt_openssl_aes(encrypted_text, password, iterations=100000):
    # Base64 decode the encrypted text
    encrypted_data = base64.b64decode(encrypted_text)
    
    # Extract salt from the encrypted file
    if encrypted_data[:8] != b"Salted__":
        raise ValueError("Missing 'Salted__' header")
    salt = encrypted_data[8:16]
    
    # Derive key and IV using PBKDF2 with the provided password and salt
    key_iv = PBKDF2(password, salt, dkLen=32+16, count=iterations, hmac_hash_module=hashlib.sha256)
    key = key_iv[:32]  # AES-256 uses a 32-byte key
    iv = key_iv[32:48]  # AES-CBC uses a 16-byte IV
    
    # Decrypt the ciphertext
    cipher = AES.new(key, AES.MODE_CBC, iv)
    decrypted_data = cipher.decrypt(encrypted_data[16:])  # Strip off the Salted__ and salt bytes
    
    # Remove padding (PKCS7)
    padding_length = decrypted_data[-1]
    decrypted_data = decrypted_data[:-padding_length]
    
    return decrypted_data.decode('utf-8')

# Example usage
password = "mypassword"
with open("crypt.txt", "r") as encrypted_file:
    encrypted_text = encrypted_file.read()

# Decrypt the file
decrypted_text = decrypt_openssl_aes(encrypted_text, password)
print(decrypted_text)
