import pyodbc
import pandas as pd
from ftplib import FTP
import io

# Connect to FTP server and retrieve the file
ftp = FTP('ftp.xyz.com', 'user', 'pass')
ftp.set_pasv(True)
r = io.StringIO()
ftp.retrbinary('RETR filename.csv', r.write)
r.seek(0)

# Load data into pandas DataFrame
df = pd.read_csv(r, delimiter=',')

# Connect to SQL Server
connStr = 'DRIVER={SQL Server Native Client 10.0};SERVER=localhost;DATABASE=TESTFEED;UID=sa;PWD=pass'
conn = pyodbc.connect(connStr)

# Insert data into SQL Server
cursor = conn.cursor()
for index, row in df.iterrows():
    cursor.execute("INSERT INTO dbo.tblImport (Startdt, Enddt, x, y, z) VALUES (?, ?, ?, ?, ?)", 
                   row['Startdt'], row['Enddt'], row['x'], row['y'], row['z'])
cursor.close()
conn.commit()
conn.close()

print("Script has successfully run!")
