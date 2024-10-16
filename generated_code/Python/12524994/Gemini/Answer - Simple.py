from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes
from Crypto.Util.Padding import pad, unpad

def encrypt(message, key):
    # Ensure the key is 16, 24, or 32 bytes long (for AES-128, AES-192, or AES-256)
    key = key[:32]  # Truncate if too long
    key = key.ljust(16, b'\0')  # Pad with null bytes if too short

    # Generate a random IV
    iv = get_random_bytes(AES.block_size)

    # Create the cipher object
    cipher = AES.new(key, AES.MODE_CBC, iv)

    # Pad the message and encrypt
    padded_message = pad(message.encode(), AES.block_size)
    ciphertext = cipher.encrypt(padded_message)

    # Return the IV and ciphertext concatenated
    return iv + ciphertext

def decrypt(ciphertext, key):
    # Ensure the key is the correct length
    key = key[:32]
    key = key.ljust(16, b'\0')

    # Extract the IV from the beginning of the ciphertext
    iv = ciphertext[:AES.block_size]
    ciphertext = ciphertext[AES.block_size:]

    # Create the cipher object
    cipher = AES.new(key, AES.MODE_CBC, iv)

    # Decrypt and unpad
    padded_message = cipher.decrypt(ciphertext)
    message = unpad(padded_message, AES.block_size)

    return message.decode()