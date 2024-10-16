import requests

FROM_ADDR = 'my@email.com'
file_path = 'path_to_your_file'

# Prepare the POST request
url = "http://somedomain.com/?action=analyze"
post_data = {
    'analysisType': 'file',
    'notification': 'email',
    'email': FROM_ADDR
}

# Open the file in binary mode for upload
try:
    with open(file_path, 'rb') as f:
        files = {
            'executable': f
        }

        # Send the POST request with multipart/form-data
        response = requests.post(url, data=post_data, files=files, proxies={'http': 'http://127.0.0.1:8080'})  # Proxy is optional
        print(response.url)
        print(response.status_code)
        print(response.text)

except IOError:
    print(f"Error: could not open file {file_path} for reading")
    print("Check permissions on the file or folder it resides in")
