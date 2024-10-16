import requests

FROM_ADDR = 'my@email.com'
file_path = 'path/to/your/file'

try:
    with open(file_path, 'rb') as f:
        data = f.read()
except IOError:
    print(f"Error: could not open file {file_path} for reading")
    print("Check permissions on the file or folder it resides in")
    sys.exit(1)

# Build the POST request
url = "http://somedomain.com/?action=analyze"
files = {'executable': ('filename', data)}
post_data = {
    'analysisType': 'file',
    'notification': 'email',
    'email': FROM_ADDR
}

# Make the request and capture the response
try:
    response = requests.post(url, files=files, data=post_data, proxies={'http': 'http://127.0.0.1:8080'})
    print(response.url)
except requests.RequestException as e:
    print("File upload failed...")
    print(e)
