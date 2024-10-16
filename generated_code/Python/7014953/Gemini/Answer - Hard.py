import sqlite3
import base64
from cryptography.fernet import Fernet
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.kdf.pbkdf2 import PBKDF2HMAC

def derive_key(master_password):
    """Derives a secure key from the master password."""
    # Replace with a randomly generated salt
    salt = b'\x8a\x95g\xc4\x16\x81\x1d\xb8\x81\x10\xbf\xf4\x91\xa7\xb1\x14o'  
    kdf = PBKDF2HMAC(
        algorithm=hashes.SHA256(),
        length=32,
        salt=salt,
        iterations=390000,
    )
    key = base64.urlsafe_b64encode(kdf.derive(master_password.encode()))
    return key

def encrypt_credentials(master_password, username, password):
    """Encrypts the credentials and stores them in the database."""
    key = derive_key(master_password)
    f = Fernet(key)
    token_username = f.encrypt(username.encode())
    token_password = f.encrypt(password.encode())

    conn = sqlite3.connect('credentials.db')
    cursor = conn.cursor()
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS credentials (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            username BLOB,
            password BLOB
        )
    ''')
    cursor.execute('INSERT INTO credentials (username, password) VALUES (?, ?)', (token_username, token_password))
    conn.commit()
    conn.close()

def decrypt_credentials(master_password):
    """Decrypts the credentials from the database."""
    key = derive_key(master_password)
    f = Fernet(key)

    conn = sqlite3.connect('credentials.db')
    cursor = conn.cursor()
    # Assuming only one set of credentials
    cursor.execute('SELECT username, password FROM credentials WHERE id = 1')  
    row = cursor.fetchone()
    conn.close()

    if row:
        token_username, token_password = row
        username = f.decrypt(token_username).decode()
        password = f.decrypt(token_password).decode()
        return username, password
    else:
        return None, None

# Example usage
master_password = input("Enter master password: ")
encrypt_credentials(master_password, "your_username", "your_password")

# Later in your script, when you need the credentials
username, password = decrypt_credentials(master_password)
if username and password:
    print("")
else:
    print("Invalid master password or credentials not found.")