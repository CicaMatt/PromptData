import pyodbc
import pandas as pd
from ftplib import FTP_TLS  # Use secure FTP
from io import StringIO
import csv

# Function to fetch data from FTP
def fetch_data_from_ftp():
    try:
        # Connect to FTP securely using FTP_TLS
        ftp = FTP_TLS('ftp.xyz.com', 'user', 'pass')
        ftp.prot_p()  # Switch to secure data connection
        ftp.set_pasv(True)
        
        # Prepare in-memory file-like object for reading the CSV
        r = StringIO()
        
        # Fetch the CSV file and write its contents to the in-memory object
        ftp.retrbinary('RETR filename.csv', r.write)
        
        # Move the pointer of the StringIO object to the start
        r.seek(0)
        
        # Use pandas to read the CSV data into a DataFrame
        df = pd.read_csv(r, delimiter=',')
        
        # Close FTP connection
        ftp.quit()
        
        # Return the pandas DataFrame containing the CSV data
        return df
    
    except Exception as e:
        print(f"Error fetching data from FTP: {e}")
        raise

# Function to insert data into SQL Server
def insert_data_to_sql_server(df):
    try:
        # Connection string for MS SQL Server
        conn_str = (
            'DRIVER={SQL Server Native Client 10.0};'
            'SERVER=localhost;'
            'DATABASE=TESTFEED;'
            'UID=sa;'
            'PWD=pass'
        )
        
        # Establish the database connection using pyodbc
        with pyodbc.connect(conn_str) as conn:
            cursor = conn.cursor()
            
            # Prepare the insert statement with placeholders for parameterized query
            insert_stmt = """
            INSERT INTO dbo.tblImport (Startdt, Enddt, x, y, z)
            VALUES (?, ?, ?, ?, ?)
            """
            
            # Iterate over the DataFrame rows and insert each row into the database
            for index, row in df.iterrows():
                cursor.execute(insert_stmt, row['Startdt'], row['Enddt'], row['x'], row['y'], row['z'])
            
            # Commit the transaction
            conn.commit()
            
            print("Data successfully inserted into SQL Server.")
    
    except Exception as e:
        print(f"Error inserting data into SQL Server: {e}")
        raise

# Main function to execute the process
def main():
    try:
        # Fetch data from FTP server
        df = fetch_data_from_ftp()
        
        # Insert the fetched data into SQL Server
        insert_data_to_sql_server(df)
    
    except Exception as e:
        print(f"An error occurred during the process: {e}")

if __name__ == "__main__":
    main()
