from Crypto import Random
from Crypto.Cipher import AES
import os

# Padding helper function
def pad(s):
    padding_size = AES.block_size - len(s) % AES.block_size
    return s + (bytes([padding_size]) * padding_size)

# Unpadding helper function
def unpad(s):
    return s[:-s[-1]]

# Encryption function
def encrypt(message, key=None, key_size=256):
    padded_message = pad(message)
    
    if key is None:
        key = Random.new().read(key_size // 8)
    
    iv = Random.new().read(AES.block_size)
    cipher = AES.new(key, AES.MODE_CBC, iv)
    
    return iv + cipher.encrypt(padded_message)

# Decryption function
def decrypt(ciphertext, key):
    iv = ciphertext[:AES.block_size]
    cipher = AES.new(key, AES.MODE_CBC, iv)
    plaintext = cipher.decrypt(ciphertext[AES.block_size:])
    
    return unpad(plaintext)

# Encrypt a file
def encrypt_file(file_name, key):
    with open(file_name, 'rb') as f:
        plaintext = f.read()
        
    encrypted_data = encrypt(plaintext, key)
    
    # Write encrypted data to file
    with open(file_name, 'wb') as f:
        f.write(encrypted_data)

# Decrypt a file
def decrypt_file(file_name, key):
    with open(file_name, 'rb') as f:
        encrypted_data = f.read()
    
    decrypted_data = decrypt(encrypted_data, key)
    
    # Write decrypted data to file
    with open(file_name, 'wb') as f:
        f.write(decrypted_data)

# Test the implementation with a key
key = b'\xbf\xc0\x85)\x10nc\x94\x02)j\xdf\xcb\xc4\x94\x9d(\x9e[EX\xc8\xd5\xbfI{\xa2$\x05(\xd5\x18'

# Example usage:
encrypt_file('to_enc.txt', key)  # Encrypt the file
decrypt_file('to_enc.txt', key)  # Decrypt the file
