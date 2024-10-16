import os
import requests
from bs4 import BeautifulSoup

def download_webcomic(base_url, start_num, end_num, save_folder):
    """Downloads webcomic images from a given URL range and saves them to a folder."""

    if not os.path.exists(save_folder):
        os.makedirs(save_folder)  # Create the save folder if it doesn't exist

    for comic_num in range(start_num, end_num + 1):
        comic_url = base_url.replace("00000001", f"{comic_num:08d}")  # Format the comic number
        response = requests.get(comic_url)

        if response.status_code == 200:
            filename = os.path.join(save_folder, f"{comic_num:08d}.jpg")
            with open(filename, 'wb') as f:
                f.write(response.content)
            print(f"Downloaded: {filename}")
        else:
            print(f"Failed to download: {comic_url}")

# Example usage (replace with your desired webcomic details)
base_url = "http://www.gunnerkrigg.com//comics/00000001.jpg"
start_num = 1
end_num = 10  # Adjust as needed or determine dynamically
save_folder = os.path.join(os.path.expanduser("~"), "Desktop", "Webcomics")

download_webcomic(base_url, start_num, end_num, save_folder)