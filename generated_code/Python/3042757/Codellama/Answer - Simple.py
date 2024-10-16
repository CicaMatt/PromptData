import requests
import os

url = "http://www.gunnerkrigg.com//comics/00000001.jpg"
filename = "00000001.jpg"
folder_path = "/Users/yourusername/Desktop/" # change this to the path where you want to save the file
if not os.path.exists(folder_path):
    os.makedirs(folder_path)
with open(os.path.join(folder_path, filename), 'wb') as f:
    r = requests.get(url, allow_redirects=True)
    f.write(r.content)