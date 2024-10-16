import urllib.request
import pytube

youtube_url = 'https://www.youtube.com/watch?v=DASMWPUFFP4'
video_file = pytube.YouTube(youtube_url).streams.get_highest_resolution().download('D:\Utakmice')