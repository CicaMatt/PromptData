from Crypto.Cipher import AES
from Crypto.Protocol.KDF import PBKDF2
import base64

def decrypt_file(encrypted_file_path, password):
    with open(encrypted_file_path, 'rb') as f:
        # Read the encrypted data
        encrypted_data = f.read()

        # Extract salt and IV (first 16 bytes each)
        salt = encrypted_data[:16]
        iv = encrypted_data[16:32]

        # Derive the key using PBKDF2
        key = PBKDF2(password, salt, dkLen=32, count=100000)  # 32 bytes = 256 bits

        # Decode the base64 encoded ciphertext
        ciphertext = base64.b64decode(encrypted_data[32:])

        # Create the AES cipher and decrypt
        cipher = AES.new(key, AES.MODE_CBC, iv)
        plaintext = cipher.decrypt(ciphertext)

        # Remove padding (if any)
        plaintext = plaintext.rstrip(b"\0") 

        return plaintext

# Example usage
decrypted_data = decrypt_file('crypt.txt', 'mypassword')
with open('out.txt', 'wb') as f:
    f.write(decrypted_data)

print("File decrypted successfully!")