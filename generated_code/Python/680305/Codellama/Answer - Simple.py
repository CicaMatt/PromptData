import urllib2
import MultipartPostHandler

FROM_ADDR = 'my@email.com'
file_path = 'path/to/your/file'

try:
    data = open(file_path, 'rb').read()
except:
    print("Error: could not open file %s for reading" % file_path)
    print("Check permissions on the file or folder it resides in")
    sys.exit(1)

# Build the POST request
url = "http://somedomain.com/?action=analyze"
post_data = {
    'analysisType': 'file',
    'executable': data,
    'notification': 'email',
    'email': FROM_ADDR
}

# MIME encode the POST payload
opener = urllib2.build_opener(MultipartPostHandler.MultipartPostHandler)
urllib2.install_opener(opener)
request = urllib2.Request(url, post_data)
request.set_proxy('127.0.0.1:8080', 'http')  # For testing with Burp Proxy

# Make the request and capture the response
try:
    response = urllib2.urlopen(request)
    print(response.geturl())
except urllib2.URLError as e:
    print ("File upload failed:", e.reason)
