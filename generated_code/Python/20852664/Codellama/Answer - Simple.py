def decrypt_file(file_name, key):
    def pad(s):
        x = AES.block_size - len(s) % AES.block_size
        return s + ((bytes([x]))) * x

    with open(file_name, 'rb') as f:
        ciphertext = f.read()

    plaintext = pad(ciphertext).decode('utf-8')
    dec = decrypt(plaintext, key)

    with open(file_name, 'wb') as f:
        f.write(dec.encode())