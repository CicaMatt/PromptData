import sqlite3
import hashlib
import base64
from cryptography.fernet import Fernet
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.kdf.pbkdf2 import PBKDF2HMAC

def generate_key(master_password):
    """Derives a secure encryption key from the master password."""
    salt = b'\x8a\x90\x03o\x96\xc1\x0e\xd7\xd4\x90\x11\x99\xe3'  # Use a fixed salt
    kdf = PBKDF2HMAC(
        algorithm=hashes.SHA256(),
        length=32,
        salt=salt,
        iterations=390000,
    )
    key = base64.urlsafe_b64encode(kdf.derive(master_password.encode()))
    return key

def encrypt_credentials(master_password, username, password):
    """Encrypts and stores credentials in the SQLite database."""
    key = generate_key(master_password)
    f = Fernet(key)

    conn = sqlite3.connect('credentials.db')
    cursor = conn.cursor()
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS credentials (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            username TEXT,
            password TEXT
        )
    ''')

    encrypted_username = f.encrypt(username.encode())
    encrypted_password = f.encrypt(password.encode())

    cursor.execute('''
        INSERT INTO credentials (username, password)
        VALUES (?, ?)
    ''', (encrypted_username, encrypted_password))

    conn.commit()
    conn.close()

def decrypt_credentials(master_password):
    """Decrypts and retrieves credentials from the database."""
    key = generate_key(master_password)
    f = Fernet(key)

    conn = sqlite3.connect('credentials.db')
    cursor = conn.cursor()
    cursor.execute('SELECT username, password FROM credentials')
    row = cursor.fetchone()
    conn.close()

    if row:
        username = f.decrypt(row[0]).decode()
        password = f.decrypt(row[1]).decode()
        return username, password
    else:
        return None, None

# Example usage (replace with your actual credentials and master password)
if __name__ == "__main__":
    master_password = "your_strong_master_password"
    username = "your_3rd_party_username"
    password = "your_3rd_party_password"

    encrypt_credentials(master_password, username, password)

    # Later in your script, when you need to use the credentials:
    decrypted_username, decrypted_password = decrypt_credentials(master_password)

    if decrypted_username and decrypted_password:
        # Use the decrypted credentials to access the 3rd party service
        print("Decrypted credentials:", decrypted_username, decrypted_password)
    else:
        print("Incorrect master password or no credentials found.")