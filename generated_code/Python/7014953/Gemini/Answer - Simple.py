import sqlite3
from cryptography.fernet import Fernet
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.kdf.pbkdf2 import PBKDF2HMAC

def derive_key(master_password):
    salt = b'\x8a\x9c...\x1f'  # Replace with a randomly generated salt
    kdf = PBKDF2HMAC(
        algorithm=hashes.SHA256(),
        length=32,
        salt=salt,
        iterations=390000,  # Adjust for desired security/performance tradeoff
    )
    key = base64.urlsafe_b64encode(kdf.derive(master_password.encode()))
    return key

def encrypt_credentials(username, password, master_password):
    f = Fernet(derive_key(master_password))
    token = f.encrypt(f"{username}:{password}".encode())
    return token

def decrypt_credentials(token, master_password):
    f = Fernet(derive_key(master_password))
    decrypted = f.decrypt(token).decode()
    username, password = decrypted.split(":")
    return username, password

# ... (SQLite database setup)

def store_credentials(conn, username, password, master_password):
    encrypted_creds = encrypt_credentials(username, password, master_password)
    # ... (Insert encrypted_creds into your SQLite database)

def retrieve_credentials(conn, master_password):
    # ... (Retrieve encrypted_creds from your SQLite database)
    username, password = decrypt_credentials(encrypted_creds, master_password)
    return username, password

# ... (Rest of your script logic)