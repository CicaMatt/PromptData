from cryptography.hazmat.primitives import padding
from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes
from cryptography.hazmat.backends import default_backend

import base64

def encrypt_message(message, password, iv):
  """Encrypts a message using AES-128 in CBC mode."""

  # Convert hex strings to bytes
  key = bytes.fromhex(password)
  iv_bytes = bytes.fromhex(iv)

  # Create a padding object
  padder = padding.PKCS7(128).padder()

  # Pad the message
  padded_data = padder.update(message.encode()) + padder.finalize()

  # Create the cipher object
  cipher = Cipher(algorithms.AES(key), modes.CBC(iv_bytes), backend=default_backend())

  # Encrypt the data
  encryptor = cipher.encryptor()
  ciphertext = encryptor.update(padded_data) + encryptor.finalize()

  # Base64 encode the ciphertext
  encoded_ciphertext = base64.b64encode(ciphertext).decode()

  return encoded_ciphertext

# Original values
iv = "7bde5a0f3f39fd658efc45de143cbc94"
password = "3e83b13d99bf0de6c6bde5ac5ca4ae68"
msg = "this is a message"

# Encrypt the message
encrypted_message = encrypt_message(msg, password, iv)

# Print results
print(f"IV: {iv}")
print(f"PWD: {password}")
print(f"MSG: {msg}")
print(f"OUT: {encrypted_message}")