import pandas as pd
from ftplib import FTP
from io import BytesIO
import pyodbc
import sqlalchemy

# 1. FTP Retrieval
ftp = FTP('ftp.xyz.com', 'user', 'pass')
ftp.set_pasv(True)

with BytesIO() as r:
    ftp.retrbinary('filename.csv', r.write)
    r.seek(0)  # Reset file pointer for reading
    df = pd.read_csv(r)  # Directly read into Pandas DataFrame

# 2. SQL Server Connection
engine = sqlalchemy.create_engine('mssql+pyodbc://sa:pass@localhost/TESTFEED?driver=SQL Server Native Client 10.0')

# 3. Data Insertion (Efficient Bulk Insert)
df.to_sql('tblImport', engine, if_exists='append', index=False)

print("Data successfully transferred to SQL Server!")