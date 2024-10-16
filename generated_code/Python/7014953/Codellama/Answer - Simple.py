import hashlib
import base64
import sqlite3

# Define the database file path
db_path = "mydatabase.db"

# Define the table name
table_name = "credentials"

# Define the column names for your username and password
username_column = "username"
password_column = "password"

# Define the key derivation function (KDF) to use for encrypting the password
kdf = hashlib.pbkdf2_hmac("sha256", "salt".encode(), "password".encode())

# Encrypt the username and password using AES-GCM
username = base64.b64encode(kdf.derive(username))
password = base64.b64encode(kdf.derive(password))

# Create a connection to the database
conn = sqlite3.connect(db_path)

# Create a cursor object
cursor = conn.cursor()

# Insert the encrypted username and password into the database
cursor.execute("INSERT INTO {} ({}, {}) VALUES (?, ?)".format(table_name, username_column, password_column), (username, password))

# Commit the changes to the database
conn.commit()

# Close the cursor and connection
cursor.close()
conn.close()

# Define the database file path
db_path = "mydatabase.db"

# Define the table name
table_name = "credentials"

# Define the column names for your username and password
username_column = "username"
password_column = "password"

# Create a connection to the database
conn = sqlite3.connect(db_path)

# Create a cursor object
cursor = conn.cursor()

# Retrieve the encrypted username and password from the database
cursor.execute("SELECT {} FROM {} WHERE id = ?".format(username_column, table_name), (1,))
encrypted_username = cursor.fetchone()[0]
encrypted_password = cursor.fetchone()[1]

# Close the cursor and connection
cursor.close()
conn.close()