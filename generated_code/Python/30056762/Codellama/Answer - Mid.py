import Crypto
from Crypto.PublicKey import RSA
from Crypto import Random

# Generate a random number generator
random_generator = Random.new().read

# Generate a 1024-bit RSA key pair
key = RSA.generate(1024, random_generator)

# Export the public key for exchange
publickey = key.publickey

# Encrypt a message with the public key and write it to a file
message = 'encrypt this message'
ciphertext = publickey.encrypt(message, 32)
f = open('encryption.txt', 'w')
f.write(str(ciphertext))
f.close()

# Read the ciphertext from the file and decrypt it with the private key
with open('encryption.txt', 'r') as f:
    message = f.read()
decrypted = key.decrypt(message)
print('Decrypted message:', decrypted)