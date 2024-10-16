from Crypto.Cipher import AES
import hashlib
import os

# Read the ciphertext from the file
with open("crypt.txt", "rb") as f:
    ciphertext = f.read()

# Derive the key and IV from the password
salt = b"mypassword"
key = hashlib.pbkdf2_hmac("sha1", salt, 100000)
iv = os.urandom(16)

# Decrypt the ciphertext using AES-CBC mode with PKCS5 padding
obj = AES.new(key, AES.MODE_CBC, iv)
plaintext = obj.decrypt(ciphertext)

# Save the plaintext to a file
with open("out.txt", "wb") as f:
    f.write(plaintext)