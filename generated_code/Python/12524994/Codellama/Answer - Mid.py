from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes

def encrypt(message, key):
    # Ensure the key length is 16, 24, or 32 bytes for AES
    if len(key) not in [16, 24, 32]:
        raise ValueError("Key must be 16, 24, or 32 bytes long.")
    
    # Generate a random nonce for AES.MODE_CTR (CTR mode uses a nonce)
    nonce = get_random_bytes(8)  # 8 bytes for the nonce in CTR mode
    
    # Create a new instance of the AES cipher
    cipher = AES.new(key, AES.MODE_CTR, nonce=nonce)
    
    # Encrypt the message (no padding needed for CTR mode)
    encrypted_message = cipher.encrypt(message)
    
    # Return both the nonce and the encrypted message
    return nonce + encrypted_message  # Prepend the nonce to the ciphertext

def decrypt(encrypted_message, key):
    # Ensure the key length is 16, 24, or 32 bytes for AES
    if len(key) not in [16, 24, 32]:
        raise ValueError("Key must be 16, 24, or 32 bytes long.")
    
    # Extract the nonce from the first 8 bytes
    nonce = encrypted_message[:8]
    ciphertext = encrypted_message[8:]
    
    # Create a new instance of the AES cipher
    cipher = AES.new(key, AES.MODE_CTR, nonce=nonce)
    
    # Decrypt the encrypted message
    decrypted_message = cipher.decrypt(ciphertext)
    
    return decrypted_message
