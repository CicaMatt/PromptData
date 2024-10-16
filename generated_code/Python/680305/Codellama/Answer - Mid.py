import urllib2
import MultipartPostHandler

FROM_ADDR = 'my@email.com'

# Build the POST request
url = "http://somedomain.com/?action=analyze"       
post_data = {}
post_data['analysisType'] = 'file'
post_data['executable'] = open(file, 'rb').read()
post_data['notification'] = 'email'
post_data['email'] = FROM_ADDR

# MIME encode the POST payload
opener = urllib2.build_opener(MultipartPostHandler.MultipartPostHandler)
urllib2.install_opener(opener)
request = urllib2.Request(url, post_data)
request.set_proxy('127.0.0.1:8080', 'http') # For testing with Burp Proxy

# Make the request and capture the response
try:
    response = urllib2.urlopen(opener)
    print(response.geturl())
except urllib2.URLError as e:
    print("File upload failed...")