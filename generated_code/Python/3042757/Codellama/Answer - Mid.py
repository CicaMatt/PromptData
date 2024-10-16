import requests
from bs4 import BeautifulSoup
import os

url = "http://www.gunnerkrigg.com"
folder_name = 'my_webcomics'
os.makedirs(folder_name, exist_ok=True)

# get the page content
response = requests.get(url)
soup = BeautifulSoup(response.content, 'html.parser')

# find all image links on the page
image_links = [img['src'] for img in soup.find_all('img')]

# download each image and save it to a file with the same name as the link
for link in image_links:
    filename = os.path.basename(link)
    full_filename = f"{folder_name}/{filename}"
    response = requests.get(url + link, stream=True)
    with open(full_filename, 'wb') as file:
        for chunk in response.iter_content(1024):
            file.write(chunk)