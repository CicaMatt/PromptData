import sqlite3
from cryptography.fernet import Fernet

# Generate a key (do this once and store it securely)
key = Fernet.generate_key()

# Store this key securely, e.g., in an environment variable or file with restricted access
print(key.decode())

# Load the key (from environment variable or file)
key = b'your-generated-key'
cipher_suite = Fernet(key)

# Example username and password
username = "my_username"
password = "my_secure_password"

# Encrypt the password
encrypted_password = cipher_suite.encrypt(password.encode())

# Store username and encrypted password in SQLite
conn = sqlite3.connect('credentials.db')
c = conn.cursor()

# Create table if it doesn't exist
c.execute('''
    CREATE TABLE IF NOT EXISTS credentials (
        id INTEGER PRIMARY KEY,
        username TEXT NOT NULL,
        encrypted_password BLOB NOT NULL
    )
''')

# Insert the encrypted credentials
c.execute('INSERT INTO credentials (username, encrypted_password) VALUES (?, ?)', 
          (username, encrypted_password))
conn.commit()
conn.close()

key = b'your-generated-key'
cipher_suite = Fernet(key)

# Connect to SQLite and retrieve the encrypted password
conn = sqlite3.connect('credentials.db')
c = conn.cursor()

# Fetch the encrypted credentials
c.execute('SELECT username, encrypted_password FROM credentials WHERE id = 1')
result = c.fetchone()
conn.close()

username = result[0]
encrypted_password = result[1]

# Decrypt the password
decrypted_password = cipher_suite.decrypt(encrypted_password).decode()

print(f"Username: {username}")
print(f"Password: {decrypted_password}")