import pyodbc
import pandas as pd
from ftplib import FTP
from io import StringIO

# FTP server details
ftp_server = 'ftp.xyz.com'
ftp_user = 'user'
ftp_pass = 'pass'
ftp_file = 'filename.csv'

# Connect to FTP server and retrieve the file
ftp = FTP(ftp_server)
ftp.login(user=ftp_user, passwd=ftp_pass)
ftp.set_pasv(True)
r = StringIO()
ftp.retrbinary(f'RETR {ftp_file}', r.write)
ftp.quit()

# Load the data into a pandas DataFrame
r.seek(0)
df = pd.read_csv(r, delimiter=',')

# SQL Server connection details
conn_str = 'DRIVER={SQL Server Native Client 10.0};SERVER=localhost;DATABASE=TESTFEED;UID=sa;PWD=pass'
conn = pyodbc.connect(conn_str)

# Insert data into SQL Server
cursor = conn.cursor()
for index, row in df.iterrows():
    cursor.execute("""
        INSERT INTO dbo.tblImport (Startdt, Enddt, x, y, z)
        VALUES (?, ?, ?, ?, ?)
    """, row['Startdt'], row['Enddt'], row['x'], row['y'], row['z'])

cursor.close()
conn.commit()
conn.close()

print("Script has successfully run!")
