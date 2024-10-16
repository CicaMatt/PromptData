from MultipartPostHandler import MultipartPostHandler
import urllib2

FROM_ADDR = 'my@email.com'

try:
    data = open(file, 'rb').read()
except:
    print("Error: could not open file %s for reading" % file)
    print("Check permissions on the file or folder it resides in")
    sys.exit(1)

# Build the POST request
url = "http://somedomain.com/?action=analyze"       
post_data = {}
post_data['analysisType'] = 'file'
post_data['executable'] = data
post_data['notification'] = 'email'
post_data['email'] = FROM_ADDR

# MIME encode the POST payload
opener = urllib2.build_opener(MultipartPostHandler)
urllib2.install_opener(opener)
request = urllib2.Request(url, post_data)
encoded_post_data = opener.multipart.encode(post_data)

# Make the request and capture the response
try:
    request.set_data(encoded_post_data)
    response = urllib2.urlopen(request)
    print(response.geturl())
except urllib2.URLError as e:
    print("File upload failed...")