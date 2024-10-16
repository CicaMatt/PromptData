from Crypto.Cipher import AES
import base64

# Load the encrypted data into a string variable
ciphertext = "crypt.txt"
with open(ciphertext, "r") as f:
    ciphertext_str = f.read()

# Decode the ciphertext from Base64 to get the raw bytes
ciphertext_bytes = base64.b64decode(ciphertext_str)

# Create a new AES Cipher object in CBC mode with PBKDF2
obj2 = AES.new("mypassword", AES.MODE_CBC, "This is an IV456")

# Decrypt the ciphertext using the password and IV
output = obj2.decrypt(ciphertext_bytes)

# Print the decrypted data
print(output)