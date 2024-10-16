from Crypto.PublicKey import RSA
from Crypto import Random
from Crypto.Cipher import PKCS1_OAEP

# Generate RSA key pair
random_generator = Random.new().read
key = RSA.generate(1024, random_generator)
public_key = key.publickey()

# Create a cipher object using the public key
cipher_rsa_encrypt = PKCS1_OAEP.new(public_key)

# Message to encrypt
message = b'encrypt this message'

# Encrypt the message
encrypted_message = cipher_rsa_encrypt.encrypt(message)
print('Encrypted message:', encrypted_message)

# Write the encrypted message to a file
with open('encryption.txt', 'wb') as f:
    f.write(encrypted_message)

# Read the encrypted message from the file
with open('encryption.txt', 'rb') as f:
    ciphertext = f.read()

# Create a cipher object using the private key for decryption
cipher_rsa_decrypt = PKCS1_OAEP.new(key)

# Decrypt the message
decrypted_message = cipher_rsa_decrypt.decrypt(ciphertext)
print('Decrypted message:', decrypted_message.decode())
