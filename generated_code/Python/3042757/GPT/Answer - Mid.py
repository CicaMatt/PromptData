import os
import requests

def download_webcomics(start_num, end_num, save_dir, base_url):
    """
    Download webcomics from a base URL and save them to a specified directory.

    :param start_num: The starting number of the comic.
    :param end_num: The ending number of the comic.
    :param save_dir: Directory to save the downloaded comics.
    :param base_url: Base URL for the comic images (with {num} as a placeholder for comic number).
    """
    # Ensure the directory exists
    if not os.path.exists(save_dir):
        os.makedirs(save_dir)

    for num in range(start_num, end_num + 1):
        # Format the number with leading zeros (e.g., 00000001.jpg)
        comic_num = f"{num:08d}.jpg"
        comic_url = base_url.format(num=comic_num)
        file_path = os.path.join(save_dir, comic_num)

        try:
            # Download the image
            response = requests.get(comic_url, stream=True)
            response.raise_for_status()  # Raise an error if the request failed

            # Save the image to the folder
            with open(file_path, 'wb') as file:
                for chunk in response.iter_content(1024):
                    file.write(chunk)
            print(f"Downloaded: {comic_num}")

        except requests.exceptions.RequestException as e:
            print(f"Failed to download {comic_num}: {e}")

if __name__ == "__main__":
    # Define the range of comics you want to download (e.g., from 1 to 100)
    start_comic = 1
    end_comic = 100  # Adjust based on your needs

    # Directory to save comics (adjust as needed)
    desktop = os.path.join(os.path.expanduser("~"), "Desktop")
    save_directory = os.path.join(desktop, "Webcomics")

    # Base URL for the webcomics (you can adjust the domain and URL structure)
    base_comic_url = "http://www.gunnerkrigg.com/comics/{num}"

    # Start downloading
    download_webcomics(start_comic, end_comic, save_directory, base_comic_url)
