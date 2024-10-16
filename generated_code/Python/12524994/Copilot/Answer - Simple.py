from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes
from Crypto.Util.Padding import pad, unpad
import hashlib

def encrypt_message(message, key):
    # Ensure the key length is 16, 24, or 32 bytes using SHA-256 and truncation
    key = hashlib.sha256(key.encode()).digest()[:32]  # For AES-256
    
    # Generate a random IV
    iv = get_random_bytes(16)  # AES block size is 16 bytes
    
    # Create a new AES cipher object in CBC mode
    cipher = AES.new(key, AES.MODE_CBC, iv)
    
    # Pad the message to ensure it's a multiple of the block size
    padded_message = pad(message.encode(), AES.block_size)
    
    # Encrypt the padded message
    ciphertext = cipher.encrypt(padded_message)
    
    # Return both the IV and the ciphertext
    return iv + ciphertext  # IV should be prepended to the ciphertext for decryption


def decrypt_message(ciphertext_with_iv, key):
    # Ensure the key length is 16, 24, or 32 bytes using SHA-256 and truncation
    key = hashlib.sha256(key.encode()).digest()[:32]
    
    # Extract the IV from the first 16 bytes
    iv = ciphertext_with_iv[:16]
    ciphertext = ciphertext_with_iv[16:]
    
    # Create a new AES cipher object in CBC mode
    cipher = AES.new(key, AES.MODE_CBC, iv)
    
    # Decrypt the ciphertext
    decrypted_padded_message = cipher.decrypt(ciphertext)
    
    # Unpad the decrypted message
    message = unpad(decrypted_padded_message, AES.block_size).decode()
    
    return message
