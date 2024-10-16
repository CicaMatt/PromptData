from Crypto.Cipher import AES
import hashlib
import os

# Load the encrypted file into memory
with open("crypt.txt", "rb") as f:
    ciphertext = f.read()

# Derive the encryption key and IV from the password using PBKDF2-HMAC-SHA256
salt = b"\x00" * 16  # 16 bytes of zeroes
iterations = 100000  # number of iterations for PBKDF2
key_length = 32  # length of the encryption key (in bytes)
iv_length = 16  # length of the initialization vector (in bytes)
dk = hashlib.pbkdf2_hmac("sha256", "mypassword".encode(), salt, iterations, dklen=key_length + iv_length)
key = dk[:key_length]
iv = dk[key_length:]

# Decrypt the ciphertext using AES-CBC mode with the derived key and IV
aes = AES.new(key, AES.MODE_CBC, iv)
plaintext = aes.decrypt(ciphertext).decode()

# Write the plaintext to disk
with open("out.txt", "wb") as f:
    f.write(plaintext.encode())