from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes
from cryptography.hazmat.primitives import padding
from cryptography.hazmat.backends import default_backend
import binascii

# Inputs
iv = "7bde5a0f3f39fd658efc45de143cbc94"
password = "3e83b13d99bf0de6c6bde5ac5ca4ae68"
msg = "this is a message"

# Convert hex strings to bytes
iv_bytes = binascii.unhexlify(iv)
key_bytes = binascii.unhexlify(password)

# Pad the message to a multiple of the block size (16 bytes for AES-128)
padder = padding.PKCS7(128).padder()
padded_data = padder.update(msg.encode()) + padder.finalize()

# AES-128-CBC encryption
cipher = Cipher(algorithms.AES(key_bytes), modes.CBC(iv_bytes), backend=default_backend())
encryptor = cipher.encryptor()
encrypted_data = encryptor.update(padded_data) + encryptor.finalize()

# Encode the encrypted data in base64
encrypted_base64 = binascii.b2a_base64(encrypted_data).decode().strip()

# Output
print(f"IV: {iv}")
print(f"PWD: {password}")
print(f"MSG: {msg}")
print(f"OUT: {encrypted_base64}")
