import base64
from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes
from cryptography.hazmat.primitives import padding
import binascii

# Input data
iv_hex = "7bde5a0f3f39fd658efc45de143cbc94"
password_hex = "3e83b13d99bf0de6c6bde5ac5ca4ae68"
msg = "this is a message"

# Convert hex to bytes
iv = binascii.unhexlify(iv_hex)
key = binascii.unhexlify(password_hex)

# Add PKCS7 padding to the message
padder = padding.PKCS7(128).padder()
padded_data = padder.update(msg.encode()) + padder.finalize()

# Create AES cipher object
cipher = Cipher(algorithms.AES(key), modes.CBC(iv), backend=default_backend())
encryptor = cipher.encryptor()

# Encrypt the padded message
encrypted_data = encryptor.update(padded_data) + encryptor.finalize()

# Base64 encode the encrypted data
encrypted_base64 = base64.b64encode(encrypted_data).decode('utf-8')

# Output
print(f"IV: {iv_hex}")
print(f"PWD: {password_hex}")
print(f"MSG: {msg}")
print(f"OUT: {encrypted_base64}")
