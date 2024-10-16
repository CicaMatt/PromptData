import requests

FROM_ADDR = 'my@email.com'

try:
    with open(file, 'rb') as f:
        file_data = f.read()
except FileNotFoundError:
    print(f"Error: could not open file {file} for reading")
    print("Check permissions on the file or folder it resides in")
    sys.exit(1)

url = "http://somedomain.com/?action=analyze"

files = {'executable': file_data}
data = {
    'analysisType': 'file',
    'notification': 'email',
    'email': FROM_ADDR
}

try:
    response = requests.post(url, files=files, data=data, proxies={'http': '127.0.0.1:8080'}) 
    response.raise_for_status()  # Raise an exception for bad status codes

    print(response.url)
except requests.exceptions.RequestException as e:
    print(f"File upload failed: {e}")