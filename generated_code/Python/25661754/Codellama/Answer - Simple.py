import pyodbc

# Establish connection with FTP server
ftp = FTP('ftp.xyz.com', 'user', 'pass')
ftp.set_pasv(True)

# Read data from file on FTP server
r = StringIO()
ftp.retrbinary('filename.csv', r.write)

# Create a pandas dataframe from the CSV data
df = pandas.read_table(r.getvalue(), delimiter=',')

# Insert data into SQL Server database using pyodbc
conn = pyodbc.connect('DRIVER={SQL Server Native Client 10.0};SERVER=localhost;DATABASE=TESTFEED;UID=sa;PWD=pass')
cursor = conn.cursor()

# Iterate over rows in pandas dataframe and insert into SQL Server database
for i, row in df.iterrows():
    cursor.execute("INSERT INTO dbo.tblImport (Startdt, Enddt, x,y,z) VALUES (?, ?, ?, ?, ?)",
                   row['Startdt'], row['Enddt'], row['x'], row['y'], row['z'])

# Commit changes and close connection
conn.commit()
cursor.close()
conn.close()