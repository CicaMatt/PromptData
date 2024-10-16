from Crypto import Random
from Crypto.Cipher import AES

# Encrypt function
def encrypt(message, key=None, key_size=256):
    def pad(s):
        x = AES.block_size - len(s) % AES.block_size
        return s + (bytes([x]) * x)

    padded_message = pad(message)

    if key is None:
        key = Random.new().read(key_size // 8)

    iv = Random.new().read(AES.block_size)
    cipher = AES.new(key, AES.MODE_CBC, iv)

    return iv + cipher.encrypt(padded_message)

# Decrypt function
def decrypt(ciphertext, key):
    unpad = lambda s: s[:-s[-1]]
    iv = ciphertext[:AES.block_size]
    cipher = AES.new(key, AES.MODE_CBC, iv)
    plaintext = cipher.decrypt(ciphertext[AES.block_size:])
    return unpad(plaintext)

# Encrypt file function
def encrypt_file(file_name, key):
    with open(file_name, 'rb') as f:  # Open in binary mode
        plaintext = f.read()

    enc = encrypt(plaintext, key)

    with open(file_name, 'wb') as f:  # Write encrypted content in binary mode
        f.write(enc)

# Decrypt file function
def decrypt_file(file_name, key):
    with open(file_name, 'rb') as f:  # Open in binary mode
        ciphertext = f.read()

    dec = decrypt(ciphertext, key)

    with open(file_name, 'wb') as f:  # Write decrypted content in binary mode
        f.write(dec)

# Your key (example key)
key = b'\xbf\xc0\x85)\x10nc\x94\x02)j\xdf\xcb\xc4\x94\x9d(\x9e[EX\xc8\xd5\xbfI{\xa2$\x05(\xd5\x18'

# Encrypt the file
encrypt_file('to_enc.txt', key)

# Decrypt the file
decrypt_file('to_enc.txt', key)
