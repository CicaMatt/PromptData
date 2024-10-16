from pytube import YouTube
from pytube.exceptions import VideoUnavailable
import time

# Retry logic
def download_video(url, output_path, retries=3):
    attempt = 0
    while attempt < retries:
        try:
            youtube_object = YouTube(url)
            stream = youtube_object.streams.get_highest_resolution()
            stream.download(output_path)
            print("Download complete!")
            return
        except VideoUnavailable:
            print("Video is unavailable.")
            return
        except Exception as e:
            print(f"Error: {e}")
            attempt += 1
            if attempt < retries:
                print("Retrying in 5 seconds...")
                time.sleep(5)
            else:
                print("Failed after multiple attempts.")

# Example usage:
download_video('https://www.youtube.com/watch?v=DASMWPUFFP4', 'D:\\Utakmice')
