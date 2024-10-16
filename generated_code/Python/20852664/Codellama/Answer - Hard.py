from Crypto.Cipher import AES
from Crypto import Random

def decrypt_file(file_name, key):
    # Open the file in binary mode to read encrypted data
    with open(file_name, 'rb') as f:
        encrypted_data = f.read()
    
    # Extract the Initialization Vector (IV) from the encrypted data
    iv = encrypted_data[:AES.block_size]
    
    # Create AES cipher in CBC mode using the key and IV
    cipher = AES.new(key, AES.PKCS7 , iv)
    
    # Decrypt the remaining data after the IV
    decrypted_padded_data = cipher.decrypt(encrypted_data[AES.block_size:])
    
    # Unpad the decrypted data using PKCS7 unpadding
    decrypted_data = unpad(decrypted_padded_data)
    
    # Write the decrypted data back to the file in binary mode
    with open(file_name, 'wb') as f:
        f.write(decrypted_data)