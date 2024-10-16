import os
from cryptodome.cipher import AES

# Define the encryption key and IV used to encrypt the credentials
key = b'YOUR_ENCRYPTION_KEY_HERE'
iv = b'YOUR_INITIALIZATION_VECTOR_HERE'

# Define the username and password to be encrypted
username = 'YOUR_USERNAME_HERE'
password = 'YOUR_PASSWORD_HERE'

# Create a secure encryption algorithm using AES-256
algorithm = AES(key, iv)

# Use a key management system (KMS) or a hardware security module (HSM) to manage and store the encryption keys
kms = KMSClient()

# Encrypt the username and password using the secure algorithm
encrypted_username = kms.encrypt(algorithm, username)
encrypted_password = kms.encrypt(algorithm, password)

# Store the encrypted credentials in a file or database using a secure storage mechanism
with open('credentials.txt', 'wb') as f:
    f.write(encrypted_username + b'\n' + encrypted_password)

# Use a secure method of decrypting the credentials when the script runs periodically via cron
def decrypt_credentials():
    with open('credentials.txt', 'rb') as f:
        encrypted_username, encrypted_password = f.read().split(b'\n')
    
    # Use a secure protocol to retrieve the encryption key from the KMS
    kms = KMSClient()
    encryption_key = kms.get_encryption_key('YOUR_KEY_ID_HERE')
    
    # Decrypt the credentials using the retrieved encryption key
    algorithm = AES(encryption_key, iv)
    decrypted_username = algorithm.decrypt(encrypted_username)
    decrypted_password = algorithm.decrypt(encrypted_password)
    
    return (decrypted_username, decrypted_password)