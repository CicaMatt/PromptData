import os
import base64
from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes

def encrypt(message, key):
    # Generate a random IV for each encryption operation
    iv = get_random_bytes(16)
    
    # Use the recommended AES-256 mode
    cipher = AES.new(key, AES.MODE_GCM, iv)
    
    # Pad the message to a multiple of 16 bytes
    padded_message = message + (16 - len(message) % 16) * chr(16 - len(message) % 16)
    
    # Encrypt the padded message using AES-256
    encrypted_message = cipher.encrypt(padded_message)
    
    # Return the base64 encoded encrypted message and IV
    return (base64.b64encode(iv + encrypted_message), iv)

def decrypt(encrypted_message, key):
    # Extract the IV from the encrypted message
    iv = encrypted_message[:16]
    
    # Use the recommended AES-256 mode
    cipher = AES.new(key, AES.MODE_GCM, iv)
    
    # Decrypt the encrypted message using AES-256
    decrypted_message = cipher.decrypt(encrypted_message[16:])
    
    # Remove any padding that may have been added
    unpadded_message = decrypted_message[:-ord(decrypted_message[-1])]
    
    # Return the decrypted message
    return unpadded_message