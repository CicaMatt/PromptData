from Crypto.PublicKey import RSA
from Crypto.Cipher import PKCS1_OAEP
from Crypto import Random

# Step 1: Key generation (private and public)
random_generator = Random.new().read
key = RSA.generate(1024, random_generator)  # Generate public and private keys

publickey = key.publickey()  # Export public key for encryption

# Step 2: Encrypt the message using the public key
message = "encrypt this message".encode('utf-8')  # Convert message to bytes
cipher = PKCS1_OAEP.new(publickey)  # Use PKCS1_OAEP for encryption
encrypted_message = cipher.encrypt(message)

# Step 3: Write the encrypted message to a file (in binary mode)
with open('encryption.txt', 'wb') as f:
    f.write(encrypted_message)  # Write encrypted message as bytes

# Step 4: Read the encrypted message from the file
with open('encryption.txt', 'rb') as f:
    encrypted_message_from_file = f.read()  # Read bytes from the file

# Step 5: Decrypt the message using the private key
decrypt_cipher = PKCS1_OAEP.new(key)
decrypted_message = decrypt_cipher.decrypt(encrypted_message_from_file)

# Convert decrypted bytes back to string
decrypted_message = decrypted_message.decode('utf-8')

# Output the results
print('Original message:', message.decode('utf-8'))
print('Encrypted message (in bytes):', encrypted_message)
print('Decrypted message:', decrypted_message)

# Optional: Write decrypted message to file
with open('decryption_output.txt', 'w') as f:
    f.write(decrypted_message)
