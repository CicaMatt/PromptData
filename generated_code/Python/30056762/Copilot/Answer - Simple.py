import Crypto
from Crypto.PublicKey import RSA
from Crypto import Random
import ast

# Step 1: Generate RSA key pair
random_generator = Random.new().read
key = RSA.generate(1024, random_generator)  # Generate private/public keys
publickey = key.publickey()  # Get the public key

# Step 2: Encrypt the message
message = "encrypt this message"
encrypted = publickey.encrypt(message.encode('utf-8'), 32)  # Encrypt the message (needs to be bytes)

# Display the encrypted message
print('Encrypted message:', encrypted)

# Step 3: Write the encrypted message to a file
with open('encryption.txt', 'w') as f:
    f.write(str(encrypted))  # Write encrypted message to the file

# Step 4: Read the encrypted message from the file
with open('encryption.txt', 'r') as f:
    encrypted_message = f.read()

# Step 5: Convert the encrypted message back to its original form (tuple)
encrypted_message = ast.literal_eval(encrypted_message)  # Convert string back to tuple

# Step 6: Decrypt the message
decrypted_message = key.decrypt(encrypted_message)  # Decrypt using the private key

# Display the decrypted message
print('Decrypted message:', decrypted_message.decode('utf-8'))  # Convert bytes back to string
