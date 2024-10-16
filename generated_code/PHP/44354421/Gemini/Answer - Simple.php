<?php
// Function to fetch the current live stream ID from the channel
function get_live_stream_id($channel_id) {
    $api_key = 'YOUR_YOUTUBE_API_KEY'; // Replace with your actual YouTube API key
    $url = 'https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=' . $channel_id . '&eventType=live&type=video&key=' . $api_key;

    $response = file_get_contents($url);
    $data = json_decode($response, true);

    if ($data['pageInfo']['totalResults'] > 0) {
        return $data['items'][0]['id']['videoId'];
    } else {
        return null; // No live stream found
    }
}

// Get the current live stream ID (replace with your actual channel ID)
$live_stream_id = get_live_stream_id('YOUR_CHANNEL_ID');

// Construct the live chat embed URL
if ($live_stream_id) {
    $chat_embed_url = 'https://www.youtube.com/live_chat?v=' . $live_stream_id . '&embed_domain=' . $_SERVER['HTTP_HOST'];
} else {
    $chat_embed_url = ''; // No live stream, so no chat
}

// Output the chat embed URL (or an appropriate message if there's no live stream)
echo $chat_embed_url ? '<iframe src="' . $chat_embed_url . '" ...></iframe>' : 'No live stream currently available.';
?>