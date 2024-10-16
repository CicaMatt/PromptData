from cryptography.hazmat.primitives import padding
from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes
from cryptography.hazmat.backends import default_backend
import base64

def encrypt_message(message, password, iv):
    """Encrypts a message using AES-128 in CBC mode with PKCS7 padding.

    Args:
        message: The message to be encrypted (bytes).
        password: The encryption password (bytes).
        iv: The initialization vector (bytes).

    Returns:
        The base64-encoded ciphertext (str).
    """
    
    # Ensure password and IV are of correct length
    if len(password) != 16 or len(iv) != 16:
        raise ValueError("Password and IV must be 16 bytes long")

    # Create padding object and pad the message
    padder = padding.PKCS7(128).padder()
    padded_data = padder.update(message) + padder.finalize()

    # Create cipher object and encryptor
    backend = default_backend()
    cipher = Cipher(algorithms.AES(password), modes.CBC(iv), backend=backend)
    encryptor = cipher.encryptor()

    # Encrypt the padded data
    ciphertext = encryptor.update(padded_data) + encryptor.finalize()

    # Base64 encode the ciphertext
    return base64.b64encode(ciphertext).decode('utf-8')

# Example usage
iv = b'\x7b\xde\x5a\x0f\x3f\x39\xfd\x65\x8e\xfc\x45\xde\x14\x3c\xbc\x94'
password = b'\x3e\x83\xb1\x3d\x99\xbf\x0d\xe6\xc6\xbd\xe5\xac\x5c\xa4\xae\x68'
msg = b"this is a message"

encrypted_message = encrypt_message(msg, password, iv)

print(f"IV: {iv.hex()}")
print(f"PWD: {password.hex()}")
print(f"MSG: {msg.decode('utf-8')}")
print(f"OUT: {encrypted_message}")