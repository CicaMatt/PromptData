from Crypto.Cipher import AES
from Crypto.Util.Padding import pad, unpad
from Crypto.Random import get_random_bytes
from hashlib import sha256

def ensure_key_length(key, length=32):
    """Ensure the key has the correct length by hashing it if necessary."""
    return sha256(key.encode()).digest()[:length]

def encrypt(message, key):
    # Ensure the key has the correct length for AES
    key = ensure_key_length(key, 32)  # AES-256 key length

    # Generate a random IV (16 bytes for AES)
    iv = get_random_bytes(16)

    # Create AES cipher object in CBC mode
    cipher = AES.new(key, AES.MODE_CBC, iv)

    # Pad the message to be a multiple of the AES block size (16 bytes)
    padded_message = pad(message.encode(), AES.block_size)

    # Encrypt the message
    ciphertext = cipher.encrypt(padded_message)

    # Return the IV and ciphertext together (IV is not secret)
    return iv + ciphertext

def decrypt(ciphertext, key):
    # Ensure the key has the correct length for AES
    key = ensure_key_length(key, 32)

    # Extract the IV from the first 16 bytes of the ciphertext
    iv = ciphertext[:16]

    # Extract the actual ciphertext
    encrypted_message = ciphertext[16:]

    # Create AES cipher object in CBC mode for decryption
    cipher = AES.new(key, AES.MODE_CBC, iv)

    # Decrypt and unpad the message
    padded_message = cipher.decrypt(encrypted_message)
    message = unpad(padded_message, AES.block_size)

    return message.decode()

# Example Usage:
key = "my_secure_key"
message = "Hello, world!"

# Encrypt
encrypted_message = encrypt(message, key)
print(f"Encrypted: {encrypted_message}")

# Decrypt
decrypted_message = decrypt(encrypted_message, key)
print(f"Decrypted: {decrypted_message}")
