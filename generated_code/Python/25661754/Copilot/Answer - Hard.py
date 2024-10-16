import pyodbc
import pandas as pd
from ftplib import FTP
from io import StringIO

# FTP server details
ftp_server = 'ftp.xyz.com'
ftp_user = 'user'
ftp_pass = 'pass'
ftp_file = 'filename.csv'

# SQL Server connection details
conn_str = 'DRIVER={SQL Server Native Client 10.0};SERVER=localhost;DATABASE=TESTFEED;UID=sa;PWD=pass'

def fetch_data_from_ftp():
    try:
        ftp = FTP(ftp_server)
        ftp.login(user=ftp_user, passwd=ftp_pass)
        ftp.set_pasv(True)
        
        r = StringIO()
        ftp.retrbinary(f'RETR {ftp_file}', r.write)
        r.seek(0)
        
        df = pd.read_csv(r, delimiter=',')
        ftp.quit()
        return df
    except Exception as e:
        print(f"Error fetching data from FTP: {e}")
        return None

def insert_data_to_sql(df):
    try:
        conn = pyodbc.connect(conn_str)
        cursor = conn.cursor()
        
        for index, row in df.iterrows():
            cursor.execute("""
                INSERT INTO dbo.tblImport (Startdt, Enddt, x, y, z)
                VALUES (?, ?, ?, ?, ?)
            """, row['Startdt'], row['Enddt'], row['x'], row['y'], row['z'])
        
        conn.commit()
        cursor.close()
        conn.close()
        print("Data successfully inserted into SQL Server")
    except Exception as e:
        print(f"Error inserting data into SQL Server: {e}")

def main():
    df = fetch_data_from_ftp()
    if df is not None:
        insert_data_to_sql(df)

if __name__ == "__main__":
    main()
