from Crypto.Cipher import AES
from Crypto.Protocol.KDF import PBKDF2
import base64

# Parameters
password = "mypassword"
salt = b'salt'  # The salt used in OpenSSL
iterations = 100000
key_length = 32  # AES-256 requires a 32-byte key

# Read the encrypted file
with open("crypt.txt", "rb") as f:
    encrypted_data = base64.b64decode(f.read())

# Derive the key and IV from the password and salt
key_iv = PBKDF2(password, salt, dkLen=key_length + AES.block_size, count=iterations)
key = key_iv[:key_length]
iv = key_iv[key_length:key_length + AES.block_size]

# Decrypt the data
cipher = AES.new(key, AES.MODE_CBC, iv)
decrypted_data = cipher.decrypt(encrypted_data)

# Remove padding
padding_length = decrypted_data[-1]
decrypted_data = decrypted_data[:-padding_length]

# Write the decrypted data to a file
with open("out.txt", "wb") as f:
    f.write(decrypted_data)
