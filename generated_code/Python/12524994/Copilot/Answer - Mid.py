from Crypto.Cipher import AES
from Crypto.Util.Padding import pad, unpad
from Crypto.Random import get_random_bytes
from hashlib import sha256

# Ensure the key is exactly 32 bytes (256 bits) by hashing the provided key
def prepare_key(key):
    return sha256(key.encode()).digest()

# Encrypt the message with the provided key
def encrypt_message(message, key):
    # Prepare the key
    key = prepare_key(key)
    
    # Convert message to bytes if it's not already
    if isinstance(message, str):
        message = message.encode()

    # Generate a random IV
    iv = get_random_bytes(16)  # AES block size is 16 bytes

    # Create AES cipher in CBC mode
    cipher = AES.new(key, AES.MODE_GCM, iv)

    # Pad the message to be a multiple of 16 bytes and encrypt it
    ciphertext = cipher.encrypt(pad(message, AES.block_size))

    # Return IV + ciphertext (IV is needed for decryption)
    return iv + ciphertext

# Decrypt the message with the provided key
def decrypt_message(ciphertext, key):
    # Prepare the key
    key = prepare_key(key)
    
    # Extract the IV from the start of the ciphertext (first 16 bytes)
    iv = ciphertext[:16]
    ciphertext = ciphertext[16:]

    # Create AES cipher in CBC mode using the extracted IV
    cipher = AES.new(key, AES.MODE_GCM, iv)

    # Decrypt and unpad the message
    decrypted_message = unpad(cipher.decrypt(ciphertext), AES.block_size)

    # Convert bytes to string and return
    return decrypted_message.decode()

# Example usage:
if __name__ == "__main__":
    original_message = "This is a secret message"
    key = "my_secret_key"

    # Encrypt the message
    encrypted = encrypt_message(original_message, key)
    print(f"Encrypted: {encrypted}")

    # Decrypt the message
    decrypted = decrypt_message(encrypted, key)
    print(f"Decrypted: {decrypted}")
