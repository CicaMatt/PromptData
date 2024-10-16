from pytube import YouTube
import time

def download_with_retry(url, retries=3, delay=5):
    for attempt in range(retries):
        try:
            youtubeObject = YouTube(url)
            stream = youtubeObject.streams.filter(progressive=True).order_by('resolution').desc().first()  # Try a slightly lower quality
            stream.download('D:\\Utakmice')
            return  # Exit the function if successful
        except urllib.error.HTTPError as e:
            if e.code == 400 and attempt < retries - 1:
                print(f"Attempt {attempt + 1} failed with HTTP Error 400. Retrying in {delay} seconds...")
                time.sleep(delay)
            else:
                raise e  # Re-raise the exception if all retries fail

# Example usage
download_with_retry('https://www.youtube.com/watch?v=DASMWPUFFP4')