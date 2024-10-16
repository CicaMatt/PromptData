from Crypto.PublicKey import RSA
from Crypto.Cipher import PKCS1_OAEP
from Crypto import Random
import base64

# Generate RSA keys (private and public)
random_generator = Random.new().read
# 2048-bit RSA key for better security
key = RSA.generate(2048, random_generator)  

# Extract the public and private keys
private_key = key
public_key = key.publickey()

# Encrypt a message using the public key
def encrypt_message(message, public_key):
    # Convert the message to bytes
    message_bytes = message.encode('utf-8')

    # Create a cipher object using the public key and PKCS1_OAEP
    cipher = PKCS1_OAEP.new(public_key)

    # Encrypt the message
    encrypted_message = cipher.encrypt(message_bytes)

    # Return base64-encoded encrypted message (to handle binary data easily)
    return base64.b64encode(encrypted_message).decode('utf-8')

# Decrypt the ciphertext using the private key
def decrypt_message(ciphertext, private_key):
    # Decode the base64-encoded ciphertext
    encrypted_bytes = base64.b64decode(ciphertext)

    # Create a cipher object using the private key and PKCS1_OAEP
    cipher = PKCS1_OAEP.new(private_key)

    # Decrypt the message
    decrypted_message = cipher.decrypt(encrypted_bytes)

    # Return the decrypted message as a string
    return decrypted_message.decode('utf-8')

# Write encrypted message to a file
def write_to_file(filename, data):
    with open(filename, 'w') as file:
        file.write(data)

# Read encrypted message from a file
def read_from_file(filename):
    with open(filename, 'r') as file:
        return file.read()

# Example message
message = "Encrypt this message using RSA."

# Encrypt the message
encrypted_message = encrypt_message(message, public_key)
print("Encrypted message:", encrypted_message)

# Write the encrypted message to a file
write_to_file('encrypted_message.txt', encrypted_message)

# Read the encrypted message from the file
ciphertext_from_file = read_from_file('encrypted_message.txt')

# Decrypt the message
decrypted_message = decrypt_message(ciphertext_from_file, private_key)
print("Decrypted message:", decrypted_message)
