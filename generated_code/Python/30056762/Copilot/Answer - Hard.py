from Crypto.PublicKey import RSA
from Crypto.Cipher import PKCS1_OAEP
from Crypto import Random
import binascii

# Step 1: Generate RSA private/public key pair
random_generator = Random.new().read
key = RSA.generate(2048, random_generator)  # Use a key size of 2048 bits for better security

# Export public and private keys
private_key = key.export_key()
public_key = key.publickey().export_key()

# Save private and public keys to files (optional but good practice for long-term use)
with open('private_key.pem', 'wb') as f:
    f.write(private_key)

with open('public_key.pem', 'wb') as f:
    f.write(public_key)

# Step 2: Encrypt the message using the public key
message = b'encrypt this message'  # Message should be in bytes
public_key = RSA.import_key(open('public_key.pem').read())  # Load public key from file
cipher = PKCS1_OAEP.new(public_key)
encrypted_message = cipher.encrypt(message)

# Save the encrypted message to a file (in binary format)
with open('encrypted_message.bin', 'wb') as f:
    f.write(encrypted_message)

print(f'Encrypted message (in hex): {binascii.hexlify(encrypted_message).decode()}')

# Step 3: Decrypt the message using the private key
private_key = RSA.import_key(open('private_key.pem').read())  # Load private key from file
cipher = PKCS1_OAEP.new(private_key)

# Read the encrypted message from the file
with open('encrypted_message.bin', 'rb') as f:
    encrypted_message_from_file = f.read()

# Decrypt the message
decrypted_message = cipher.decrypt(encrypted_message_from_file)

# Print decrypted message
print(f'Decrypted message: {decrypted_message.decode()}')

# Optionally, write decrypted message to a file
with open('decrypted_message.txt', 'w') as f:
    f.write(decrypted_message.decode())
