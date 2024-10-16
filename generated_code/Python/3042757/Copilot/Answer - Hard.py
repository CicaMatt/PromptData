import os
import requests

def download_image(url, folder_path, file_name):
    response = requests.get(url)
    if response.status_code == 200:
        with open(os.path.join(folder_path, file_name), 'wb') as file:
            file.write(response.content)
        print(f"Downloaded {file_name}")
    else:
        print(f"Failed to download {file_name}")

def main():
    # Define the base URL and the range of comic numbers
    base_url = "http://www.gunnerkrigg.com//comics/"
    start_num = 1
    end_num = 10  # Adjust this to the number of comics you want to download

    # Create a folder on the desktop
    desktop_path = os.path.join(os.path.expanduser("~"), "Desktop")
    folder_path = os.path.join(desktop_path, "Webcomics")
    os.makedirs(folder_path, exist_ok=True)

    # Download each comic
    for num in range(start_num, end_num + 1):
        file_name = f"{num:08}.jpg"
        url = f"{base_url}{file_name}"
        download_image(url, folder_path, file_name)

if __name__ == "__main__":
    main()
