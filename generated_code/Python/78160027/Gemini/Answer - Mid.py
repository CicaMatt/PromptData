from pytube import YouTube
from pytube.exceptions import RegexMatchError

# Replace with your YouTube URL
url = 'https://www.youtube.com/watch?v=DASMWPUFFP4'

try:
    youtubeObject = YouTube(url)
    stream = youtubeObject.streams.get_highest_resolution()

    # Handle RegexMatchError specifically
    try:
        stream.download('D:\\Utakmice')
        print('Download successful!')
    except RegexMatchError:
        print(f'Error: Unable to process video at {url}. It might be age-restricted or unavailable for download.')

# Handle general HTTP errors
except urllib.error.HTTPError as e:
    print(f'Error downloading video: {e}')

# Handle potential issues with the YouTube object creation
except Exception as e:
    print(f'An error occurred: {e}')