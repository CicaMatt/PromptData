import cryptography
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.backends import default_backend

# Define the encryption parameters
iv = b"7bde5a0f3f39fd658efc45de143cbc94"
password = b"3e83b13d99bf0de6c6bde5ac5ca4ae68"
msg = b"this is a message"

# Create the cipher object and encrypt the message
cipher = cryptography.hazmat.primitives.ciphers.Cipher(
    algorithm=hashes.HashAlgorithm('aes_128'),
    mode=cryptography.hazmat.primitives.ciphers.CBC,
    backend=default_backend()
)
encrypted = cipher.encrypt(msg, iv, password)

# Print the encrypted message
print("IV:", iv.hex())
print("PWD:", password.hex())
print("MSG:", msg.decode('utf-8'))
print("OUT:", encrypted.hex())