from Crypto.PublicKey import RSA
from Crypto.Cipher import PKCS1_OAEP
from Crypto import Random

# Key Generation (Safely store the private key)
random_generator = Random.new().read
key = RSA.generate(2048, random_generator)  # Increased key size for security

public_key = key.publickey

# Encryption
message = b'encrypt this message'  # Ensure message is in bytes
cipher = PKCS1_OAEP.new(public_key)
ciphertext = cipher.encrypt(message)

# Write Ciphertext to File (Securely handle file permissions)
with open('encryption.txt', 'wb') as f:  # 'wb' for binary writing
    f.write(ciphertext)

# Decryption
with open('encryption.txt', 'rb') as f:  # 'rb' for binary reading
    ciphertext = f.read()

cipher = PKCS1_OAEP.new(key)
decrypted_message = cipher.decrypt(ciphertext)

print('Decrypted:', decrypted_message.decode('utf-8'))  # Decode back to string

# Append to File (Consider logging or separate files for clarity)
with open('encryption.txt', 'a') as f:
    f.write('\nDecrypted: ' + decrypted_message.decode('utf-8'))