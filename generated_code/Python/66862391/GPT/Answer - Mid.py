import base64
from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes
from cryptography.hazmat.backends import default_backend

def aes_128_cbc_encrypt(msg, key, iv):
    # Convert key and IV from hex to bytes
    key_bytes = bytes.fromhex(key)
    iv_bytes = bytes.fromhex(iv)
    
    # Pad the message according to PKCS7
    pad_len = 16 - (len(msg) % 16)
    padded_msg = msg + (chr(pad_len) * pad_len)
    
    # Create the cipher
    cipher = Cipher(algorithms.AES(key_bytes), modes.CBC(iv_bytes), backend=default_backend())
    encryptor = cipher.encryptor()

    # Encrypt the padded message
    encrypted = encryptor.update(padded_msg.encode()) + encryptor.finalize()

    # Encode the encrypted message in base64
    return base64.b64encode(encrypted).decode()

# Given values
iv = "7bde5a0f3f39fd658efc45de143cbc94"
password = "3e83b13d99bf0de6c6bde5ac5ca4ae68"
msg = "this is a message"

# Encrypt the message
out = aes_128_cbc_encrypt(msg, password, iv)

# Output the results
print(f"IV: {iv}")    
print(f"PWD: {password}")     
print(f"MSG: {msg}")   
print(f"OUT: {out}")
