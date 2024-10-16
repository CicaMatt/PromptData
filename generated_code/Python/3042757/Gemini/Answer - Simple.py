import urllib.request  # Use urllib.request for more modern Python versions
import os

def download_webcomic(base_url, start_num, end_num, save_folder='webcomics'):
    """Downloads a series of webcomics from a given URL pattern."""

    # Create the save folder if it doesn't exist
    if not os.path.exists(save_folder):
        os.makedirs(save_folder)

    for comic_num in range(start_num, end_num + 1):
        # Format the comic URL
        comic_url = base_url.format(str(comic_num).zfill(8))  # zfill ensures leading zeros

        # Format the save filename
        save_path = os.path.join(save_folder, f"{str(comic_num).zfill(8)}.jpg")

        try:
            urllib.request.urlretrieve(comic_url, save_path)
            print(f"Downloaded comic {comic_num}")
        except urllib.error.HTTPError as e:
            if e.code == 404:  # Not Found
                print(f"Comic {comic_num} not found, stopping download.")
                break  # Stop if we hit a missing comic
            else:
                print(f"Error downloading comic {comic_num}: {e}")

# Example usage for Gunnerkrigg Court
base_url = "http://www.gunnerkrigg.com//comics/{}.jpg"
start_num = 1
end_num = 10  # You'll need to find the actual latest comic number

download_webcomic(base_url, start_num, end_num)