import os
import requests

# Define the URL pattern and the range of comic numbers
base_url = "http://www.gunnerkrigg.com//comics/"
start_num = 1
end_num = 10  # Adjust this to the number of comics you want to download

# Define the folder path on your desktop
desktop_path = os.path.join(os.path.expanduser("~"), "Desktop", "Webcomics")
os.makedirs(desktop_path, exist_ok=True)

for i in range(start_num, end_num + 1):
    comic_num = str(i).zfill(8)  # Format the number with leading zeros
    image_url = f"{base_url}{comic_num}.jpg"
    image_path = os.path.join(desktop_path, f"{comic_num}.jpg")

    response = requests.get(image_url)
    if response.status_code == 200:
        with open(image_path, 'wb') as file:
            file.write(response.content)
        print(f"Downloaded {comic_num}.jpg")
    else:
        print(f"Failed to download {comic_num}.jpg")
