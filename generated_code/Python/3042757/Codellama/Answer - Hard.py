import urllib.request

# The URL of the image to download
image_url = "http://www.gunnerkrigg.com//comics/00000001.jpg"

# The path where you want to save the downloaded image
output_path = "/path/to/folder/on/your/desktop"

# Download the image and save it to the specified folder
with urllib.request.urlopen(image_url) as response, open(output_path, 'wb') as out_file:
    data = response.read()
    out_file.write(data)