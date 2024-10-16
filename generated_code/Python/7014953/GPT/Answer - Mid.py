import sqlite3
from cryptography.fernet import Fernet
import keyring
import base64

# Constants
DATABASE = 'credentials.db'
SERVICE_NAME = 'MyApp'
USERNAME = 'your_user'  # Static username
CREDENTIAL_KEY = f"{SERVICE_NAME}_key"

# Initialize database and create table if it doesn't exist
def init_db():
    conn = sqlite3.connect(DATABASE)
    cursor = conn.cursor()
    cursor.execute('''
    CREATE TABLE IF NOT EXISTS credentials (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        username TEXT NOT NULL,
        encrypted_password BLOB NOT NULL
    )''')
    conn.commit()
    conn.close()

# Retrieve or generate encryption key
def get_encryption_key():
    key = keyring.get_password(SERVICE_NAME, CREDENTIAL_KEY)
    if not key:
        key = Fernet.generate_key()
        keyring.set_password(SERVICE_NAME, CREDENTIAL_KEY, key.decode())
    return key.encode()

# Encrypt password and store it in the database
def store_credentials(username, password):
    key = get_encryption_key()
    fernet = Fernet(key)
    encrypted_password = fernet.encrypt(password.encode())

    conn = sqlite3.connect(DATABASE)
    cursor = conn.cursor()
    cursor.execute("INSERT INTO credentials (username, encrypted_password) VALUES (?, ?)", (username, encrypted_password))
    conn.commit()
    conn.close()

# Retrieve and decrypt the password from the database
def retrieve_credentials():
    conn = sqlite3.connect(DATABASE)
    cursor = conn.cursor()
    cursor.execute("SELECT username, encrypted_password FROM credentials ORDER BY id DESC LIMIT 1")
    result = cursor.fetchone()
    conn.close()

    if result:
        username, encrypted_password = result
        key = get_encryption_key()
        fernet = Fernet(key)
        decrypted_password = fernet.decrypt(encrypted_password).decode()
        return username, decrypted_password
    else:
        return None, None

# Example usage
if __name__ == "__main__":
    init_db()

    # Uncomment to store new credentials (run this just once or when you update credentials)
    # store_credentials(USERNAME, "your_secure_password")

    # Retrieve and print the credentials
    user, password = retrieve_credentials()
    if user and password:
        print(f"Retrieved credentials: {user} / {password}")
    else:
        print("No credentials found")
