from pytube import YouTube
from pytube.exceptions import VideoUnavailable, RegexMatchError
import time

def download_video(url, download_path):
    try:
        youtube_object = YouTube(url)
        stream = youtube_object.streams.get_highest_resolution()
        print("Downloading video...")
        stream.download(download_path)
        print("Download completed!")
    except VideoUnavailable:
        print("The video is unavailable.")
    except RegexMatchError:
        print("The video URL is invalid.")
    except urllib.error.HTTPError as e:
        if e.code == 400:
            print("Encountered HTTP 400 error, retrying in a moment...")
            time.sleep(5)  # Wait and retry
            download_video(url, download_path)
        else:
            print(f"An unexpected error occurred: {e}")

# Example usage
download_video('https://www.youtube.com/watch?v=DASMWPUFFP4', 'D:\\Utakmice')
