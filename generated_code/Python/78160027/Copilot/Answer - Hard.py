from pytube.innertube import _default_clients
from pytube import YouTube

# Update client versions
_default_clients["ANDROID"]["context"]["client"]["clientVersion"] = "19.08.35"
_default_clients["IOS"]["context"]["client"]["clientVersion"] = "19.08.35"
_default_clients["ANDROID_EMBED"]["context"]["client"]["clientVersion"] = "19.08.35"
_default_clients["IOS_EMBED"]["context"]["client"]["clientVersion"] = "19.08.35"
_default_clients["IOS_MUSIC"]["context"]["client"]["clientVersion"] = "6.41"
_default_clients["ANDROID_MUSIC"] = _default_clients["ANDROID_CREATOR"]

# Download video
youtubeObject = YouTube('https://www.youtube.com/watch?v=DASMWPUFFP4')
video = youtubeObject.streams.get_highest_resolution()
video.start = 0  # Start time in seconds
video.end = 100 * 60  # End time in seconds (100 minutes)
video.download('D:\\Utakmice')
