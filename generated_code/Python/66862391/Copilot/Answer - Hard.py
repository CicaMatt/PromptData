from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes
from cryptography.hazmat.primitives import padding
from cryptography.hazmat.backends import default_backend
import base64

# Convert hex string to bytes
def hex_to_bytes(hex_string):
    return bytes.fromhex(hex_string)

# Input values
iv_hex = "7bde5a0f3f39fd658efc45de143cbc94"
password_hex = "3e83b13d99bf0de6c6bde5ac5ca4ae68"
msg = "this is a message"

# Convert hex values to bytes
iv = hex_to_bytes(iv_hex)
key = hex_to_bytes(password_hex)

# Pad the message to match block size requirements (AES block size is 128 bits)
padder = padding.PKCS7(algorithms.AES.block_size).padder()
padded_msg = padder.update(msg.encode()) + padder.finalize()

# Create cipher object and encrypt the message
cipher = Cipher(algorithms.AES(key), modes.CBC(iv), backend=default_backend())
encryptor = cipher.encryptor()
encrypted_msg = encryptor.update(padded_msg) + encryptor.finalize()

# Encode the encrypted message in base64
encrypted_msg_base64 = base64.b64encode(encrypted_msg).decode()

# Output the values
print(f"IV: {iv_hex}")
print(f"PWD: {password_hex}")
print(f"MSG: {msg}")
print(f"OUT: {encrypted_msg_base64}")
