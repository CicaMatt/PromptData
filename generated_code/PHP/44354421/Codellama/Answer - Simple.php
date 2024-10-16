<?php
// Get the current YouTube channel's live stream URL
$channel_url = 'https://www.youtube.com/embed/live_stream?channel=CHANNEL_ID';
$video_id = get_yt_video_id($channel_url);

// Replace the Video ID in the chat embed URL with the latest one
$chat_url = str_replace('VIDEOID', $video_id, 'https://www.youtube.com/live_chat?v=VIDEOID&embed_domain=DOMAINURL');
?>