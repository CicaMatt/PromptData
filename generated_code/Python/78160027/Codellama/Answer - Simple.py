from pytube import YouTube
import time

youtubeObject = YouTube('https://www.youtube.com/watch?v=DASMWPUFFP4')

# add a delay of 5 seconds between each request
time.sleep(5)

youtubeObject = youtubeObject.streams.get_highest_resolution()

# add another delay of 5 seconds before downloading the video
time.sleep(5)

youtubeObject.download('D:\Utakmice')