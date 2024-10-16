import base64
from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes
from cryptography.hazmat.backends import default_backend

# Convert hex strings to bytes
iv = bytes.fromhex("7bde5a0f3f39fd658efc45de143cbc94")
password = bytes.fromhex("3e83b13d99bf0de6c6bde5ac5ca4ae68")
msg = "this is a message".encode()  # Convert message to bytes

# AES-128 uses a key of 16 bytes, so we slice the password to 16 bytes
key = password[:16]

# Create the AES-128-CBC cipher
cipher = Cipher(algorithms.AES(key), modes.CBC(iv), backend=default_backend())
encryptor = cipher.encryptor()

# AES encryption requires padding to make the plaintext a multiple of the block size (16 bytes)
# Implement PKCS7 padding (the same padding used by OpenSSL)
from cryptography.hazmat.primitives import padding

padder = padding.PKCS7(algorithms.AES.block_size).padder()
padded_msg = padder.update(msg) + padder.finalize()

# Encrypt the message
encrypted = encryptor.update(padded_msg) + encryptor.finalize()

# Base64 encode the result
encrypted_base64 = base64.b64encode(encrypted).decode()

# Output the result
print(f"IV: {iv.hex()}")
print(f"PWD: {password.hex()}")
print(f"MSG: {msg.decode()}")
print(f"OUT: {encrypted_base64}")
