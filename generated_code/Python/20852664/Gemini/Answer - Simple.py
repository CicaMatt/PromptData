def decrypt_file(file_name, key):
    def unpad(s):
        return s[:-ord(s[len(s)-1:])]  # Use ord() to get the integer value of the padding byte

    f = open(file_name, 'rb')  # Open in binary mode to read bytes directly
    ciphertext = f.read() 
    f.close()

    plaintext = decrypt(ciphertext, key)  # Decrypt the ciphertext (bytes)

    f = open(file_name, 'w', encoding='utf-8')  # Open in write mode with UTF-8 encoding
    f.write(plaintext.decode('utf-8'))  # Decode bytes to string before writing
    f.close()