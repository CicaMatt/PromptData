import os
import requests

def download_comic(url, save_path):
    try:
        response = requests.get(url)
        response.raise_for_status()  # Check for HTTP errors
        with open(save_path, 'wb') as file:
            file.write(response.content)
        print(f"Downloaded: {save_path}")
    except requests.exceptions.RequestException as e:
        print(f"Failed to download {url}: {e}")

def main():
    desktop_path = os.path.join(os.path.expanduser("~"), "Desktop", "Webcomics")
    os.makedirs(desktop_path, exist_ok=True)

    base_url = "http://www.gunnerkrigg.com//comics/"
    start_num = 1
    end_num = 10  # Adjust this to the number of comics you want to download

    for i in range(start_num, end_num + 1):
        comic_num = str(i).zfill(8)
        comic_url = f"{base_url}{comic_num}.jpg"
        save_path = os.path.join(desktop_path, f"{comic_num}.jpg")
        download_comic(comic_url, save_path)

if __name__ == "__main__":
    main()
