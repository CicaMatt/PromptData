import os
import requests

# Function to download an image and save it to a local folder
def download_image(url, save_folder, image_name):
    response = requests.get(url, stream=True)
    if response.status_code == 200:
        # Create the folder if it doesn't exist
        if not os.path.exists(save_folder):
            os.makedirs(save_folder)

        image_path = os.path.join(save_folder, image_name)
        with open(image_path, 'wb') as file:
            file.write(response.content)
        print(f"Downloaded {image_name}")
    else:
        print(f"Failed to download {image_name}, status code: {response.status_code}")

# Set the base URL for the webcomic images
base_url = "http://www.gunnerkrigg.com//comics/"

# Set the folder on the Desktop where you want to save the images
desktop_folder = os.path.join(os.path.expanduser("~"), "Desktop", "Webcomics")

# Loop through the comic image numbers (e.g., 00000001 to some upper limit)
for i in range(1, 10):  # Adjust the range to the number of comic pages you want
    image_number = f"{i:08}.jpg"  # This will format numbers like '00000001', '00000002', etc.
    image_url = base_url + image_number

    try:
        download_image(image_url, desktop_folder, image_number)
    except Exception as e:
        print(f"An error occurred: {e}")
        break
