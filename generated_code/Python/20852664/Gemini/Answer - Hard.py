from Crypto import Random
from Crypto.Cipher import AES
import base64 

# ... (your existing encrypt and decrypt functions)

def encrypt_file(file_name, key):
    with open(file_name, 'rb') as f:  # Open in binary mode for reading
        plaintext = f.read()
    enc = encrypt(plaintext, key)
    with open(file_name, 'wb') as f:  # Open in binary mode for writing
        f.write(base64.b64encode(enc))  # Encode binary data to safe text 

def decrypt_file(file_name, key):
    with open(file_name, 'r') as f:
        ciphertext = base64.b64decode(f.read())  # Decode back to binary
    dec = decrypt(ciphertext, key)
    with open(file_name, 'wb') as f:  
        f.write(dec)