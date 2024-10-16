from Cryptodome.Cipher import AES
from Cryptodome.Random import get_random_bytes
from Cryptodome.Protocol.KDF import scrypt
from Cryptodome.Hash import SHA256
import base64

# Function to pad or trim key to ensure it has the correct length for AES (16, 24, or 32 bytes)
def derive_key(key: str, length: int = 32) -> bytes:
    return SHA256.new(key.encode()).digest()[:length]

# Encrypt function
def encrypt(message: str, key: str) -> str:
    # Derive a key of 32 bytes from the provided key string
    key_bytes = derive_key(key, 32)
    
    # Generate a random 12-byte IV (recommended length for GCM)
    iv = get_random_bytes(12)
    
    # Create a new AES cipher in GCM mode
    cipher = AES.new(key_bytes, AES.MODE_GCM, nonce=iv)
    
    # Encrypt the message and generate the authentication tag
    ciphertext, tag = cipher.encrypt_and_digest(message.encode())
    
    # Return the IV, ciphertext, and tag, base64 encoded for ease of transmission
    return base64.b64encode(iv + ciphertext + tag).decode()

# Decrypt function
def decrypt(encrypted_message: str, key: str) -> str:
    # Derive the same key used for encryption
    key_bytes = derive_key(key, 32)
    
    # Decode the base64 encoded encrypted message
    encrypted_data = base64.b64decode(encrypted_message)
    
    # Extract the IV, ciphertext, and tag
    iv = encrypted_data[:12]
    ciphertext = encrypted_data[12:-16]
    tag = encrypted_data[-16:]
    
    # Create a new AES cipher in GCM mode with the same IV (nonce)
    cipher = AES.new(key_bytes, AES.MODE_GCM, nonce=iv)
    
    # Decrypt the ciphertext and verify the tag
    try:
        decrypted_message = cipher.decrypt_and_verify(ciphertext, tag)
        return decrypted_message.decode()
    except ValueError:
        raise Exception("Incorrect decryption or authentication failed")

# Example usage
message = "This is a secret message."
key = "supersecretkey"
encrypted = encrypt(message, key)
print(f"Encrypted: {encrypted}")
decrypted = decrypt(encrypted, key)
print(f"Decrypted: {decrypted}")
