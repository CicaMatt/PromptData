import os
import sqlite3
import base64
from cryptography.hazmat.primitives.kdf.pbkdf2 import PBKDF2HMAC
from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes
from cryptography.hazmat.primitives.kdf.scrypt import Scrypt
from cryptography.hazmat.primitives import padding
from cryptography.hazmat.backends import default_backend
from cryptography.exceptions import InvalidKey

# Generate or retrieve encryption key securely from environment variable
def get_encryption_key():
    # You can store this securely in an environment variable or other secure storage
    key = os.getenv('ENCRYPTION_KEY')
    if key is None:
        raise ValueError("Encryption key not found. Set ENCRYPTION_KEY in environment.")
    return base64.urlsafe_b64decode(key)

# Generate a strong encryption key and salt
def generate_key_salt():
    salt = os.urandom(16)
    kdf = Scrypt(salt=salt, length=32, n=2**14, r=8, p=1, backend=default_backend())
    key = kdf.derive(os.urandom(32))
    return key, salt

# Encrypt the password using AES-GCM
def encrypt_password(password: str, key: bytes) -> (bytes, bytes, bytes):
    iv = os.urandom(12)  # GCM needs a 12-byte IV
    cipher = Cipher(algorithms.AES(key), modes.GCM(iv), backend=default_backend())
    encryptor = cipher.encryptor()
    
    padder = padding.PKCS7(128).padder()
    padded_password = padder.update(password.encode()) + padder.finalize()
    
    ciphertext = encryptor.update(padded_password) + encryptor.finalize()
    return iv, ciphertext, encryptor.tag

# Decrypt the password
def decrypt_password(iv: bytes, ciphertext: bytes, tag: bytes, key: bytes) -> str:
    cipher = Cipher(algorithms.AES(key), modes.GCM(iv, tag), backend=default_backend())
    decryptor = cipher.decryptor()

    padded_password = decryptor.update(ciphertext) + decryptor.finalize()

    unpadder = padding.PKCS7(128).unpadder()
    password = unpadder.update(padded_password) + unpadder.finalize()
    return password.decode()

# Store encrypted credentials in the SQLite database
def store_credentials(username: str, password: str, db_path: str):
    key = get_encryption_key()
    iv, encrypted_password, tag = encrypt_password(password, key)
    
    conn = sqlite3.connect(db_path)
    c = conn.cursor()
    
    c.execute('''CREATE TABLE IF NOT EXISTS credentials 
                 (id INTEGER PRIMARY KEY, username TEXT, iv BLOB, password BLOB, tag BLOB)''')
    
    c.execute('DELETE FROM credentials')  # Clear any existing credentials
    c.execute('INSERT INTO credentials (username, iv, password, tag) VALUES (?, ?, ?, ?)',
              (username, iv, encrypted_password, tag))
    
    conn.commit()
    conn.close()

# Retrieve encrypted credentials from the SQLite database
def retrieve_credentials(db_path: str) -> (str, str):
    key = get_encryption_key()
    
    conn = sqlite3.connect(db_path)
    c = conn.cursor()
    
    c.execute('SELECT username, iv, password, tag FROM credentials')
    result = c.fetchone()
    
    if result:
        username, iv, encrypted_password, tag = result
        password = decrypt_password(iv, encrypted_password, tag, key)
        return username, password
    else:
        raise ValueError("No credentials found in the database")

# Example usage: Storing and retrieving credentials
if __name__ == "__main__":
    db_path = "credentials.db"
    
    # Example of setting an encryption key in an environment variable
    # For actual use, set this securely in your environment before running the script
    # os.environ['ENCRYPTION_KEY'] = base64.urlsafe_b64encode(os.urandom(32)).decode()

    action = input("Do you want to (s)tore or (r)etrieve credentials? ")
    
    if action.lower() == 's':
        username = input("Enter the username: ")
        password = input("Enter the password: ")
        store_credentials(username, password, db_path)
        print("Credentials stored securely.")
    
    elif action.lower() == 'r':
        try:
            username, password = retrieve_credentials(db_path)
            print(f"Retrieved credentials - Username: {username}, Password: {password}")
        except ValueError as e:
            print(e)
