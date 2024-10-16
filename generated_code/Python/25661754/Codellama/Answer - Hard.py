import pyodbc

# Set up the connection string
conn_str = 'DRIVER={SQL Server Native Client 10.0};SERVER=localhost;DATABASE=TESTFEED;UID=sa;PWD=pass'

# Connect to the SQL Server database
conn = pyodbc.connect(conn_str)

# Set up the FTP connection
ftp = FTP('ftp.xyz.com', 'user', 'pass')

# Download the data from the FTP server
r = StringIO()
ftp.retrbinary('filname.csv', r.write)

# Read the data into a Pandas dataframe
df = pandas.read_table(r.getvalue(), delimiter=',')

# Insert the data into the SQL Server database
cursor = conn.cursor()
for i, row in df.iterrows():
    cursor.execute("INSERT INTO dbo.tblImport (Startdt, Enddt, x,y,z) VALUES ({}, {}, {}, {}, {})".format(row['Startdt'], row['Enddt'], row['x'], row['y'], row['z']))
conn.commit()
conn.close()