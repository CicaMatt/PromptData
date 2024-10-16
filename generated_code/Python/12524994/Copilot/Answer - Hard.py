from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes
from hashlib import sha256

# Function to ensure the key is of valid size (AES expects 128, 192, or 256-bit keys)
def derive_key(key: str, key_size: int = 32) -> bytes:
    # Derive a 256-bit key using SHA-256 hash function
    return sha256(key.encode()).digest()[:key_size]

# Encryption function using AES-GCM (Galois/Counter Mode)
def encrypt_message(message: str, key: str) -> dict:
    # Convert the message to bytes
    message_bytes = message.encode('utf-8')

    # Derive a secure key from the provided key string
    derived_key = derive_key(key)

    # Generate a random nonce (used instead of IV in GCM mode)
    nonce = get_random_bytes(12)  # AES-GCM typically uses 12 bytes for the nonce

    # Create AES cipher object with the key and nonce using GCM mode
    cipher = AES.new(derived_key, AES.MODE_GCM, nonce=nonce)

    # Encrypt the message and compute the authentication tag
    ciphertext, tag = cipher.encrypt_and_digest(message_bytes)

    # Return the nonce, tag, and ciphertext (all are necessary for decryption)
    return {'nonce': nonce, 'ciphertext': ciphertext, 'tag': tag}

# Decryption function using AES-GCM
def decrypt_message(encrypted_data: dict, key: str) -> str:
    # Extract nonce, ciphertext, and tag from the encrypted data
    nonce = encrypted_data['nonce']
    ciphertext = encrypted_data['ciphertext']
    tag = encrypted_data['tag']

    # Derive the same key as used for encryption
    derived_key = derive_key(key)

    # Create AES cipher object with the same key and nonce using GCM mode
    cipher = AES.new(derived_key, AES.MODE_GCM, nonce=nonce)

    # Decrypt the ciphertext and verify the tag for integrity
    decrypted_message = cipher.decrypt_and_verify(ciphertext, tag)

    # Convert bytes back to string
    return decrypted_message.decode('utf-8')

# Example usage
if __name__ == "__main__":
    message = "This is a secret message."
    key = "mysecurekey"

    # Encrypt the message
    encrypted_data = encrypt_message(message, key)
    print("Encrypted:", encrypted_data)

    # Decrypt the message
    decrypted_message = decrypt_message(encrypted_data, key)
    print("Decrypted:", decrypted_message)
