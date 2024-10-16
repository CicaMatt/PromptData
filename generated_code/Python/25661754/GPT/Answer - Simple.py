import pyodbc
import pandas as pd
from ftplib import FTP
import io

# Step 1: FTP to pandas
ftp = FTP('ftp.xyz.com', 'user', 'pass')
ftp.set_pasv(True)

# Use BytesIO to handle the binary content of the file
r = io.BytesIO()
ftp.retrbinary('RETR filename.csv', r.write)

# Reset the buffer position to the start
r.seek(0)

# Read the CSV data into a Pandas DataFrame
df = pd.read_csv(r)

# Close the FTP connection
ftp.quit()

# Step 2: Connect to SQL Server
connStr = ('DRIVER={SQL Server Native Client 10.0};SERVER=localhost;DATABASE=TESTFEED;UID=sa;PWD=pass')
conn = pyodbc.connect(connStr)

# Step 3: Insert data into SQL Server
# For inserting large DataFrame into SQL Server, using a loop or pandas-to_sql with SQLAlchemy is an option.
# Here, I'll show how to insert row by row using pyodbc.

cursor = conn.cursor()

# Loop through the DataFrame rows and insert data
for index, row in df.iterrows():
    cursor.execute("""
        INSERT INTO dbo.tblImport (Startdt, Enddt, x, y, z)
        VALUES (?, ?, ?, ?, ?)""",
        row['Startdt'], row['Enddt'], row['x'], row['y'], row['z']
    )

# Commit and close the connection
conn.commit()
cursor.close()
conn.close()

print("Script has successfully run!")
