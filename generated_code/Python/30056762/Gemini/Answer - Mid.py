from Crypto.PublicKey import RSA
from Crypto import Random
from Crypto.Cipher import PKCS1_OAEP

# Generate keys
random_generator = Random.new().read
key = RSA.generate(1024, random_generator) 
publickey = key.publickey 

# Encrypt message
message = b'encrypt this message'  # Ensure the message is bytes
cipher = PKCS1_OAEP.new(publickey)
encrypted = cipher.encrypt(message)

# Write ciphertext to file
with open('encryption.txt', 'wb') as f:  # Use 'wb' for binary writing
    f.write(encrypted) 

# Decrypt message
with open('encryption.txt', 'rb') as f:  # Use 'rb' for binary reading
    encrypted_data = f.read()

cipher = PKCS1_OAEP.new(key)
decrypted = cipher.decrypt(encrypted_data)

print('Decrypted:', decrypted.decode('utf-8'))  # Decode back to string

# Optionally, write back to the file
with open('encryption.txt', 'w') as f: 
    f.write(decrypted.decode('utf-8'))