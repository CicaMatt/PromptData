import requests
from pytube import YouTube

youtubeObject = YouTube('https://www.youtube.com/watch?v=DASMWPUFFP4')

# Download the video in the highest resolution available
youtubeObject = youtubeObject.streams.get_highest_resolution()

# Create a temporary folder to store the downloaded file
temp_folder = 'D:/Utakmice'
if not os.path.exists(temp_folder):
    os.makedirs(temp_folder)

# Download the video using the requests library
with open(f"{temp_folder}/{youtubeObject.title}.mp4", "wb") as f:
    r = requests.get(youtubeObject.url, stream=True)
    for chunk in r.iter_content(chunk_size=1024):
        if chunk:
            f.write(chunk)