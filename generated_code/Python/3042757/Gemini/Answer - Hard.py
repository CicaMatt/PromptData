import os
import requests
from bs4 import BeautifulSoup

def download_webcomic(base_url, start_num, end_num, save_dir):
    """
    Downloads webcomic images from a specified URL range and saves them to a directory.

    Args:
        base_url: The base URL of the webcomic images, containing a placeholder for the image number.
        start_num: The starting image number.
        end_num: The ending image number.
        save_dir: The directory where the images will be saved.
    """

    # Create the save directory if it doesn't exist
    os.makedirs(save_dir, exist_ok=True)

    for image_num in range(start_num, end_num + 1):
        # Format the image URL with the current image number
        image_url = base_url.format(str(image_num).zfill(8))  # Ensure 8-digit formatting

        try:
            # Send a GET request to the image URL
            response = requests.get(image_url)

            # Check if the request was successful (status code 200)
            if response.status_code == 200:
                # Construct the file path for saving the image
                file_path = os.path.join(save_dir, f"{str(image_num).zfill(8)}.jpg")

                # Save the image content to the file
                with open(file_path, "wb") as f:
                    f.write(response.content)

                print(f"Downloaded: {image_url}")
            else:
                print(f"Failed to download: {image_url} (Status code: {response.status_code})")

        except requests.exceptions.RequestException as e:
            print(f"Error downloading {image_url}: {e}")

if __name__ == "__main__":
    # Example usage (replace with your desired values)
    base_url = "http://www.gunnerkrigg.com//comics/{}.jpg"
    start_num = 1
    end_num = 10  # Replace with the actual last image number or use a method to determine it
    save_dir = os.path.join(os.path.expanduser("~"), "Desktop", "webcomics") 

    download_webcomic(base_url, start_num, end_num, save_dir)