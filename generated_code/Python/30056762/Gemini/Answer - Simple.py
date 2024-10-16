from Crypto.PublicKey import RSA
from Crypto import Random
import ast  # For safely evaluating strings representing tuples

# Generate keys
random_generator = Random.new().read
key = RSA.generate(1024, random_generator) 
publickey = key.publickey 

# Encrypt
message = 'encrypt this message'
encrypted = publickey.encrypt(message.encode('utf-8'), 32)  # Encode message to bytes

# Write ciphertext to file (safely)
with open('encryption.txt', 'w') as f:
    f.write(str(encrypted)) 

# Decrypt 
with open('encryption.txt', 'r') as f:
    encrypted_from_file = ast.literal_eval(f.read())  # Safely evaluate tuple from file

decrypted = key.decrypt(encrypted_from_file)

# Print and write results
print('Decrypted:', decrypted.decode('utf-8'))  # Decode back to string

with open('encryption.txt', 'w') as f:
    f.write(f"Encrypted: {encrypted}\n")
    f.write(f"Decrypted: {decrypted.decode('utf-8')}\n")