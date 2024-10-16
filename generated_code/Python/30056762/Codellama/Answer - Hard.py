import Crypto
from Crypto.PublicKey import RSA
from Crypto import Random

# Generate a 2048-bit random number for the private key
random_generator = Random.new().read(2048)
key = RSA.generate(2048, random_generator) #generate public and private keys

publickey = key.publickey # pub key export for exchange

# Encrypt a message with the public key
encrypted = publickey.encrypt('This is a top-secret message', 32)

# Write the encrypted message to a file
with open ('encryption.txt', 'w') as f:
    f.write(str(encrypted)) #write ciphertext to file

# Read the encrypted message from the file
with open ('encryption.txt', 'r') as f:
    message = f.read()

# Decrypt the message with the private key
decrypted = key.decrypt(message)

print('Decrypted message:', decrypted)