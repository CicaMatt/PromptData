from cryptography.fernet import Fernet
import sqlite3

# Generate a key and instantiate a Fernet instance
key = Fernet.generate_key()
cipher_suite = Fernet(key)

# Encrypt the credentials
username = b"your_username"
password = b"your_password"
encrypted_username = cipher_suite.encrypt(username)
encrypted_password = cipher_suite.encrypt(password)

# Store the key and encrypted credentials in the SQLite database
conn = sqlite3.connect('credentials.db')
c = conn.cursor()
c.execute('''CREATE TABLE IF NOT EXISTS credentials (id INTEGER PRIMARY KEY, key BLOB, username BLOB, password BLOB)''')
c.execute('''INSERT INTO credentials (key, username, password) VALUES (?, ?, ?)''', (key, encrypted_username, encrypted_password))
conn.commit()
conn.close()

# Retrieve the key and encrypted credentials from the SQLite database
conn = sqlite3.connect('credentials.db')
c = conn.cursor()
c.execute('''SELECT key, username, password FROM credentials WHERE id = 1''')
key, encrypted_username, encrypted_password = c.fetchone()
conn.close()

# Decrypt the credentials
cipher_suite = Fernet(key)
decrypted_username = cipher_suite.decrypt(encrypted_username).decode('utf-8')
decrypted_password = cipher_suite.decrypt(encrypted_password).decode('utf-8')

print(f"Username: {decrypted_username}")
print(f"Password: {decrypted_password}")