import hashlib
import sqlite3
import os

# Set the username and password here
username = "my_username"
password = "my_password"

# Create a salt for the password using the hashlib module
salt = hashlib.sha256(os.urandom(10)).hexdigest()

# Encrypt the password using the AES-256 algorithm and the salt
encrypted_password = hashlib.sha256((salt + password).encode()).hexdigest()

# Connect to the SQLite database
conn = sqlite3.connect("mydatabase.db")

# Create a table for storing the username and encrypted password
c = conn.cursor()
c.execute("CREATE TABLE IF NOT EXISTS users (username TEXT, password TEXT)")

# Insert the username and encrypted password into the database
c.execute("INSERT INTO users VALUES (?, ?)", (username, encrypted_password))

# Commit the changes to the database
conn.commit()

# Close the connection to the database
conn.close()