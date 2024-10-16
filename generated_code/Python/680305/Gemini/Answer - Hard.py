import urllib2
import MultipartPostHandler

FROM_ADDR = 'my@email.com'

try:
    with open(file, 'rb') as f:
        data = f.read()
except IOError as e:
    print(f"Error: could not open file {file} for reading: {e}")
    print("Check permissions on the file or folder it resides in")
    sys.exit(1)

# Build the POST request
url = "http://somedomain.com/?action=analyze"

# Create a list of tuples to represent the multipart form data
post_data = [
    ('analysisType', 'file'),
    ('executable', data),
    ('notification', 'email'),
    ('email', FROM_ADDR)
]

# MIME encode the POST payload using MultipartPostHandler
opener = urllib2.build_opener(MultipartPostHandler.MultipartPostHandler)
request = urllib2.Request(url)
request.set_proxy('127.0.0.1:8080', 'http')  # For testing with Burp Proxy

# Add the multipart form data to the request
request.add_data(post_data)

# Make the request and capture the response
try:
    response = opener.open(request)
    print(response.geturl())
except urllib2.URLError as e:
    print("File upload failed:", e)