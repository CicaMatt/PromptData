from Crypto.Cipher import AES
from Crypto.Util.Padding import pad
import base64

# Inputs
iv = bytes.fromhex("7bde5a0f3f39fd658efc45de143cbc94")
password = bytes.fromhex("3e83b13d99bf0de6c6bde5ac5ca4ae68")
msg = "this is a message"

# Create AES cipher object
cipher = AES.new(password, AES.MODE_CBC, iv)

# Encrypt the message with padding
encrypted = cipher.encrypt(pad(msg.encode(), AES.block_size))

# Encode in base64 to match OpenSSL output
encrypted_base64 = base64.b64encode(encrypted).decode()

# Print results
print(f"IV: {iv.hex()}")
print(f"PWD: {password.hex()}")
print(f"MSG: {msg}")
print(f"OUT: {encrypted_base64}")
