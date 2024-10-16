def decrypt_file(file_name, key):
    f = open(file_name, 'rb')  # Open in binary mode for reading bytes
    ciphertext = f.read()
    f.close()

    try:
        decrypted_data = decrypt(ciphertext, key)
    except ValueError as e:
        if "Input strings must be a multiple of 16 in length" in str(e):
            # Handle potential padding issues (if any)
            # You might need to adjust this based on your specific padding scheme
            decrypted_data = decrypt(ciphertext[:-1], key)  # Try removing one byte
        else:
            raise e  # Re-raise other ValueErrors

    f = open(file_name, 'w', encoding='utf-8')  # Open in text mode for writing
    f.write(decrypted_data.decode('utf-8')) 
    f.close()