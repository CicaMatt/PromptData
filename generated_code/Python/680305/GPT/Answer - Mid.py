import requests

FROM_ADDR = 'my@email.com'
file_path = 'path_to_your_file'

try:
    with open(file_path, 'rb') as file_data:
        files = {'executable': file_data}
        post_data = {
            'analysisType': 'file',
            'notification': 'email',
            'email': FROM_ADDR
        }
        
        # URL for file upload
        url = "http://somedomain.com/?action=analyze"
        
        # Send the POST request with multipart/form-data
        response = requests.post(url, files=files, data=post_data, proxies={'http': 'http://127.0.0.1:8080'})
        
        # Check for a successful response
        if response.status_code == 200:
            print("Upload successful. Response URL:", response.url)
        else:
            print("Upload failed with status code:", response.status_code)

except FileNotFoundError:
    print(f"Error: could not open file {file_path} for reading")
    print("Check permissions on the file or folder it resides in")

except requests.exceptions.RequestException as e:
    print(f"File upload failed with error: {e}")
