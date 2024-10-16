from Crypto.PublicKey import RSA
from Crypto.Cipher import PKCS1_OAEP
from Crypto import Random

# Step 1: Generate private/public key pair
random_generator = Random.new().read
key = RSA.generate(2048, random_generator)  # Generate a public/private key pair
public_key = key.publickey()

# Step 2: Encrypt the message
message = "encrypt this message".encode()  # Convert string message to bytes
cipher_rsa = PKCS1_OAEP.new(public_key)  # Use OAEP padding for RSA
encrypted_message = cipher_rsa.encrypt(message)

# Write encrypted message to a file
with open('encryption.txt', 'wb') as f:
    f.write(encrypted_message)

print("Encrypted message written to file.")

# Step 3: Read ciphertext from the file and decrypt it
with open('encryption.txt', 'rb') as f:
    encrypted_message_from_file = f.read()

cipher_rsa = PKCS1_OAEP.new(key)  # Use private key for decryption
decrypted_message = cipher_rsa.decrypt(encrypted_message_from_file)

print("Decrypted message:", decrypted_message.decode())  # Decode bytes back to string
