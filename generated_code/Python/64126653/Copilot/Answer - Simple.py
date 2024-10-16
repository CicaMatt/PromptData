from Crypto.Cipher import AES
from Crypto.Protocol.KDF import PBKDF2
from Crypto.Util.Padding import unpad
import base64

# Parameters
password = "mypassword"
# Replace with the actual salt used in OpenSSL
salt = b"your_salt_here"  
iterations = 100000
key_length = 32  # AES-256

# Read the encrypted file
with open("crypt.txt", "rb") as f:
    encrypted_data = base64.b64decode(f.read())

# Derive the key and IV using PBKDF2
key_iv = PBKDF2(password, salt, dkLen=key_length + AES.block_size, count=iterations)
key = key_iv[:key_length]
iv = key_iv[key_length:]

# Decrypt the data
cipher = AES.new(key, AES.MODE_CBC, iv)
decrypted_data = unpad(cipher.decrypt(encrypted_data), AES.block_size)

# Write the decrypted data to a file
with open("out.txt", "wb") as f:
    f.write(decrypted_data)

print("Decryption complete.")
