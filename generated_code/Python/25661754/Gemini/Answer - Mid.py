import pyodbc
import pandas as pd
from ftplib import FTP
from io import BytesIO  # Use BytesIO for binary data

try:
    # FTP Connection
    ftp = FTP('ftp.xyz.com', 'user', 'pass')
    ftp.set_pasv(True)

    # Read CSV Directly from FTP
    with BytesIO() as r:
        ftp.retrbinary('RETR filename.csv', r.write)
        r.seek(0)  # Reset file pointer
        df = pd.read_csv(r)

    # SQL Server Connection
    conn_str = ('DRIVER={SQL Server Native Client 10.0};SERVER=localhost;DATABASE=TESTFEED;UID=sa;PWD=pass')
    conn = pyodbc.connect(conn_str)

    # Insert into SQL Server
    df.to_sql('tblImport', conn, if_exists='append', index=False)

    conn.close()
    print("Script has successfully run!")

except Exception as e:
    print(f"An error occurred: {e}")