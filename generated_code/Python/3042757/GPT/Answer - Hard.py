import os
import requests
from pathlib import Path
from time import sleep

# Constants
BASE_URL = "http://www.gunnerkrigg.com/comics/{:08d}.jpg"  
DOWNLOAD_FOLDER = str(Path.home() / 'Desktop' / 'Webcomics')  
START_INDEX = 1  
RETRY_LIMIT = 3  
MAX_COMICS = 10000  

def create_download_folder(folder_path):
    """
    Creates the download folder if it does not exist.
    """
    try:
        os.makedirs(folder_path, exist_ok=True)
        print(f"Download folder created at: {folder_path}")
    except Exception as e:
        print(f"Error creating download folder: {e}")
        raise

def download_image(image_url, image_path):
    """
    Downloads a single image from the provided URL and saves it to the specified path.
    """
    try:
        response = requests.get(image_url, timeout=10)
        response.raise_for_status() 
        with open(image_path, 'wb') as f:
            f.write(response.content)
        print(f"Downloaded: {image_path}")
        return True
    except requests.exceptions.RequestException as e:
        print(f"Failed to download {image_url}: {e}")
        return False

def download_webcomics(start_index=START_INDEX, max_comics=MAX_COMICS):
    """
    Downloads webcomics starting from the start_index, stopping if the file doesn't exist
    or after reaching the max_comics limit.
    """
    create_download_folder(DOWNLOAD_FOLDER)
    
    for comic_number in range(start_index, max_comics + 1):
        image_url = BASE_URL.format(comic_number)
        image_filename = f"{comic_number:08d}.jpg"
        image_path = os.path.join(DOWNLOAD_FOLDER, image_filename)
        
        retries = 0
        success = False

        while retries < RETRY_LIMIT and not success:
            success = download_image(image_url, image_path)
            if not success:
                retries += 1
                print(f"Retrying {comic_number:08d}... ({retries}/{RETRY_LIMIT})")
                sleep(2) 
