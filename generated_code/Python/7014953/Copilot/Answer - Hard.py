import sqlite3
from cryptography.fernet import Fernet
import os

# Generate a key for encryption and decryption
# You must store this key securely. If you lose it, you won't be able to decrypt your data.
key = Fernet.generate_key()
cipher_suite = Fernet(key)

# Store the key in a secure location
with open('secret.key', 'wb') as key_file:
    key_file.write(key)

# Function to load the key
def load_key():
    return open('secret.key', 'rb').read()

# Function to encrypt data
def encrypt_data(data):
    return cipher_suite.encrypt(data.encode())

# Function to decrypt data
def decrypt_data(encrypted_data):
    return cipher_suite.decrypt(encrypted_data).decode()

# Connect to SQLite database (or create it if it doesn't exist)
conn = sqlite3.connect('credentials.db')
c = conn.cursor()

# Create table to store credentials
c.execute('''CREATE TABLE IF NOT EXISTS credentials
             (id INTEGER PRIMARY KEY, username TEXT, password TEXT)''')

# Function to store credentials
def store_credentials(username, password):
    encrypted_username = encrypt_data(username)
    encrypted_password = encrypt_data(password)
    c.execute("INSERT INTO credentials (username, password) VALUES (?, ?)", (encrypted_username, encrypted_password))
    conn.commit()

# Function to retrieve credentials
def retrieve_credentials():
    c.execute("SELECT username, password FROM credentials WHERE id=1")
    encrypted_username, encrypted_password = c.fetchone()
    username = decrypt_data(encrypted_username)
    password = decrypt_data(encrypted_password)
    return username, password

# Example usage
store_credentials('your_username', 'your_password')
username, password = retrieve_credentials()
print(f"Username: {username}, Password: {password}")

# Close the database connection
conn.close()
