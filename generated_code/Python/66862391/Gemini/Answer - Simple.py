from Crypto.Cipher import AES
from Crypto.Util.Padding import pad
import base64

def encrypt_message(msg, password, iv):
    """Encrypts a message using AES-128 in CBC mode.

    Args:
        msg: The message to be encrypted.
        password: The encryption password (key).
        iv: The initialization vector.

    Returns:
        The base64 encoded ciphertext.
    """

    key = bytes.fromhex(password)  # Convert password to bytes
    iv = bytes.fromhex(iv)  # Convert IV to bytes
    cipher = AES.new(key, AES.MODE_CBC, iv)
    padded_msg = pad(msg.encode(), AES.block_size)  # Pad the message
    ciphertext = cipher.encrypt(padded_msg)
    return base64.b64encode(ciphertext).decode()

# Your existing variables
iv = "7bde5a0f3f39fd658efc45de143cbc94"
password = "3e83b13d99bf0de6c6bde5ac5ca4ae68"
msg = "this is a message"

out = encrypt_message(msg, password, iv)

print(f"IV: {iv}")
print(f"PWD: {password}")
print(f"MSG: {msg}")
print(f"OUT: {out}")