from pytube import YouTube
from pytube.innertube import _default_clients

# Update client versions to fix the issue
_default_clients["ANDROID"]["context"]["client"]["clientVersion"] = "19.08.35"
_default_clients["IOS"]["context"]["client"]["clientVersion"] = "19.08.35"
_default_clients["ANDROID_EMBED"]["context"]["client"]["clientVersion"] = "19.08.35"
_default_clients["IOS_EMBED"]["context"]["client"]["clientVersion"] = "19.08.35"
_default_clients["IOS_MUSIC"]["context"]["client"]["clientVersion"] = "6.41"

# Your original code
youtubeObject = YouTube('https://www.youtube.com/watch?v=DASMWPUFFP4')
youtubeObject = youtubeObject.streams.get_highest_resolution()
youtubeObject.download('D:\\Utakmice')
