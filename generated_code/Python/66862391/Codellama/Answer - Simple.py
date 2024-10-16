from cryptography.hazmat.primitives import padding
from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes
import binascii

# Define the encryption parameters
iv = b"7bde5a0f3f39fd658efc45de143cbc94"
password = b"3e83b13d99bf0de6c6bde5ac5ca4ae68"
mode = modes.CBC(iv)
backend = default_backend()

# Encrypt the message using AES-128 in CBC mode with a 128-bit key and an IV
cipher = Cipher(algorithms.AES(password), mode, backend=backend)
encryptor = cipher.encryptor()
padder = padding.PKCS7(128).padder()
ct = encryptor.update(padder.update(msg)) + padder.finalize()
print("IV:", iv.hex())
print("PWD:", password.hex())
print("MSG:", msg)
print("OUT:", ct.hex())