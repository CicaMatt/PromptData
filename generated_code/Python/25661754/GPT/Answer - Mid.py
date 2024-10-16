import pandas as pd
from ftplib import FTP
from io import BytesIO  # Correct for Python 3
import pyodbc
from sqlalchemy import create_engine

# FTP server details
ftp_server = 'ftp.xyz.com'
ftp_user = 'user'
ftp_password = 'pass'
ftp_file = 'filename.csv'

# Step 1: Connect to FTP and retrieve the file into a pandas DataFrame
ftp = FTP(ftp_server)
ftp.login(ftp_user, ftp_password)
ftp.set_pasv(True)

# Using BytesIO to read the binary data
r = BytesIO()
ftp.retrbinary(f"RETR {ftp_file}", r.write)
r.seek(0)  # Move to the start of the file after writing

# Load the CSV data into a pandas DataFrame
df = pd.read_csv(r)

# Close FTP connection
ftp.quit()

# Step 2: Connect to SQL Server and insert data into a table
try:
    # Create the connection to the SQL Server using pyodbc
    conn_str = (
        "DRIVER={ODBC Driver 17 for SQL Server};"
        "SERVER=localhost;"
        "DATABASE=TESTFEED;"
        "UID=sa;"
        "PWD=pass"
    )
    engine = create_engine(f"mssql+pyodbc:///?odbc_connect={conn_str}")
    
    # Insert data into SQL Server using pandas to_sql
    df.to_sql('tblImport', engine, if_exists='append', index=False)
    
    print("Data successfully inserted into SQL Server!")

except Exception as e:
    print(f"Error occurred: {e}")
finally:
    engine.dispose()  # Ensure the engine is closed after use
