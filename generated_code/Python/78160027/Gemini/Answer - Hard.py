from pytube import YouTube
from pytube.exceptions import RegexMatchError, VideoUnavailable

def download_video(url, save_path='D:\\Utakmice'):
    try:
        yt = YouTube(url)

        # Check video availability and age restrictions
        if yt.age_restricted or not yt.streams:
            print(f"Error: Video is age-restricted or unavailable: {url}")
            return

        # Filter for high-resolution streams and download
        stream = yt.streams.filter(progressive=True, file_extension='mp4').order_by('resolution').desc().first()
        if stream:
            print(f"Downloading: {yt.title} ({stream.resolution})")
            stream.download(save_path)
            print("Download complete!")
        else:
            print(f"Error: No suitable stream found for: {url}")

    except RegexMatchError:
        print(f"Error: Invalid YouTube URL: {url}")
    except VideoUnavailable:
        print(f"Error: Video is unavailable: {url}")
    except Exception as e:
        print(f"An unexpected error occurred: {e}")

# Example usage
video_url = 'https://www.youtube.com/watch?v=DASMWPUFFP4'  # Replace with your desired URL
download_video(video_url)