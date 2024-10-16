from Crypto.Cipher import AES
from Crypto.Protocol.KDF import PBKDF2
from base64 import b64decode
import hashlib

# Parameters
password = "mypassword"
salt = b"your_salt"  # Replace with the actual salt used during encryption
iterations = 100000
key_length = 32  # AES-256 requires a 32-byte key

# Read the encrypted file
with open("crypt.txt", "rb") as f:
    encrypted_data = f.read()

# Decode the base64 encoded data
encrypted_data = b64decode(encrypted_data)

# Derive the key and IV using PBKDF2
key_iv = PBKDF2(password, salt, dkLen=key_length + AES.block_size, count=iterations, hmac_hash_module=hashlib.sha256)
key = key_iv[:key_length]
iv = key_iv[key_length:]

# Decrypt the data
cipher = AES.new(key, AES.MODE_CBC, iv)
decrypted_data = cipher.decrypt(encrypted_data)

# Remove padding
padding_length = decrypted_data[-1]
decrypted_data = decrypted_data[:-padding_length]

# Write the decrypted data to a file
with open("out.txt", "wb") as f:
    f.write(decrypted_data)

print("Decryption complete. Check the 'out.txt' file.")
