import base64
from cryptography.hazmat.primitives import ciphers
from cryptography.hazmat.backends import default_backend

iv = "7bde5a0f3f39fd658efc45de143cbc94"
password = "3e83b13d99bf0de6c6bde5ac5ca4ae68"
msg = "this is a message"

# Encrypt the message using AES-128 in CBC mode with PKCS7 padding
cipher = ciphers.Cipher(algorithms.AES(password), modes.CBC(iv), backend=default_backend())
encryptor = cipher.encryptor()
padded_msg = encryptor.update(msg.encode('utf-8')) + encryptor.finalize()
print("Encrypted message:", base64.b64encode(padded_msg).decode('utf-8'))

# Decrypt the encrypted message using AES-128 in CBC mode with PKCS7 padding
cipher = ciphers.Cipher(algorithms.AES(password), modes.CBC(iv), backend=default_backend())
decryptor = cipher.decryptor()
padded_msg = decryptor.update(base64.b64decode(out)) + decryptor.finalize()
print("Decrypted message:", padded_msg.decode('utf-8'))