import base64  # For secure encoding/decoding

def decrypt_file(file_name, key):
    with open(file_name, 'r') as f:
        ciphertext_str = f.read()  # Read the ciphertext as a string

    # Decode the base64 string back to bytes
    ciphertext = base64.b64decode(ciphertext_str) 

    # No need for manual padding here, decryption handles it
    decrypted_data = decrypt(ciphertext, key)

    # Convert decrypted bytes back to a string (assuming UTF-8 encoding)
    plaintext = decrypted_data.decode('utf-8') 

    # Write the decrypted plaintext back to the file (overwriting)
    with open(file_name, 'w') as f:
        f.write(plaintext)