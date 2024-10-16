import os
from pytube import YouTube
from pytube.exceptions import VideoUnavailable, PytubeError
import urllib.error
import time

# Constants
MAX_RETRIES = 3
RETRY_DELAY = 5  # seconds
DOWNLOAD_PATH = 'D:\\Utakmice'  # Ensure this path exists or modify as needed

def download_youtube_video(url: str, download_path: str):
    """
    Download a YouTube video at the highest available resolution.
    Includes error handling for HTTP and PyTube-specific issues.
    
    :param url: The YouTube video URL
    :param download_path: The directory path to download the video
    :return: None
    """
    if not url.startswith("https://www.youtube.com/"):
        raise ValueError("Invalid YouTube URL provided.")
    
    for attempt in range(1, MAX_RETRIES + 1):
        try:
            print(f"Attempt {attempt} to download the video.")
            
            youtube_object = YouTube(url)
            video_stream = youtube_object.streams.get_highest_resolution()

            # Check if download path exists
            if not os.path.exists(download_path):
                os.makedirs(download_path)

            print("Downloading...")
            video_stream.download(download_path)
            print(f"Video downloaded successfully to {download_path}")
            break

        except VideoUnavailable:
            print(f"Error: Video at {url} is unavailable.")
            break

        except urllib.error.HTTPError as e:
            print(f"HTTP Error: {e.code} - {e.reason}. Retrying in {RETRY_DELAY} seconds.")
            if attempt == MAX_RETRIES:
                print("Max retries reached. Download failed.")
            time.sleep(RETRY_DELAY)

        except PytubeError as e:
            print(f"PyTube encountered an issue: {str(e)}. Retrying in {RETRY_DELAY} seconds.")
            if attempt == MAX_RETRIES:
                print("Max retries reached. Download failed.")
            time.sleep(RETRY_DELAY)

        except Exception as e:
            print(f"An unexpected error occurred: {str(e)}")
            break


if __name__ == "__main__":
    video_url = 'https://www.youtube.com/watch?v=DASMWPUFFP4'  # Replace with the actual video URL
    try:
        download_youtube_video(video_url, DOWNLOAD_PATH)
    except ValueError as ve:
        print(f"Input Error: {str(ve)}")
    except Exception as e:
        print(f"Critical Error: {str(e)}")
