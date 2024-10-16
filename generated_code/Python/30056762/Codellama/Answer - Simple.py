import Crypto
from Crypto.PublicKey import RSA
from Crypto import Random

random_generator = Random.new().read
key = RSA.generate(1024, random_generator) #generate public and private keys

publickey = key.publickey # pub key export for exchange

# Encrypt the message
encrypted = publickey.encrypt('encrypt this message', 32)
print('encrypted message:' + encrypted) #ciphertext

# Write the ciphertext to a file
f = open('encryption.txt', 'w')
f.write(str(encrypted))
f.close()

# Read the ciphertext from the file and decrypt it using the private key
with open('encryption.txt', 'r') as f:
    message = f.read()
    decrypted = key.decrypt(message)

print('decrypted message:', decrypted)