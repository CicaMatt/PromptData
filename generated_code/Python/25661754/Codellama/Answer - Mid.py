import pandas as pd
import pyodbc
from ftplib import FTP

# Connect to the FTP server
ftp = FTP('ftp.xyz.com', 'user', 'pass')

# Download the CSV file from the FTP server
ftp.retrbinary('filename.csv', open('/path/to/local/file.csv', 'wb').write)

# Read the CSV file into a Pandas DataFrame
df = pd.read_csv('/path/to/local/file.csv')

# Connect to the SQL Server
connStr = ('DRIVER={SQL Server Native Client 10.0};SERVER=localhost;DATABASE=TESTFEED;UID=sa;PWD=pass')
conn = pyodbc.connect(connStr)
cursor = conn.cursor()

# Insert the data into the SQL Server table
for row in df.itertuples():
    cursor.execute("INSERT INTO dbo.tblImport (Startdt, Enddt, x, y, z) VALUES (?, ?, ?, ?, ?)", tuple(row))

# Commit the changes and close the connection
conn.commit()
cursor.close()
conn.close()