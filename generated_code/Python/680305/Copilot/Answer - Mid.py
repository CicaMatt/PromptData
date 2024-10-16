import requests

FROM_ADDR = 'my@email.com'
file_path = 'path/to/your/file'

try:
    with open(file_path, 'rb') as f:
        files = {'executable': f}
        data = {
            'analysisType': 'file',
            'notification': 'email',
            'email': FROM_ADDR
        }
        url = "http://somedomain.com/?action=analyze"
        response = requests.post(url, files=files, data=data, proxies={'http': 'http://127.0.0.1:8080'})
        print(response.url)
except IOError:
    print(f"Error: could not open file {file_path} for reading")
    print("Check permissions on the file or folder it resides in")
except requests.RequestException as e:
    print("File upload failed...")
    print(e)
