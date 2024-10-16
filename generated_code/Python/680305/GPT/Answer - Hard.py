import requests
import sys
import os

# Constants
FROM_ADDR = 'my@email.com'
URL = "http://somedomain.com/?action=analyze"
PROXY = {
    # For testing with Burp Proxy
    'http': 'http://127.0.0.1:8080',  
    'https': 'http://127.0.0.1:8080',
}

# Securely handle the file and POST request
def upload_file(file_path, analysis_type='file', notification_type='email', email=FROM_ADDR):
    if not os.path.isfile(file_path):
        print(f"Error: file '{file_path}' does not exist or cannot be opened.")
        sys.exit(1)
    
    try:
        # Opening the file safely with context manager to avoid issues
        with open(file_path, 'rb') as file:
            # Prepare the multipart-form data
            files = {
                'executable': (os.path.basename(file_path), file, 'application/octet-stream'),
            }
            data = {
                'analysisType': analysis_type,
                'notification': notification_type,
                'email': email
            }
            
            # Making the POST request securely
            response = requests.post(URL, files=files, data=data, proxies=PROXY)
            
            # Check for successful response
            response.raise_for_status()
            
            # Print the final URL for verification
            print("File uploaded successfully. Response URL:", response.url)
            return response.text

    except requests.exceptions.RequestException as e:
        print(f"File upload failed: {str(e)}")
        sys.exit(1)

# Usage
if __name__ == '__main__':
    # Replace 'your_file_path_here' with the actual file path
    upload_file('your_file_path_here')
